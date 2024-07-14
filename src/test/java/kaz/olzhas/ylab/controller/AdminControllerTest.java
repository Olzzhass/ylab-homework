package kaz.olzhas.ylab.controller;

import kaz.olzhas.ylab.dto.BookingDto;
import kaz.olzhas.ylab.dto.WorkspaceRequest;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.mapper.BookingMapper;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.state.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;

    @Mock
    private Authentication authentication;

    @Mock
    private BookingMapper bookingMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        try (AutoCloseable closeable = MockitoAnnotations.openMocks(this)) {
            mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
        }
    }

    @Test
    public void testShowAllUsers() throws Exception {
        List<User> users = Collections.singletonList(new User());
        when(adminService.showAllUsers()).thenReturn(users);

        mockMvc.perform(get("/admin/all-users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testShowAllWorkspaces() throws Exception {
        List<Workspace> workspaces = Collections.singletonList(new Workspace());
        when(adminService.showAllWorkspaces()).thenReturn(workspaces);

        mockMvc.perform(get("/admin/all-workspaces")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddWorkspace() throws Exception {
        WorkspaceRequest request = new WorkspaceRequest("Test Workspace");
        when(adminService.addWorkspace(request.workspaceName())).thenReturn(true);

        mockMvc.perform(post("/admin/add-workspace")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"workspaceName\":\"" + request.workspaceName() + "\"}"))
                        .andExpect(status().isOk());
    }

    @Test
    public void testShowBookingsByUser() throws Exception {
        String username = "testUser";
        List<Booking> bookings = Collections.singletonList(new Booking());
        List<BookingDto> bookingDtos = Collections.singletonList(new BookingDto());
        when(adminService.bookingsByUser(username)).thenReturn(bookings);
        when(bookingMapper.toDto(bookings.get(0))).thenReturn(bookingDtos.get(0));

        mockMvc.perform(get("/admin/bookings-by-user")
                        .param("username", username)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    @Test
    public void testShowBookingsByWorkspace() throws Exception {
        Long workspaceId = 1L;
        List<Booking> bookings = Collections.singletonList(new Booking());
        List<BookingDto> bookingDtos = Collections.singletonList(new BookingDto());
        when(adminService.bookingsByWorkspace(workspaceId)).thenReturn(bookings);
        when(bookingMapper.toDto(bookings.get(0))).thenReturn(bookingDtos.get(0));

        mockMvc.perform(get("/admin/bookings-by-workspace")
                        .param("workspaceId", String.valueOf(workspaceId))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }
}
