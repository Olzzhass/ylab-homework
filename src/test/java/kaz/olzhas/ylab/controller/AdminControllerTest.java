package kaz.olzhas.ylab.controller;

import kaz.olzhas.ylab.dto.BookingDto;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.mapper.BookingMapper;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.state.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;

@DisplayName("Test class for AdminController.class")
public class AdminControllerTest {
    private MockMvc mockMvc;
    @Mock
    private AdminService adminService;
    @Mock
    private Authentication authentication;
    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    @DisplayName("Method of testing show all users functionality")
    public void testShowAllUsers() throws Exception {
        List<User> users = Arrays.asList(new User(), new User());
        when(adminService.showAllUsers()).thenReturn(users);

        mockMvc.perform(get("/admin/all-users").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Method of testing show all workspaces functionality")
    public void testShowAllWorkspaces() throws Exception {
        List<Workspace> workspaces = Arrays.asList(new Workspace(), new Workspace());
        when(adminService.showAllWorkspaces()).thenReturn(workspaces);

        mockMvc.perform(get("/admin/all-workspaces")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Method of testing add workspace functionality")
    public void testAddWorkspace() throws Exception {
        when(authentication.isAdmin()).thenReturn(true);
        when(adminService.addWorkspace("Workspace-Test")).thenReturn(true);

        mockMvc.perform(post("/admin/add-workspace")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"workspaceName\": \"Workspace-Test\"}"))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Method of testing show bookings by user functionality")
    public void testShowBookingsByUser() throws Exception {
        when(authentication.isAdmin()).thenReturn(true);

        List<Booking> bookings = Arrays.asList(new Booking(), new Booking());
        when(adminService.bookingsByUser("Test")).thenReturn(bookings);

        BookingDto bookingDto = new BookingDto();
        when(bookingMapper.toDto(bookings.get(0))).thenReturn(bookingDto);
        when(bookingMapper.toDto(bookings.get(1))).thenReturn(bookingDto);

        mockMvc.perform(get("/admin/bookings-by-user")
                        .param("username", "Test"))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Method of testing show bookings by workspace functionality")
    public void testShowBookingsByWorkspace() throws Exception {
        when(authentication.isAdmin()).thenReturn(true);

        List<Booking> bookings = Arrays.asList(new Booking(), new Booking());
        when(adminService.bookingsByWorkspace(1L)).thenReturn(bookings);

        BookingDto bookingDto = new BookingDto();
        when(bookingMapper.toDto(bookings.get(0))).thenReturn(bookingDto);
        when(bookingMapper.toDto(bookings.get(1))).thenReturn(bookingDto);

        mockMvc.perform(get("/admin/bookings-by-workspace")
                        .param("workspaceId", "1"))
                        .andExpect(status().isOk());
    }
}
