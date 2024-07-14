package kaz.olzhas.ylab.servlets.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.BookingDto;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.exception.ValidationParametersException;
import kaz.olzhas.ylab.mapper.BookingMapper;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.state.Authentication;
import java.io.IOException;
import java.util.List;

@WebServlet("/user/my-workspaces")
public class ShowUserWorkspacesServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private UserService userService;
    private BookingMapper bookingMapper;

    @Override
    public void init() throws ServletException {
        super.init();

        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
        userService = (UserService) getServletContext().getAttribute("userService");
        bookingMapper = (BookingMapper) getServletContext().getAttribute("bookingMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");

        if(authentication.isAuth() && !authentication.isAdmin()){
            try {
                showAllWorkspaces(resp, authentication);
            } catch (ValidationParametersException e){
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(e.getMessage());
            }catch (RuntimeException e){
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(e.getMessage());
            }
        }else{
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("Страница только для пользователей!");
        }
    }

    private void showAllWorkspaces(HttpServletResponse resp, Authentication authentication) throws IOException {
        String username = authentication.getUsername();

        if(username == null){
            throw new ValidationParametersException("Имя пользователя пустое!");
        }

        List<Booking> bookings = userService.showAllReservations(username);

        List<BookingDto> bookingsDto = bookings.stream().map(bookingMapper::toDto).toList();

        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), bookingsDto);
    }


}
