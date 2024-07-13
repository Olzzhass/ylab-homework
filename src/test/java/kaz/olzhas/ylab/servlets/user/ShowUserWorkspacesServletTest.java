package kaz.olzhas.ylab.servlets.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.BookingDto;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.mapper.BookingMapper;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.state.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;

public class ShowUserWorkspacesServletTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

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
    private BookingMapper bookingMapper;

    @InjectMocks
    private ShowUserWorkspacesServlet servlet;

    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("objectMapper")).thenReturn(objectMapper);
        when(servletContext.getAttribute("userService")).thenReturn(userService);
        when(servletContext.getAttribute("bookingMapper")).thenReturn(bookingMapper);
        when(servletContext.getAttribute("authentication")).thenReturn(authentication);

        writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        servlet = new ShowUserWorkspacesServlet();
        servlet.init(servletConfig);
    }

    @Test
    @DisplayName("Successful retrieval of user workspaces")
    public void testShowUserWorkspacesSuccess() throws Exception {
        when(authentication.isAuth()).thenReturn(true);
        when(authentication.isAdmin()).thenReturn(false);
        when(authentication.getUsername()).thenReturn("user1");

        List<Booking> bookings = Arrays.asList(
                new Booking(),
                new Booking()
        );

        when(userService.showAllReservations("user1")).thenReturn(bookings);

        BookingDto bookingDto1 = new BookingDto();
        BookingDto bookingDto2 = new BookingDto();
        when(bookingMapper.toDto(bookings.get(0))).thenReturn(bookingDto1);
        when(bookingMapper.toDto(bookings.get(1))).thenReturn(bookingDto2);

        servlet.doGet(request, response);

        InOrder inOrder = inOrder(response, objectMapper);
        inOrder.verify(response).setStatus(HttpServletResponse.SC_OK);
        inOrder.verify(objectMapper).writeValue(writer, Arrays.asList(bookingDto1, bookingDto2));
    }

    @Test
    @DisplayName("Forbidden access for non-authenticated user")
    public void testShowUserWorkspacesForbiddenAccess() throws Exception {
        when(authentication.isAuth()).thenReturn(false);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response.getWriter()).write("Страница только для пользователей!");
    }

    @Test
    @DisplayName("Validation error on empty username")
    public void testShowUserWorkspacesValidationError() throws Exception {
        when(authentication.isAuth()).thenReturn(true);
        when(authentication.getUsername()).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response.getWriter()).write("Имя пользователя пустое!");
    }

    @Test
    @DisplayName("Internal server error on runtime exception")
    public void testShowUserWorkspacesInternalServerError() throws Exception {
        when(authentication.isAuth()).thenReturn(true);
        when(authentication.isAdmin()).thenReturn(false);
        when(authentication.getUsername()).thenReturn("user1");

        when(userService.showAllReservations("user1")).thenThrow(new RuntimeException("Internal error"));

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(response.getWriter()).write("Internal error");
    }
}
