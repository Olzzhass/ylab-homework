package kaz.olzhas.ylab.controller;

import kaz.olzhas.ylab.aspects.AuthAspect;
import kaz.olzhas.ylab.dto.BookingDto;
import kaz.olzhas.ylab.dto.ShowSlotsRequestDto;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.mapper.BookingMapper;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.service.WorkspaceService;
import kaz.olzhas.ylab.state.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@DisplayName("Test class for UserController.class")
public class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private Authentication authentication;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("Method of testing booking success functionality")
    public void testBookWorkspaceSuccess() throws Exception {
        when(authentication.isAuth()).thenReturn(true);
        when(authentication.getUsername()).thenReturn("testUser");
        BookingDto bookingDto = new BookingDto();
        bookingDto.setWorkspaceId(1L);
        bookingDto.setStart(LocalDateTime.of(2024, 7, 12, 10, 0));
        bookingDto.setEnd(LocalDateTime.of(2024, 7, 12, 12, 0));
        when(workspaceService.bookWorkspace(bookingDto.getWorkspaceId(), bookingDto.getStart(), bookingDto.getEnd(), "testUser")).thenReturn(true);

        mockMvc.perform(post("/user/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"workspaceId\": 1, \"start\": \"2024-07-12T10:00:00\", \"end\": \"2024-07-12T12:00:00\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Method of testing booking failure functionality")
    public void testBookWorkspaceFailure() throws Exception {
        when(authentication.isAuth()).thenReturn(true);
        when(authentication.getUsername()).thenReturn("testUser");
        BookingDto bookingDto = new BookingDto();
        bookingDto.setWorkspaceId(1L);
        bookingDto.setStart(LocalDateTime.of(2024, 7, 12, 10, 0));
        bookingDto.setEnd(LocalDateTime.of(2024, 7, 12, 12, 0));
        when(workspaceService.bookWorkspace(bookingDto.getWorkspaceId(), bookingDto.getStart(), bookingDto.getEnd(), "testUser")).thenReturn(false);

        mockMvc.perform(post("/user/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"workspaceId\": 1, \"start\": \"2024-07-12T10:00:00\", \"end\": \"2024-07-12T12:00:00\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Method of testing delete booking success functionality")
    public void testDeleteBookingSuccess() throws Exception {
        when(authentication.isAuth()).thenReturn(true);
        when(workspaceService.deleteReservation(1L)).thenReturn(true);

        mockMvc.perform(post("/user/deleteBooking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookingId\": 1}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Method of testing delete booking failure functionality")
    public void testDeleteBookingFailure() throws Exception {
        when(authentication.isAuth()).thenReturn(true);
        when(workspaceService.deleteReservation(1L)).thenReturn(false);

        mockMvc.perform(post("/user/deleteBooking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookingId\": 1}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Method of testing show slots by date functionality")
    public void testShowSlotsByDate() throws Exception {
        ShowSlotsRequestDto requestDto = new ShowSlotsRequestDto();
        requestDto.setWorkspaceId(1L);
        requestDto.setDate("2024-07-12");
        LocalDateTime date = LocalDateTime.of(2024, 7, 12, 0, 0);
        List<LocalDateTime> slots = Arrays.asList(LocalDateTime.of(2024, 7, 12, 10, 0), LocalDateTime.of(2024, 7, 12, 14, 0));

        when(workspaceService.getAvailableSlots(1L, date)).thenReturn(slots);

        mockMvc.perform(get("/user/showSlots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"workspaceId\": 1, \"date\": \"2024-07-12\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Method of testing show user workspaces functionality")
    public void testShowUserWorkspaces() throws Exception {
        when(authentication.getUsername()).thenReturn("testUser");
        List<Booking> bookings = Arrays.asList(new Booking(), new Booking());
        when(userService.showAllReservations("testUser")).thenReturn(bookings);

        BookingDto bookingDto = new BookingDto();
        when(bookingMapper.toDto(bookings.get(0))).thenReturn(bookingDto);
        when(bookingMapper.toDto(bookings.get(1))).thenReturn(bookingDto);

        mockMvc.perform(get("/user/my-workspaces").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
