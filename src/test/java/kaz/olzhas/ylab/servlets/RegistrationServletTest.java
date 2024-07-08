package kaz.olzhas.ylab.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.UserRequest;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.state.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegistrationServletTest {

    @Mock
    private UserService userService;
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
    private RegistrationServlet servlet;

    PrintWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("userService")).thenReturn(userService);
        when(servletContext.getAttribute("objectMapper")).thenReturn(objectMapper);
        when(servletContext.getAttribute("authentication")).thenReturn(authentication);

        writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);

        servlet = new RegistrationServlet();
        servlet.init(servletConfig);
    }

    @Test
    @DisplayName("User registration successful")
    public void testRegisterUser() throws IOException, ServletException {
        String jsonRequest = "{\"username\":\"Olzhas\",\"password\":\"olzhas\"}";

        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));
        when(request.getReader()).thenReturn(reader);

        UserRequest userRequest = new UserRequest("Olzhas", "olzhas");
        when(objectMapper.readValue(reader, UserRequest.class)).thenReturn(userRequest);

        when(userService.registerUser("Olzhas", "olzhas")).thenReturn(true);

        servlet.doPost(request, response);

        verify(authentication).setAuth(true);
        verify(authentication).setUsername("Olzhas");

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    @DisplayName("User registration failure - user already exists")
    public void testRegisterUserAlreadyExists() throws IOException, ServletException {
        String jsonRequest = "{\"username\":\"Olzhas\",\"password\":\"olzhas\"}";

        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));
        when(request.getReader()).thenReturn(reader);

        UserRequest userRequest = new UserRequest("Olzhas", "olzhas");
        when(objectMapper.readValue(reader, UserRequest.class)).thenReturn(userRequest);

        when(userService.registerUser("Olzhas", "olzhas")).thenReturn(false);

        servlet.doPost(request, response);

        verify(authentication, never()).setAuth(anyBoolean());
        verify(authentication, never()).setUsername(any());

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}

