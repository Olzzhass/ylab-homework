package kaz.olzhas.ylab.servlets.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.DeleteBookingDto;
import kaz.olzhas.ylab.dto.ExceptionResponse;
import kaz.olzhas.ylab.dto.ResponseMessage;
import kaz.olzhas.ylab.service.WorkspaceService;
import kaz.olzhas.ylab.state.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import static org.mockito.Mockito.*;

public class DeleteUserBookingServletTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private WorkspaceService workspaceService;

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
    private DeleteUserBookingServlet servlet;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("workspaceService")).thenReturn(workspaceService);
        when(servletContext.getAttribute("objectMapper")).thenReturn(objectMapper);
        when(servletContext.getAttribute("authentication")).thenReturn(authentication);

        when(response.getWriter()).thenReturn(writer);

        servlet = new DeleteUserBookingServlet();
        servlet.init(servletConfig);
    }

    @Test
    @DisplayName("Successful booking deletion")
    public void testDeleteBooking() throws Exception {
        when(authentication.isAuth()).thenReturn(true);
        DeleteBookingDto deleteBookingDto = new DeleteBookingDto();
        deleteBookingDto.setBookingId(1L);

        String jsonRequest = "{\"bookingId\":1}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));
        when(request.getReader()).thenReturn(reader);

        when(objectMapper.readValue(reader, DeleteBookingDto.class)).thenReturn(deleteBookingDto);
        when(workspaceService.deleteReservation(1L)).thenReturn(true);

        servlet.doDelete(request, response);

        InOrder inOrder = inOrder(response, objectMapper);
        inOrder.verify(response).setContentType("application/json");
        inOrder.verify(response).setStatus(HttpServletResponse.SC_OK);
        inOrder.verify(objectMapper).writeValue(writer, new ResponseMessage("Бронирование места успешно удалено!"));
    }

    @Test
    @DisplayName("Forbidden access for unauthenticated user")
    public void testDeleteBookingForbiddenAccess() throws Exception {
        when(authentication.isAuth()).thenReturn(false);

        servlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response.getWriter()).write("Сначало нужно войти в приложение!");
    }

    @Test
    @DisplayName("Internal server error on runtime exception")
    public void testDeleteBookingInternalServerError() throws Exception {
        when(authentication.isAuth()).thenReturn(true);
        DeleteBookingDto deleteBookingDto = new DeleteBookingDto();
        deleteBookingDto.setBookingId(1L);

        String jsonRequest = "{\"bookingId\":1}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));
        when(request.getReader()).thenReturn(reader);

        when(objectMapper.readValue(reader, DeleteBookingDto.class)).thenReturn(deleteBookingDto);
        doThrow(new RuntimeException("Internal error")).when(workspaceService).deleteReservation(1L);

        servlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(objectMapper).writeValue(writer, new ExceptionResponse("Internal error"));
    }
}
