package kaz.olzhas.ylab.servlets.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.SuccessResponse;
import kaz.olzhas.ylab.dto.WorkspaceRequest;
import kaz.olzhas.ylab.exception.ValidationParametersException;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.state.Authentication;

import java.io.IOException;

@WebServlet("/admin/workspaces/new")
public class AddWorkspaceServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private AdminService adminService;

    @Override
    public void init() throws ServletException {
        super.init();
        adminService = (AdminService) getServletContext().getAttribute("adminService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");

        if(authentication.isAdmin()){
            try {
                addWorkspace(req, resp, authentication);
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

    private void addWorkspace(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException {
        String username = authentication.getUsername();

        if(username == null){
            throw new ValidationParametersException("Имя пользователя пустое!");
        }

        WorkspaceRequest workspaceRequest = objectMapper.readValue(req.getInputStream(), WorkspaceRequest.class);

        if(adminService.addWorkspace(workspaceRequest.workspaceName())){
            resp.setStatus(HttpServletResponse.SC_OK);
            SuccessResponse successResponse = new SuccessResponse("Рабочее место успешно добавлено!");
            objectMapper.writeValue(resp.getWriter(), successResponse);
        }else{
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Ошибка сохранения рабочего места.");
        }

    }

}
