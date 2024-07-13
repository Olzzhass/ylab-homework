package kaz.olzhas.ylab.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.UserRequest;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.state.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.*;
import static org.mockito.Mockito.*;


public class AuthenticationServletTest {

    @Mock
    private UserService userService;
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
    private ServletConfig servletConfig;
    private AuthenticationServlet servlet;

    PrintWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("userService")).thenReturn(userService);
        when(servletContext.getAttribute("adminService")).thenReturn(adminService);
        when(servletContext.getAttribute("objectMapper")).thenReturn(objectMapper);
        when(servletContext.getAttribute("authentication")).thenReturn(authentication);

        writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);

        servlet = new AuthenticationServlet();
        servlet.init(servletConfig);
    }

    @Test
    @DisplayName("User authentication servlet test")
    public void testAuthenticateServlet() throws IOException {
        String jsonRequest = "{\"username\":\"Olzhas\",\"password\":\"olzhas\"}";

        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));
        when(request.getReader()).thenReturn(reader);

        UserRequest userRequest = new UserRequest("Olzhas", "olzhas");
        when(objectMapper.readValue(reader, UserRequest.class)).thenReturn(userRequest);

        when(userService.authenticateUser("Olzhas", "olzhas")).thenReturn(true);

        servlet.doPost(request, response);

        verify(authentication).setAuth(true);
        verify(authentication).setUsername("Olzhas");

        verify(response).setStatus(HttpServletResponse.SC_ACCEPTED);
    }

    @Test
    @DisplayName("User authentication servlet test on failure")
    public void testAuthenticateServletFailure() throws IOException {
        String jsonRequest = "{\"username\":\"Olzhas\",\"password\":\"wrong\"}";

        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));
        when(request.getReader()).thenReturn(reader);

        UserRequest userRequest = new UserRequest("Olzhas", "wrong");
        when(objectMapper.readValue(reader, UserRequest.class)).thenReturn(userRequest);

        when(userService.authenticateUser("Olzhas", "wrong")).thenReturn(false);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
