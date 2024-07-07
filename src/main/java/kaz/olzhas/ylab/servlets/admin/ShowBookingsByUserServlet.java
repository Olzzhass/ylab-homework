package kaz.olzhas.ylab.servlets.admin;

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
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.state.Authentication;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/bookings-by-user")
public class ShowBookingsByUserServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private AdminService adminService;
    private BookingMapper bookingMapper;

    @Override
    public void init() throws ServletException {
        super.init();

        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
        adminService = (AdminService) getServletContext().getAttribute("adminService");
        bookingMapper = (BookingMapper) getServletContext().getAttribute("bookingMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");

        if (authentication.isAdmin()) {
            try {
                String userName = String.valueOf(req.getParameter("userName"));
                List<Booking> bookings = adminService.bookingsByUser(userName);

                List<BookingDto> bookingsDto = bookings.stream().map(bookingMapper::toDto).toList();

                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getWriter(), bookingsDto);

            } catch (ValidationParametersException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(e.getMessage());
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(e.getMessage());
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("Только админы могут видеть всех пользователей.");
        }
    }
}
