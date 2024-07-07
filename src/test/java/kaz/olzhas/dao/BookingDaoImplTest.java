package kaz.olzhas.dao;

import kaz.olzhas.containers.PostgresTestContainer;
import kaz.olzhas.ylab.dao.WorkspaceDao;
import kaz.olzhas.ylab.dao.implementations.BookingDaoImpl;
import kaz.olzhas.ylab.dao.implementations.UserDaoImpl;
import kaz.olzhas.ylab.dao.implementations.WorkspaceDaoImpl;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.liquibase.LiquibaseDemo;
import kaz.olzhas.ylab.util.ConnectionManager;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("user dao implementation test")
public class BookingDaoImplTest extends PostgresTestContainer {

    private BookingDaoImpl bookingDao;
    private WorkspaceDaoImpl workspaceDao;
    private UserDaoImpl userDao;

    private ConnectionManager connectionManager = new ConnectionManager(
            container.getJdbcUrl(), container.getUsername(), container.getPassword(),
            "org.postgresql.Driver");

    @BeforeEach
    public void setUp() {

        LiquibaseDemo liquibaseTest = new LiquibaseDemo(connectionManager.getConnection(), "db/main-changelog.xml", "liquibase");
        liquibaseTest.runMigrations();

        bookingDao = new BookingDaoImpl(connectionManager);
        workspaceDao = new WorkspaceDaoImpl(connectionManager);
        userDao = new UserDaoImpl(connectionManager);

    }

    @Test
    @DisplayName("Booking save method verification test")
    public void saveTest(){

        bookingDao.deleteAll();

        Workspace workspace = new Workspace();
        workspace.setName("Workspace-Test");

        var save = workspaceDao.save(workspace);

        String startDateTime = "2024-07-03T16:00";
        String endDateTime = "2024-07-03T17:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime start = LocalDateTime.parse(startDateTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endDateTime, formatter);

        var save1 = bookingDao.save(1L, 1L, start, end);
        List<Booking> bookings = bookingDao.getByUserId(1L);

        assertAll(
                () -> assertThat(save).isEqualTo(true),
                () -> assertThat(save1).isEqualTo(true),
                () -> assertThat(bookings).hasSize(1),
                () -> assertThat(bookings.get(0).getStart()).isEqualTo(start),
                () -> assertThat(bookings.get(0).getEnd()).isEqualTo(end)
        );
    }


    @Test
    @DisplayName("Booking get by user Id test")
    public void getByUserIdTest(){

        bookingDao.deleteAll();

        Workspace workspace = new Workspace();
        workspace.setName("Workspace-Test2");

        User user = new User();
        user.setUsername("Test");
        user.setPassword("test");

        userDao.save(user);

        var save = workspaceDao.save(workspace);

        String startDateTime = "2024-07-03T18:00";
        String endDateTime = "2024-07-03T19:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime start = LocalDateTime.parse(startDateTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endDateTime, formatter);

        var save1 = bookingDao.save(2L, 1L, start, end);

        List<Booking> bookings = bookingDao.getByUserId(2L);

        assertAll(
                () -> assertThat(save).isEqualTo(true),
                () -> assertThat(save1).isEqualTo(true),
                () -> assertThat(bookings).hasSize(1),
                () -> assertThat(bookings.get(0).getUserId()).isEqualTo(2)
        );

    }

    @Test
    @DisplayName("Booking delete by id test")
    public void deleteByIdTest(){

        bookingDao.deleteAll();

        Workspace workspace = new Workspace();
        workspace.setName("Workspace-Test2");

        User user = new User();
        user.setUsername("Test");
        user.setPassword("test");

        userDao.save(user);

        var save = workspaceDao.save(workspace);

        String startDateTime = "2024-07-03T18:00";
        String endDateTime = "2024-07-03T19:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime start = LocalDateTime.parse(startDateTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endDateTime, formatter);

        var save1 = bookingDao.save(2L, 1L, start, end);

        var is = bookingDao.deleteById(2L);


        assertAll(
                () -> assertThat(save).isEqualTo(true),
                () -> assertThat(save1).isEqualTo(true)
        );
    }

    @Test
    @DisplayName("Booking get by workspace id test")
    public void getByWorkspaceIdTest(){

        bookingDao.deleteAll();

        Workspace workspace = new Workspace();
        workspace.setName("Workspace-Test2");

        User user = new User();
        user.setUsername("Test");
        user.setPassword("test");

        userDao.save(user);

        var save = workspaceDao.save(workspace);

        String startDateTime = "2024-07-03T18:00";
        String endDateTime = "2024-07-03T19:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime start = LocalDateTime.parse(startDateTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endDateTime, formatter);

        var save1 = bookingDao.save(2L, 1L, start, end);

        List<Booking> bookings = bookingDao.getByWorkspaceId(1L);

        assertAll(
                () -> assertThat(save).isEqualTo(true),
                () -> assertThat(save1).isEqualTo(true),
                () -> assertThat(bookings).hasSize(1),
                () -> assertThat(bookings.get(0).getWorkspaceId()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("Booking get bookings for workspace test")
    public void getBookingsForWorkspaceTest(){

        bookingDao.deleteAll();

        Workspace workspace = new Workspace();
        workspace.setName("Workspace-Test2");

        User user = new User();
        user.setUsername("Test");
        user.setPassword("test");

        userDao.save(user);

        var save = workspaceDao.save(workspace);

        String startDateTime = "2024-07-03T18:00";
        String endDateTime = "2024-07-03T19:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime start = LocalDateTime.parse(startDateTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endDateTime, formatter);

        var save1 = bookingDao.save(2L, 1L, start, end);

        List<Booking> bookings = bookingDao.getBookingsForWorkspace(1L, start, end);

        assertAll(
                () -> assertThat(save).isEqualTo(true),
                () -> assertThat(save1).isEqualTo(true),
                () -> assertThat(bookings).hasSize(1),
                () -> assertThat(bookings.get(0).getWorkspaceId()).isEqualTo(1)
        );
    }
}
