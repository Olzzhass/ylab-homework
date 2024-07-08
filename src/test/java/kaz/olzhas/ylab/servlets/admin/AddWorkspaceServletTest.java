package kaz.olzhas.ylab.servlets.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.WorkspaceRequest;
import kaz.olzhas.ylab.exception.ValidationParametersException;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.state.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddWorkspaceServletTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AdminService adminService;

    @Mock
    private ServletContext servletContext;

    @Mock
    private Authentication authentication;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletConfig servletConfig;

    @Mock
    private PrintWriter writer;

    @InjectMocks
    private AddWorkspaceServlet servlet;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("adminService")).thenReturn(adminService);
        when(servletContext.getAttribute("objectMapper")).thenReturn(objectMapper);
        when(servletContext.getAttribute("authentication")).thenReturn(authentication);

        when(response.getWriter()).thenReturn(writer);

        servlet = new AddWorkspaceServlet();
        servlet.init(servletConfig);
    }

    @Test
    @DisplayName("Successful workspace addition")
    public void testAddWorkspace() throws IOException, ValidationParametersException {
        when(authentication.isAdmin()).thenReturn(true);
        when(authentication.getUsername()).thenReturn("admin");

        WorkspaceRequest workspaceRequest = new WorkspaceRequest("Workspace1");
        String jsonRequest = "{\"workspaceName\":\"Workspace1\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));
        when(request.getReader()).thenReturn(reader);

        when(objectMapper.readValue(reader, WorkspaceRequest.class)).thenReturn(workspaceRequest);

        when(adminService.addWorkspace("Workspace1")).thenReturn(true);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);

    }

    @Test
    @DisplayName("Forbidden access for non-admin")
    public void testAddWorkspaceForbiddenAccess() throws IOException {
        when(authentication.isAdmin()).thenReturn(false);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Validation exception on empty username")
    public void testAddWorkspaceValidationException() throws IOException, ValidationParametersException {
        when(authentication.isAdmin()).thenReturn(true);
        when(authentication.getUsername()).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Internal server error on admin service failure")
    public void testAddWorkspaceInternalServerError() throws IOException, ValidationParametersException {
        when(authentication.isAdmin()).thenReturn(true);
        when(authentication.getUsername()).thenReturn("admin");

        WorkspaceRequest workspaceRequest = new WorkspaceRequest("Workspace1");

        when(objectMapper.readValue(any(InputStream.class), eq(WorkspaceRequest.class))).thenReturn(workspaceRequest);

        when(adminService.addWorkspace("Workspace1")).thenReturn(false);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}

