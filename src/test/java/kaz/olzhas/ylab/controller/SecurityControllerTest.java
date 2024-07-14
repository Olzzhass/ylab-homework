package kaz.olzhas.ylab.controller;

import kaz.olzhas.ylab.dto.UserRequest;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.state.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityControllerTest {

    @InjectMocks
    private SecurityController securityController;

    @Mock
    private AdminService adminService;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        try (AutoCloseable closeable = MockitoAnnotations.openMocks(this)) {
            mockMvc = MockMvcBuilders.standaloneSetup(securityController).build();
        }
    }

    @Test
    public void testAuthorizationAdminSuccess() throws Exception {
        UserRequest userRequest = new UserRequest("admin", "password");
        when(adminService.authenticateAdmin(userRequest.username(), userRequest.password())).thenReturn(true);

        mockMvc.perform(post("/main/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + userRequest.username() + "\", \"password\":\"" + userRequest.password() + "\"}"))
                        .andExpect(status().isOk());
    }

    @Test
    public void testAuthorizationUserSuccess() throws Exception {
        UserRequest userRequest = new UserRequest("user", "password");
        when(userService.authenticateUser(userRequest.username(), userRequest.password())).thenReturn(true);

        mockMvc.perform(post("/main/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + userRequest.username() + "\", \"password\":\"" + userRequest.password() + "\"}"))
                        .andExpect(status().isOk());
    }

    @Test
    public void testAuthorizationFailure() throws Exception {
        UserRequest userRequest = new UserRequest("user", "wrongPassword");
        when(userService.authenticateUser(userRequest.username(), userRequest.password())).thenReturn(false);

        mockMvc.perform(post("/main/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + userRequest.username() + "\", \"password\":\"" + userRequest.password() + "\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testRegistrationSuccess() throws Exception {
        UserRequest userRequest = new UserRequest("newUser", "password");
        when(userService.registerUser(userRequest.username(), userRequest.password())).thenReturn(true);

        mockMvc.perform(post("/main/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + userRequest.username() + "\", \"password\":\"" + userRequest.password() + "\"}"))
                        .andExpect(status().isOk());
    }

    @Test
    public void testRegistrationFailure() throws Exception {
        UserRequest userRequest = new UserRequest("newUser", "password");
        when(userService.registerUser(userRequest.username(), userRequest.password())).thenReturn(false);

        mockMvc.perform(post("/main/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + userRequest.username() + "\", \"password\":\"" + userRequest.password() + "\"}"))
                .andExpect(status().isInternalServerError());
    }
}