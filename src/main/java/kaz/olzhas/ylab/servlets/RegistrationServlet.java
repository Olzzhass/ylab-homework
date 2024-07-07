package kaz.olzhas.ylab.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.ExceptionResponse;
import kaz.olzhas.ylab.dto.SuccessResponse;
import kaz.olzhas.ylab.dto.UserRequest;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.exception.NotValidArgumentException;
import kaz.olzhas.ylab.exception.RegisterException;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.state.Authentication;

import java.io.IOException;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    private UserService userService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userService = (UserService) getServletContext().getAttribute("userService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {

            UserRequest userRequest = objectMapper.readValue(req.getInputStream(), UserRequest.class);
            boolean registeredUser = userService.registerUser(userRequest.username(), userRequest.password());


            if(registeredUser) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
                authentication.setAuth(true);
                authentication.setUsername(userRequest.username());
                objectMapper.writeValue(resp.getWriter(), new SuccessResponse("Пользователь с именем: " + userRequest.username() + " успешно создан."));
            }else{
                throw new RegisterException("Ошибка при регистрации!");
            }

        }catch (JsonParseException | NotValidArgumentException | RegisterException e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }

        super.doPost(req, resp);
    }
}
