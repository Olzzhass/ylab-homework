package kaz.olzhas.ylab.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kaz.olzhas.ylab.annotations.CheckAuth;
import kaz.olzhas.ylab.dto.BookingDto;
import kaz.olzhas.ylab.dto.WorkspaceRequest;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.exception.AuthorizeException;
import kaz.olzhas.ylab.mapper.BookingMapper;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.state.Authentication;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST контроллер {@code AdminController} предоставляет операции, доступные для учетной записи администратора.
 *
 * <p>Контроллер обеспечивает доступ к операциям управления пользователями, рабочими местами и бронированиями,
 * требующими аутентификации с правами администратора через аннотацию {@code @CheckAuth(admin = true)}.</p>
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Api(value = "Admin Controller", description = "Operations for admin account")
public class AdminController {

    private final AdminService adminService;
    private final Authentication authentication;
    private final BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);

    /**
     * Показывает всех пользователей системы.
     *
     * @return HTTP ответ с списком всех пользователей
     */
    @GetMapping("/all-users")
    @CheckAuth(admin = true)
    @ApiOperation(value = "Show all users", response = List.class)
    public ResponseEntity<List<User>> showAllUsers(){
        List<User> allUsers = adminService.showAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    /**
     * Показывает все рабочие места в системе.
     *
     * @return HTTP ответ с списком всех рабочих мест
     */
    @GetMapping("/all-workspaces")
    @CheckAuth(admin = true)
    @ApiOperation(value = "Show all workspaces", response = List.class)
    public ResponseEntity<List<Workspace>> showAllWorkspaces(){
        List<Workspace> allWorkspaces = adminService.showAllWorkspaces();
        return ResponseEntity.ok(allWorkspaces);
    }

    /**
     * Добавляет новое рабочее место в систему.
     *
     * @param workspaceRequest Запрос на добавление нового рабочего места
     * @return HTTP ответ об успешном добавлении или ошибке
     */
    @PostMapping("/add-workspace")
    @CheckAuth(admin = true)
    @ApiOperation(value = "Add workspace", response = Object.class)
    public ResponseEntity<Object> addWorkspace(@RequestBody WorkspaceRequest workspaceRequest){
        if(adminService.addWorkspace(workspaceRequest.workspaceName())){
            return ResponseEntity.ok("Новое рабочее место успешно добавлено");
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка добавления нового рабочего места.");
        }
    }

    /**
     * Показывает все бронирования конкретного пользователя.
     *
     * @param username Имя пользователя для поиска бронирований
     * @return HTTP ответ с списком бронирований пользователя
     */
    @GetMapping("/bookings-by-user")
    @CheckAuth(admin = true)
    @ApiOperation(value = "Show all bookings by specific user", response = List.class)
    public ResponseEntity<List<BookingDto>> showBookingsByUser(@RequestParam String username){
        List<Booking> bookings = adminService.bookingsByUser(username);
        List<BookingDto> bookingsDto = bookings.stream()
                .map(bookingMapper::toDto)
                .toList();

        return ResponseEntity.ok(bookingsDto);
    }

    /**
     * Показывает все бронирования для конкретного рабочего места.
     *
     * @param workspaceId Идентификатор рабочего места для поиска бронирований
     * @return HTTP ответ с списком бронирований рабочего места
     */
    @GetMapping("/bookings-by-workspace")
    @CheckAuth(admin = true)
    @ApiOperation(value = "Show all bookings by workspace", response = List.class)
    public ResponseEntity<List<BookingDto>> showBookingsByWorkspace(@RequestParam Long workspaceId){
        List<Booking> bookings = adminService.bookingsByWorkspace(workspaceId);
        List<BookingDto> bookingsDto = bookings.stream()
                .map(bookingMapper::toDto)
                .toList();

        return ResponseEntity.ok(bookingsDto);
    }
}
