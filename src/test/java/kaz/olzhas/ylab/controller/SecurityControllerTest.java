package kaz.olzhas.ylab.controller;

import kaz.olzhas.ylab.dto.UserRequest;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@DisplayName("Test class for SecurityController.class")
public class SecurityControllerTest {
    private MockMvc mockMvc;
    @Mock
    private AdminService adminService;
    @Mock
    private UserService userService;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private SecurityController securityController;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(securityController).build();
    }

    @Test
    @DisplayName("Method of testing sign in admin functionality")
    public void testSignInAdmin() throws Exception {
        UserRequest userRequest = new UserRequest("admin", "password");

        when(adminService.authenticateAdmin(userRequest.username(), userRequest.password())).thenReturn(true);

        mockMvc.perform(post("/main/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"" + userRequest.username() + "\", \"password\": \"" + userRequest.password() + "\"}"))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Method of testing sign in user functionality")
    public void testSignInUser() throws Exception {
        UserRequest userRequest = new UserRequest("user", "password");

        when(userService.authenticateUser(userRequest.username(), userRequest.password())).thenReturn(true);

        mockMvc.perform(post("/main/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"" + userRequest.username() + "\", \"password\": \"" + userRequest.password() + "\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Method of testing sign up functionality")
    public void testSignUp() throws Exception {
        UserRequest userRequest = new UserRequest("Test", "test");

        when(userService.registerUser(userRequest.username(), userRequest.password())).thenReturn(true);

        mockMvc.perform(post("/main/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"" + userRequest.username() + "\", \"password\": \"" + userRequest.password() + "\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Method of testing sign up failure functionality")
    public void testSignUpFailure() throws Exception {
        UserRequest userRequest = new UserRequest("existinguser", "password");

        when(userService.registerUser(userRequest.username(), userRequest.password())).thenReturn(false);

        mockMvc.perform(post("/main/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"" + userRequest.username() + "\", \"password\": \"" + userRequest.password() + "\"}"))
                .andExpect(status().isInternalServerError());
    }
}
