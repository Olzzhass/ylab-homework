package kaz.olzhas.ylab.servlets.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.ExceptionResponse;
import kaz.olzhas.ylab.dto.ShowSlotsRequestDto;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ShowSlotsByDateServletTest {

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

    @InjectMocks
    private ShowSlotsByDateServlet servlet;

    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("workspaceService")).thenReturn(workspaceService);
        when(servletContext.getAttribute("objectMapper")).thenReturn(objectMapper);
        when(servletContext.getAttribute("authentication")).thenReturn(authentication);

        writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        servlet = new ShowSlotsByDateServlet();
        servlet.init(servletConfig);
    }

    @Test
    @DisplayName("Successful retrieval of available slots")
    public void testShowSlotsByDateSuccess() throws Exception {
        when(authentication.isAuth()).thenReturn(true);

        ShowSlotsRequestDto requestDto = new ShowSlotsRequestDto();
        requestDto.setWorkspaceId(1L);
        requestDto.setDate("2023-07-10");

        LocalDateTime date = LocalDateTime.parse("2023-07-10T00:00:00");
        List<LocalDateTime> availableSlots = Arrays.asList(
                LocalDateTime.parse("2023-07-10T10:00:00"),
                LocalDateTime.parse("2023-07-10T12:00:00")
        );

        BufferedReader reader = new BufferedReader(new StringReader(""));
        when(request.getReader()).thenReturn(reader);

        when(objectMapper.readValue(reader, ShowSlotsRequestDto.class)).thenReturn(requestDto);
        when(workspaceService.getAvailableSlots(1L, date)).thenReturn(availableSlots);

        servlet.doGet(request, response);

        InOrder inOrder = inOrder(response, objectMapper);
        inOrder.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Forbidden access for unauthenticated user")
    public void testShowSlotsByDateForbiddenAccess() throws Exception {
        when(authentication.isAuth()).thenReturn(false);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response.getWriter()).write("Сначала нужно войти в приложение!");
    }

    @Test
    @DisplayName("Internal server error on exception")
    public void testShowSlotsByDateInternalServerError() throws Exception {
        when(authentication.isAuth()).thenReturn(true);

        ShowSlotsRequestDto requestDto = new ShowSlotsRequestDto();
        requestDto.setWorkspaceId(1L);
        requestDto.setDate("2023-07-01");

        LocalDateTime date = LocalDateTime.parse("2023-07-01T00:00:00");

        when(objectMapper.readValue(any(BufferedReader.class), eq(ShowSlotsRequestDto.class))).thenReturn(requestDto);
        when(workspaceService.getAvailableSlots(1L, date)).thenThrow(new RuntimeException("Internal error"));

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(objectMapper).writeValue(writer, new ExceptionResponse("An error occurred while retrieving available slots"));
    }
}
