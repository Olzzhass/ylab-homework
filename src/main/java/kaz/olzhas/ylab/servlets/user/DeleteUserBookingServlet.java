package kaz.olzhas.ylab.servlets.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.DeleteBookingDto;
import kaz.olzhas.ylab.dto.ExceptionResponse;
import kaz.olzhas.ylab.dto.ResponseMessage;
import kaz.olzhas.ylab.service.WorkspaceService;
import kaz.olzhas.ylab.state.Authentication;

import java.io.IOException;

@WebServlet("/user/deleteBooking")
public class DeleteUserBookingServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private WorkspaceService workspaceService;

    @Override
    public void init() throws ServletException {
        super.init();
        workspaceService = (WorkspaceService) getServletContext().getAttribute("workspaceService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");

        try {

            if(authentication.isAuth()){
                DeleteBookingDto deleteBookingDto = objectMapper.readValue(req.getInputStream(), DeleteBookingDto.class);

                boolean isDeleted = workspaceService.deleteReservation(deleteBookingDto.getBookingId());

                if(isDeleted){
                    resp.setStatus(HttpServletResponse.SC_OK);
                    objectMapper.writeValue(resp.getWriter(), new ResponseMessage("Бронирование места успешно удалено!"));
                }else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().write("Ошибка удаления!");
                }

            }else{
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("Сначало нужно войти в приложение!");
            }

        }catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }
}
