package kaz.olzhas.ylab.servlets.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaz.olzhas.ylab.dto.ExceptionResponse;
import kaz.olzhas.ylab.dto.ShowSlotsRequestDto;
import kaz.olzhas.ylab.service.WorkspaceService;
import kaz.olzhas.ylab.state.Authentication;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/user/showSlots")
public class ShowSlotsByDateServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private WorkspaceService workspaceService;

    @Override
    public void init() throws ServletException {
        super.init();
        workspaceService = (WorkspaceService) getServletContext().getAttribute("workspaceService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");

        try {

            if(authentication.isAuth()) {

                ShowSlotsRequestDto requestDTO = objectMapper.readValue(req.getReader(), ShowSlotsRequestDto.class);
                LocalDateTime date = LocalDateTime.parse(requestDTO.getDate() + "T00:00:00");

                List<LocalDateTime> availableSlots = workspaceService.getAvailableSlots(requestDTO.getWorkspaceId(), date);

                List<String> formattedSlots = availableSlots.stream()
                        .map(dateTime -> dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .toList();

                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getWriter(), availableSlots);
            }else{
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("Сначала нужно войти в приложение!");
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Invalid workspace ID format"));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("An error occurred while retrieving available slots"));
        }
    }
}
