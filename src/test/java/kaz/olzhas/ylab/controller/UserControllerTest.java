package kaz.olzhas.ylab.controller;

import kaz.olzhas.ylab.dto.BookingDto;
import kaz.olzhas.ylab.dto.DeleteBookingDto;
import kaz.olzhas.ylab.dto.ShowSlotsRequestDto;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.mapper.BookingMapper;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.service.WorkspaceService;
import kaz.olzhas.ylab.state.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private Authentication authentication;

    @Mock
    private BookingMapper bookingMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        try (AutoCloseable closeable = MockitoAnnotations.openMocks(this)) {
            mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        }
    }

    @Test
    public void testBookWorkspaceSuccess() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setWorkspaceId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusHours(1));
        when(authentication.getUsername()).thenReturn("user");
        when(workspaceService.bookWorkspace(bookingDto.getWorkspaceId(), bookingDto.getStart(), bookingDto.getEnd(), "user"))
                .thenReturn(true);

        mockMvc.perform(post("/user/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"workspaceId\":\"" + bookingDto.getWorkspaceId() + "\",\"start\":\"" + bookingDto.getStart() + "\",\"end\":\"" + bookingDto.getEnd() + "\"}"))
                        .andExpect(status().isOk());
    }

    @Test
    public void testBookWorkspaceFailure() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setWorkspaceId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusHours(1));
        when(authentication.getUsername()).thenReturn("user");
        when(workspaceService.bookWorkspace(bookingDto.getWorkspaceId(), bookingDto.getStart(), bookingDto.getEnd(), "user"))
                .thenReturn(false);

        mockMvc.perform(post("/user/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"workspaceId\":1,\"start\":\"2024-07-14T10:00:00\",\"end\":\"2024-07-14T11:00:00\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testDeleteBookingSuccess() throws Exception {
        DeleteBookingDto deleteBookingDto = new DeleteBookingDto();
        deleteBookingDto.setBookingId(1L);
        when(workspaceService.deleteReservation(deleteBookingDto.getBookingId())).thenReturn(true);

        mockMvc.perform(post("/user/deleteBooking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookingId\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteBookingFailure() throws Exception {
        DeleteBookingDto deleteBookingDto = new DeleteBookingDto();
        deleteBookingDto.setBookingId(1L);
        when(workspaceService.deleteReservation(deleteBookingDto.getBookingId())).thenReturn(false);

        mockMvc.perform(post("/user/deleteBooking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookingId\":1}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testShowSlotsByDateSuccess() throws Exception {
        ShowSlotsRequestDto requestDTO = new ShowSlotsRequestDto();
        requestDTO.setWorkspaceId(1L);
        requestDTO.setDate("2024-07-14");
        LocalDateTime date = LocalDateTime.parse(requestDTO.getDate() + "T00:00:00");
        List<LocalDateTime> slots = List.of(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        when(workspaceService.getAvailableSlots(requestDTO.getWorkspaceId(), date)).thenReturn(slots);

        mockMvc.perform(get("/user/showSlots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"workspaceId\":1,\"date\":\"2024-07-14\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testShowUserWorkspacesSuccess() throws Exception {
        when(authentication.getUsername()).thenReturn("user");
        List<Booking> bookings = List.of(new Booking(), new Booking());
        when(userService.showAllReservations("user")).thenReturn(bookings);
        when(bookingMapper.toDto(bookings.get(0))).thenReturn(new BookingDto());
        when(bookingMapper.toDto(bookings.get(1))).thenReturn(new BookingDto());

        mockMvc.perform(get("/user/my-workspaces")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}