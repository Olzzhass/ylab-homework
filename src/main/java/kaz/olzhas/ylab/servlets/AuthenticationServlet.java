package kaz.olzhas.ylab.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.ExceptionResponse;
import kaz.olzhas.ylab.dto.SuccessResponse;
import kaz.olzhas.ylab.dto.UserRequest;
import kaz.olzhas.ylab.exception.AuthorizeException;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.state.Authentication;

import java.io.IOException;

@WebServlet("/authentication")
public class AuthenticationServlet extends HttpServlet {

    private UserService userService;
    private AdminService adminService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        userService = (UserService) getServletContext().getAttribute("userService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
        adminService = (AdminService) getServletContext().getAttribute("adminService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        try {
            UserRequest request = objectMapper.readValue(req.getInputStream(), UserRequest.class);

            boolean authorize = false;

            if(request.username().equalsIgnoreCase("admin")){
                authorize = adminService.authenticateAdmin(request.username(), request.password());
            }else{
                authorize = userService.authenticateUser(request.username(), request.password());
            }

            if(authorize) {
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                Authentication authentication = (Authentication)getServletContext().getAttribute("authentication");
                authentication.setAuth(true);
                authentication.setUsername(request.username());
                objectMapper.writeValue(resp.getWriter(), new SuccessResponse("Добро пожаловать в наш Coworking-Service"));
            }else{
                throw new AuthorizeException("Ошибка при входе в программу.");
            }

        } catch (AuthorizeException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }
}
