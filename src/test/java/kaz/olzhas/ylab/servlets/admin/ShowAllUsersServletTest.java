package kaz.olzhas.ylab.servlets.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.UserDto;
import kaz.olzhas.ylab.dto.UserRequest;
import kaz.olzhas.ylab.dto.WorkspaceDto;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.exception.ValidationParametersException;
import kaz.olzhas.ylab.mapper.UserMapper;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.state.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ShowAllUsersServletTest {

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

    @Mock
    private UserMapper userMapper;

    @Mock
    private PrintWriter writer;

    @Mock
    private UserService userService;

    @InjectMocks
    private ShowAllUsersServlet servlet;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("adminService")).thenReturn(adminService);
        when(servletContext.getAttribute("objectMapper")).thenReturn(objectMapper);
        when(servletContext.getAttribute("authentication")).thenReturn(authentication);
        when(servletContext.getAttribute("userService")).thenReturn(userService);

        when(response.getWriter()).thenReturn(writer);

        servlet = new ShowAllUsersServlet();
        servlet.init(servletConfig);
    }

    @Test
    @DisplayName("Successful retrieval of all users")
    public void testShowAllUsers() throws IOException, ValidationParametersException {
        when(authentication.isAdmin()).thenReturn(true);
        when(authentication.getUsername()).thenReturn("admin");

        User user = new User();
        user.setUsername("Olzhas");
        user.setPassword("olzhas");
        User user1 = new User();
        user1.setUsername("Olzhik");
        user1.setPassword("olzhik");

        List<User> userList = Arrays.asList(
                user,
                user1
        );
        when(adminService.seeAllUsers()).thenReturn(userList);

        UserDto userDto = new UserDto();
        userDto.setUsername("Olzhas");
        UserDto userDto1 = new UserDto();
        userDto1.setUsername("Olzhik");

        when(userMapper.toDto(userList.get(0))).thenReturn(userDto);
        when(userMapper.toDto(userList.get(1))).thenReturn(userDto1);

        List<UserDto> userDtoList = Arrays.asList(userDto, userDto1);

        servlet.doGet(request, response);

        InOrder inOrder = inOrder(response, objectMapper);
        inOrder.verify(response).setContentType("application/json");
        inOrder.verify(response).setStatus(HttpServletResponse.SC_OK);
        inOrder.verify(objectMapper).writeValue(any(PrintWriter.class), eq(userDtoList));
    }

    @Test
    @DisplayName("Unauthorized access for non-admin user")
    public void testShowAllUsersUnauthorized() throws IOException {
        when(authentication.isAdmin()).thenReturn(false);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("ValidationParametersException handling")
    public void testShowAllUsersValidationException() throws IOException, ValidationParametersException {
        when(authentication.isAdmin()).thenReturn(true);
        when(authentication.getUsername()).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

}
