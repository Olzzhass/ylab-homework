package kaz.olzhas.ylab.servlets.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.UserDto;
import kaz.olzhas.ylab.dto.WorkspaceDto;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.exception.ValidationParametersException;
import kaz.olzhas.ylab.mapper.UserMapper;
import kaz.olzhas.ylab.mapper.WorkspaceMapper;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.state.Authentication;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/workspaces/all")
public class ShowAllWorkspaceServlet extends HttpServlet {
    private AdminService adminService;
    private ObjectMapper objectMapper;
    private WorkspaceMapper workspaceMapper;

    @Override
    public void init() throws ServletException {
        super.init();

        adminService = (AdminService) getServletContext().getAttribute("adminService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
        workspaceMapper = (WorkspaceMapper) getServletContext().getAttribute("workspaceMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");

        if(authentication.isAdmin()){
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
            resp.getWriter().write("Только админы могут видеть все рабочие места.");
        }
    }

    private void showAllWorkspaces(HttpServletResponse resp, Authentication authentication) throws IOException {
        String username = authentication.getUsername();

        if(username == null){
            throw new ValidationParametersException("Имя пользователя пустое!");
        }

        List<Workspace> workspaces = adminService.seeAllWorkspaces();

        List<WorkspaceDto> workspacesDto = workspaces.stream().map(workspaceMapper::toDto).toList();

        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), workspacesDto);
    }

}
