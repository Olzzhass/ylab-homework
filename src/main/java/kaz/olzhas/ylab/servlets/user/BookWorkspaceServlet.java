package kaz.olzhas.ylab.servlets.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.BookingDto;
import kaz.olzhas.ylab.dto.ExceptionResponse;
import kaz.olzhas.ylab.dto.ResponseMessage;
import kaz.olzhas.ylab.exception.AuthorizeException;
import kaz.olzhas.ylab.exception.NotValidArgumentException;
import kaz.olzhas.ylab.service.WorkspaceService;
import kaz.olzhas.ylab.state.Authentication;

import java.io.IOException;

@WebServlet("/booking")
public class BookWorkspaceServlet extends HttpServlet {
    private WorkspaceService workspaceService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        workspaceService = (WorkspaceService) getServletContext().getAttribute("workspaceService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");

        try {

            if (authentication.isAuth()) {

                BookingDto bookingDto = objectMapper.readValue(req.getReader(), BookingDto.class);
                System.out.println(bookingDto.getStart());
                System.out.println(bookingDto.getEnd());
                boolean isBooked = workspaceService.bookWorkspace(bookingDto.getWorkspaceId(), bookingDto.getStart(), bookingDto.getEnd(), authentication.getUsername());

                if (isBooked) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    objectMapper.writeValue(resp.getWriter(), new ResponseMessage("Рабочее место успешно забронировано!"));
                }else{
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("Ошибка бронирования!");
                }
            }else{
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("Сначало нужно войти в приложение!");
            }

        } catch (NotValidArgumentException | AuthorizeException e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }
}
