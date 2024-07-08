package kaz.olzhas.ylab.servlets.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.BookingDto;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.exception.ValidationParametersException;
import kaz.olzhas.ylab.mapper.BookingMapper;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.state.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;

public class ShowBookingsByWorkspaceServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AdminService adminService;

    @Mock
    private Authentication authentication;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private ServletConfig servletConfig;

    @Mock
    private ServletContext servletContext;

    @Mock
    private PrintWriter writer;

    @InjectMocks
    private ShowBookingsByWorkspaceServlet servlet;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("adminService")).thenReturn(adminService);
        when(servletContext.getAttribute("objectMapper")).thenReturn(objectMapper);
        when(servletContext.getAttribute("bookingMapper")).thenReturn(bookingMapper);
        when(servletContext.getAttribute("authentication")).thenReturn(authentication);

        writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        servlet = new ShowBookingsByWorkspaceServlet();
        servlet.init(servletConfig);
    }

    @Test
    @DisplayName("Successful retrieval of bookings by workspace")
    public void testShowBookingsByWorkspaceSuccess() throws IOException, ValidationParametersException {
        when(authentication.isAdmin()).thenReturn(true);

        Long workspaceId = 1L;
        when(request.getParameter("workspaceId")).thenReturn(workspaceId.toString());

        List<Booking> bookingList = Arrays.asList(
                new Booking(),
                new Booking()
        );
        when(adminService.bookingsByWorkspace(workspaceId)).thenReturn(bookingList);

        BookingDto bookingDto1 = new BookingDto();
        BookingDto bookingDto2 = new BookingDto();
        when(bookingMapper.toDto(bookingList.get(0))).thenReturn(bookingDto1);
        when(bookingMapper.toDto(bookingList.get(1))).thenReturn(bookingDto2);

        servlet.doGet(request, response);

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(objectMapper).writeValue(any(PrintWriter.class), captor.capture());

        verify(response).setStatus(HttpServletResponse.SC_OK);

    }

    @Test
    @DisplayName("Forbidden access for non-admin users")
    public void testShowBookingsByWorkspaceForbidden() throws IOException {
        when(authentication.isAdmin()).thenReturn(false);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response.getWriter()).write("Только админы могут видеть всех пользователей.");
    }

    @Test
    @DisplayName("Bad request due to ValidationParametersException")
    public void testShowBookingsByWorkspaceValidationParametersException() throws IOException, ValidationParametersException {
        when(authentication.isAdmin()).thenReturn(true);

        Long workspaceId = 1L;
        when(request.getParameter("workspaceId")).thenReturn(workspaceId.toString());

        doThrow(new ValidationParametersException("Validation error")).when(adminService).bookingsByWorkspace(workspaceId);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response.getWriter()).write("Validation error");
    }

    @Test
    @DisplayName("Internal server error due to RuntimeException")
    public void testShowBookingsByWorkspaceRuntimeException() throws IOException, ValidationParametersException {
        when(authentication.isAdmin()).thenReturn(true);

        Long workspaceId = 1L;
        when(request.getParameter("workspaceId")).thenReturn(workspaceId.toString());

        doThrow(new RuntimeException("Internal error")).when(adminService).bookingsByWorkspace(workspaceId);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(response.getWriter()).write("Internal error");
    }
}
