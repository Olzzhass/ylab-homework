package kaz.olzhas.ylab.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kaz.olzhas.ylab.dto.ResponseMessage;
import kaz.olzhas.ylab.dto.UserRequest;
import kaz.olzhas.ylab.exception.AuthorizeException;
import kaz.olzhas.ylab.exception.RegisterException;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.state.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер безопасности {@code SecurityController} предоставляет операции для аутентификации и регистрации пользователей.
 *
 * <p>Контроллер предоставляет эндпоинты для процессов аутентификации ({@code /sign-in}) и регистрации ({@code /sign-up}).
 * В зависимости от типа пользователя (администратор или обычный пользователь), используются соответствующие сервисы
 * для выполнения операций аутентификации и регистрации. В случае успешной операции, контроллер устанавливает аутентификацию
 * и возвращает соответствующее сообщение. В случае ошибки, возвращается HTTP статус 500 с деталями ошибки.</p>
 */
@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
@Api(value = "Security Controller", description = "Operations like registration or authentication")
public class SecurityController {

    private final AdminService adminService;
    private final UserService userService;
    private final Authentication authentication;

    /**
     * Процесс аутентификации пользователя.
     *
     * @param userRequest Запрос пользователя с именем и паролем для аутентификации
     * @return HTTP ответ с результатом аутентификации и сообщением об успешной аутентификации или ошибке
     */
    @PostMapping("/sign-in")
    @ApiOperation(value = "Authentication process", response = Object.class)
    public ResponseEntity<Object> authorization(@RequestBody UserRequest userRequest){
        boolean authorize = false;

        if(userRequest.username().equalsIgnoreCase("admin")){
            authorize = adminService.authenticateAdmin(userRequest.username(), userRequest.password());
        }else{
            authorize = userService.authenticateUser(userRequest.username(), userRequest.password());
        }

        if(authorize){
            authentication.setAuth(true);
            authentication.setUsername(userRequest.username());
            return ResponseEntity.ok(new ResponseMessage("Добро пожаловать в наш Coworking-Service"));
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthorizeException("Ошибка при входе в программу."));
        }
    }

    /**
     * Процесс регистрации нового пользователя.
     *
     * @param userRequest Запрос пользователя с именем и паролем для регистрации
     * @return HTTP ответ с результатом регистрации и сообщением об успешной регистрации или ошибке
     */
    @PostMapping("/sign-up")
    @ApiOperation(value = "Registration process", response = Object.class)
    public ResponseEntity<Object> registration(@RequestBody UserRequest userRequest){

        boolean registeredUser = userService.registerUser(userRequest.username(), userRequest.password());

        if(registeredUser){
            authentication.setAuth(true);
            authentication.setUsername(userRequest.username());
            return ResponseEntity.ok(new ResponseMessage("Пользователь с именем: " + userRequest.username() + " успешно создан."));
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RegisterException("Ошибка при регистрации!"));
        }
    }
}
