package kaz.olzhas;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.service.WorkspaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Класс теста Сервиса Админа
@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    public void setUp() {
        adminService = new AdminService(workspaceService, userService);
    }

    @Test
    public void testAddWorkspace() {
        List<Workspace> workspaces = new ArrayList<>();
        when(workspaceService.getWorkspaceList()).thenReturn(workspaces);

        adminService.addWorkspace(1, "Workspace1");

        verify(workspaceService).setWorkspaceList(workspaces);
        assertThat(workspaces).hasSize(1);
        assertThat(workspaces.get(0).getName()).isEqualTo("Workspace1");
    }

    @Test
    public void testSeeAllWorkspaces() {
        List<Workspace> workspaces = new ArrayList<>();
        workspaces.add(new Workspace(1, "Workspace1"));
        when(workspaceService.getWorkspaceList()).thenReturn(workspaces);

        adminService.seeAllWorkspaces();

        verify(workspaceService).getWorkspaceList();
    }

    @Test
    public void testSeeAllUsers() {
        Map<String, User> users = new HashMap<>();
        users.put("user1", new User("user1", "password1"));
        when(userService.getUsers()).thenReturn(users);

        adminService.seeAllUsers();

        verify(userService).getUsers();
    }

    @Test
    public void testSeeAllUsersWithoutPassword() {
        Map<String, User> users = new HashMap<>();
        users.put("user1", new User("user1", "password1"));
        when(userService.getUsers()).thenReturn(users);

        adminService.seeAllUsersWithoutPassword();

        verify(userService).getUsers();
    }

    @Test
    public void testBookingsByDate() {
        List<Booking> bookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        bookings.add(new Booking(now.minusDays(1), now.plusDays(1)));
        when(workspaceService.getAllBookings()).thenReturn(bookings);

        List<Booking> filteredBookings = adminService.bookingsByDate(now.minusDays(2), now.plusDays(2));

        assertThat(filteredBookings).hasSize(1);
        verify(workspaceService).getAllBookings();
    }

    @Test
    public void testBookingsByWorkspace() {
        List<Workspace> workspaces = new ArrayList<>();
        Workspace workspace = new Workspace(1, "Workspace1");
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)));
        workspace.setBookings(bookings);
        workspaces.add(workspace);
        when(workspaceService.getWorkspaceList()).thenReturn(workspaces);

        List<Booking> filteredBookings = adminService.bookingsByWorkspace(1);

        assertThat(filteredBookings).hasSize(1);
        verify(workspaceService).getWorkspaceList();
    }

    @Test
    public void testBookingsByUser() {
        User user = new User("user1", "password1");
        Workspace workspace = new Workspace(1, "Workspace1");
        List<Workspace> workspaces = new ArrayList<>();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)));
        workspace.setBookings(bookings);
        workspaces.add(workspace);
        user.setWorkspaceList(workspaces);
        Map<String, User> users = new HashMap<>();
        users.put("user1", user);
        when(userService.getUsers()).thenReturn(users);

        adminService.bookingsByUser("user1");

        verify(userService).getUsers();
    }
}


