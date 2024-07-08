package kaz.olzhas.ylab.servlets.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.WorkspaceDto;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.exception.ValidationParametersException;
import kaz.olzhas.ylab.mapper.WorkspaceMapper;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.state.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class ShowAllWorkspaceServletTest {

    @Mock
    private AdminService adminService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ServletContext servletContext;

    @Mock
    private Authentication authentication;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private WorkspaceMapper workspaceMapper;

    @Mock
    private ServletConfig servletConfig;

    @InjectMocks
    private ShowAllWorkspaceServlet servlet;

    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("adminService")).thenReturn(adminService);
        when(servletContext.getAttribute("objectMapper")).thenReturn(objectMapper);
        when(servletContext.getAttribute("workspaceMapper")).thenReturn(workspaceMapper);
        when(servletContext.getAttribute("authentication")).thenReturn(authentication);

        writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        servlet = new ShowAllWorkspaceServlet();
        servlet.init(servletConfig);
    }

    @Test
    @DisplayName("Successful retrieval of all workspaces")
    public void testShowAllWorkspacesSuccess() throws IOException, ValidationParametersException {
        when(authentication.isAdmin()).thenReturn(true);
        when(authentication.getUsername()).thenReturn("admin");

        Workspace workspace = new Workspace();
        Workspace workspace2 = new Workspace();
        workspace.setName("Workspace1");
        workspace.setName("Workspace2");
        List<Workspace> workspaceList = Arrays.asList(
                workspace,
                workspace2
        );
        when(adminService.seeAllWorkspaces()).thenReturn(workspaceList);

        WorkspaceDto workspaceDto1 = new WorkspaceDto();
        workspaceDto1.setName("Workspace1");
        WorkspaceDto workspaceDto2 = new WorkspaceDto();
        workspaceDto2.setName("Workspace2");

        when(workspaceMapper.toDto(workspaceList.get(0))).thenReturn(workspaceDto1);
        when(workspaceMapper.toDto(workspaceList.get(1))).thenReturn(workspaceDto2);

        List<WorkspaceDto> workspaceDtoList = Arrays.asList(workspaceDto1, workspaceDto2);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(objectMapper).writeValue(any(PrintWriter.class), eq(workspaceDtoList));
    }

    @Test
    @DisplayName("Forbidden access for non-admin user")
    public void testShowAllWorkspacesNotAdmin() throws IOException {
        when(authentication.isAdmin()).thenReturn(false);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response.getWriter()).write("Только админы могут видеть все рабочие места.");
    }

    @Test
    @DisplayName("Username is null, causing validation error")
    public void testShowAllWorkspacesUsernameNull() throws IOException, ValidationParametersException {
        when(authentication.isAdmin()).thenReturn(true);
        when(authentication.getUsername()).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response.getWriter()).write("Имя пользователя пустое!");
    }

    @Test
    @DisplayName("Runtime exception during processing")
    public void testShowAllWorkspacesRuntimeException() throws IOException, ValidationParametersException {
        when(authentication.isAdmin()).thenReturn(true);
        when(authentication.getUsername()).thenReturn("admin");

        when(adminService.seeAllWorkspaces()).thenThrow(new RuntimeException("Internal error"));

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(response.getWriter()).write("Internal error");
    }
}
