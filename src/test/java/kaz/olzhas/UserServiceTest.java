package kaz.olzhas;

import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

//Класс теста Сервиса Админа
public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService();
    }

    @Test
    public void testRegisterUser() {
        boolean result = userService.registerUser("user1", "password1");
        assertThat(result).isTrue();

        Map<String, User> users = userService.getUsers();
        assertThat(users).containsKey("user1");
    }

    @Test
    public void testAuthenticateUser() {
        userService.registerUser("user1", "password1");
        boolean result = userService.authenticateUser("user1", "password1");

        assertThat(result).isTrue();
    }

    @Test
    public void testShowAllReservations() {
        userService.registerUser("user1", "password1");
        User user = userService.getUserByUsername("user1");

        userService.showAllReservations("user1");

        assertThat(user.getWorkspaceList()).isEmpty();
    }
}