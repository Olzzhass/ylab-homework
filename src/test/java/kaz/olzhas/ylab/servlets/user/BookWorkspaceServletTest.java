package kaz.olzhas.ylab.servlets.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.BookingDto;
import kaz.olzhas.ylab.dto.ExceptionResponse;
import kaz.olzhas.ylab.dto.ResponseMessage;
import kaz.olzhas.ylab.exception.AuthorizeException;
import kaz.olzhas.ylab.exception.NotValidArgumentException;
import kaz.olzhas.ylab.service.WorkspaceService;
import kaz.olzhas.ylab.state.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.print.Book;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookWorkspaceServletTest {

    @Mock
    private WorkspaceService workspaceService;

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
    private PrintWriter writer;

    @InjectMocks
    private BookWorkspaceServlet servlet;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("workspaceService")).thenReturn(workspaceService);
        when(servletContext.getAttribute("objectMapper")).thenReturn(objectMapper);
        when(servletContext.getAttribute("authentication")).thenReturn(authentication);

        when(response.getWriter()).thenReturn(writer);

        servlet = new BookWorkspaceServlet();
        servlet.init(servletConfig);
    }

    @Test
    @DisplayName("Successful booking")
    public void testBookWorkspaceSuccess() throws IOException, NotValidArgumentException, AuthorizeException {
        when(authentication.isAuth()).thenReturn(true);
        when(authentication.getUsername()).thenReturn("user");

        BookingDto bookingDto = new BookingDto();
        bookingDto.setWorkspaceId(1L);
        bookingDto.setStart(LocalDateTime.parse("2023-07-01T10:00:00"));
        bookingDto.setEnd(LocalDateTime.parse("2023-07-01T12:00:00"));

        String jsonRequest = "{\"workspaceId\":1,\"start\":\"2023-07-01T10:00:00\",\"end\":\"2023-07-01T12:00:00\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));
        when(request.getReader()).thenReturn(reader);

        when(objectMapper.readValue(reader, BookingDto.class)).thenReturn(bookingDto);

        when(workspaceService.bookWorkspace(1L, LocalDateTime.parse("2023-07-01T10:00:00"), LocalDateTime.parse("2023-07-01T12:00:00"), "user")).thenReturn(true);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(objectMapper).writeValue(writer, new ResponseMessage("Рабочее место успешно забронировано!"));
    }

    @Test
    @DisplayName("Forbidden access for unauthorized user")
    public void testBookWorkspaceForbiddenAccess() throws IOException {
        when(authentication.isAuth()).thenReturn(false);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response.getWriter()).write("Сначало нужно войти в приложение!");
    }

    @Test
    @DisplayName("Bad request on validation error")
    public void testBookWorkspaceValidationError() throws IOException, NotValidArgumentException, AuthorizeException {
        when(authentication.isAuth()).thenReturn(true);
        when(authentication.getUsername()).thenReturn("user");

        BookingDto bookingDto = new BookingDto();
        bookingDto.setWorkspaceId(1L);
        bookingDto.setStart(LocalDateTime.parse("2024-07-10T16:00:00"));
        bookingDto.setEnd(LocalDateTime.parse("2024-07-10T17:00:00"));

        String jsonRequest = "{\"workspaceId\":1,\"start\":\"2024-07-10T16:00:00\",\"end\":\"2024-07-10T17:00:00\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonRequest));
        when(request.getReader()).thenReturn(reader);

        when(objectMapper.readValue(reader, BookingDto.class)).thenReturn(bookingDto);

        doThrow(new NotValidArgumentException("Validation error"))
                .when(workspaceService)
                .bookWorkspace(eq(1L), eq(LocalDateTime.parse("2024-07-10T16:00:00")), eq(LocalDateTime.parse("2024-07-10T17:00:00")), eq("user"));

        servlet.doPost(request, response);

        InOrder inOrder = inOrder(response, objectMapper);
        inOrder.verify(response).setContentType("application/json");
        inOrder.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        inOrder.verify(objectMapper).writeValue(writer, new ExceptionResponse("Validation error"));
    }
}

