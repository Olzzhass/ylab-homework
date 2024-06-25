package kaz.olzhas;

import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.service.WorkspaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

//Класс теста Сервиса Админа
public class WorkspaceServiceTest {

    private WorkspaceService workspaceService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        workspaceService = new WorkspaceService(userService);
    }

    @Test
    public void testShowAllAvailableWorkspaces() {
        Workspace workspace = new Workspace(1, "Workspace1");
        workspaceService.setWorkspaceList(Arrays.asList(workspace));

        workspaceService.showAllAvailableWorkspaces();

        assertThat(workspaceService.getWorkspaceList()).hasSize(1);
    }

    @Test
    public void testBookWorkspace() {
        Workspace workspace = new Workspace(1, "Workspace1");
        workspaceService.setWorkspaceList(Arrays.asList(workspace));

        User user = new User("user1", "password1");
        when(userService.getUserByUsername("user1")).thenReturn(user);

        boolean result = workspaceService.bookWorkspace(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "user1");

        assertThat(result).isTrue();
        verify(userService, times(1)).getUserByUsername("user1");
    }

    @Test
    public void testDeleteReservation() {
        Workspace workspace = new Workspace(1, "Workspace1");
        Booking booking = new Booking(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        workspace.addBooking(booking);
        workspaceService.setWorkspaceList(List.of(workspace));

        User user = new User("user1", "password1");
        user.getWorkspaceList().add(workspace);
        when(userService.getUserByUsername("user1")).thenReturn(user);

        workspaceService.deleteReservation(1, 1, "user1");

        assertThat(workspace.getBookings()).isEmpty();
        verify(userService, times(1)).getUserByUsername("user1");
    }
}

