package kaz.olzhas.ylab.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kaz.olzhas.ylab.annotations.CheckAuth;
import kaz.olzhas.ylab.dto.BookingDto;
import kaz.olzhas.ylab.dto.DeleteBookingDto;
import kaz.olzhas.ylab.dto.ResponseMessage;
import kaz.olzhas.ylab.dto.ShowSlotsRequestDto;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.mapper.BookingMapper;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.service.WorkspaceService;
import kaz.olzhas.ylab.state.Authentication;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Контроллер пользователя {@code UserController} предоставляет операции для работы с бронированием рабочих мест и просмотра своих бронирований.
 *
 * <p>Контроллер предоставляет эндпоинты для бронирования рабочих мест ({@code /booking}), удаления бронирования ({@code /deleteBooking}),
 * просмотра доступных временных слотов ({@code /showSlots}) и просмотра собственных бронирований ({@code /my-workspaces}).
 * Для выполнения этих операций используются сервисы {@link WorkspaceService} и {@link UserService}, аутентификация пользователя
 * осуществляется через {@link Authentication}.</p>
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Api(value = "User Controller", description = "Operations of user")
public class UserController {

    private final UserService userService;
    private final WorkspaceService workspaceService;
    private final Authentication authentication;
    private final BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);

    /**
     * Эндпоинт для бронирования рабочего места на определенное время.
     *
     * @param bookingDto DTO с информацией о бронировании
     * @return HTTP ответ с результатом бронирования и сообщением о его успешном выполнении или ошибке
     */
    @PostMapping("/booking")
    @CheckAuth
    @ApiOperation(value = "Workspace booking for specific time", response = Object.class)
    public ResponseEntity<Object> bookWorkspace(@RequestBody BookingDto bookingDto){

        boolean isBooked = workspaceService.bookWorkspace(
                bookingDto.getWorkspaceId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                authentication.getUsername()
        );

        if(isBooked){
            return ResponseEntity.ok(new ResponseMessage("Рабочее место успешно забронировано!"));
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка бронирования!");
        }
    }

    /**
     * Эндпоинт для удаления бронирования рабочего места.
     *
     * @param deleteBookingDto DTO с идентификатором бронирования для удаления
     * @return HTTP ответ с результатом удаления бронирования и сообщением о его успешном выполнении или ошибке
     */
    @PostMapping("/deleteBooking")
    @CheckAuth
    @ApiOperation(value = "Delete workspace booking", response = Object.class)
    public ResponseEntity<Object> deleteBooking(@RequestBody DeleteBookingDto deleteBookingDto){

        boolean isDeleted = workspaceService.deleteReservation(deleteBookingDto.getBookingId());

        if(isDeleted){
            return ResponseEntity.ok(new ResponseMessage("Бронирование места успешно удалено!"));
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка удаления!");
        }
    }

    /**
     * Эндпоинт для просмотра доступных временных слотов по конкретной дате.
     *
     * @param requestDTO DTO с датой и идентификатором рабочего места для поиска доступных слотов
     * @return HTTP ответ с списком доступных временных слотов на указанную дату
     */
    @GetMapping("/showSlots")
    @CheckAuth
    @ApiOperation(value = "Show all available slots by specific date", response = List.class)
    public ResponseEntity<List<LocalDateTime>> showSlotsByDate(@RequestBody ShowSlotsRequestDto requestDTO){

        LocalDateTime date = LocalDateTime.parse(requestDTO.getDate() + "T00:00:00");

        List<LocalDateTime> availableSlots = workspaceService.getAvailableSlots(requestDTO.getWorkspaceId(), date);

        return ResponseEntity.ok(availableSlots);
    }

    /**
     * Эндпоинт для просмотра бронирований пользователя по рабочим местам.
     *
     * @return HTTP ответ с списком бронирований пользователя в виде DTO
     */
    @GetMapping("/my-workspaces")
    @CheckAuth
    @ApiOperation(value = "Show user bookings by workspaces", response = List.class)
    public ResponseEntity<List<BookingDto>> showUserWorkspaces(){

        String username = authentication.getUsername();

        List<Booking> bookings = userService.showAllReservations(username);

        List<BookingDto> bookingsDto = bookings.stream().map(bookingMapper::toDto).toList();

        return ResponseEntity.ok(bookingsDto);
    }
}
