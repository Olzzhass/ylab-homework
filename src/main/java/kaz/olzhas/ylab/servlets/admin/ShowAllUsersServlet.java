package kaz.olzhas.ylab.servlets.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.UserDto;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.exception.ValidationParametersException;
import kaz.olzhas.ylab.mapper.UserMapper;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.state.Authentication;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users/all")
public class ShowAllUsersServlet extends HttpServlet {

    private AdminService adminService;
    private ObjectMapper objectMapper;
    private UserMapper userMapper;

    @Override
    public void init() throws ServletException {
        super.init();

        adminService = (AdminService) getServletContext().getAttribute("adminService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
        userMapper = (UserMapper) getServletContext().getAttribute("userMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");

        if(authentication.isAdmin()){
            try {
                showAllUsers(resp, authentication);
            } catch (ValidationParametersException e){
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(e.getMessage());
            }catch (RuntimeException e){
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(e.getMessage());
            }
        }else{
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("Только админы могут видеть всех пользователей.");
        }
    }

    private void showAllUsers(HttpServletResponse resp, Authentication authentication) throws IOException {
        String username = authentication.getUsername();

        if(username == null){
            throw new ValidationParametersException("Имя пользователя пустое!");
        }

        List<User> allUsers = adminService.seeAllUsers();

        List<UserDto> allUserDto = allUsers.stream().map(userMapper::toDto).toList();

        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), allUserDto);
    }
}
