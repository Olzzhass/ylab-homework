package kaz.olzhas.ylab.controller.handler;

import kaz.olzhas.ylab.exception.*;
import kaz.olzhas.ylab.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Обработчик исключений {@code MainExceptionHandler} для перехвата и обработки различных типов исключений
 * в REST контроллерах Spring приложения.
 *
 * <p>Этот класс аннотирован как {@code @RestControllerAdvice}, что позволяет ему глобально перехватывать
 * исключения, выбрасываемые контроллерами, и возвращать клиенту соответствующие HTTP ответы с установленными
 * статусами и сообщениями об ошибке.</p>
 */
@RestControllerAdvice
public class MainExceptionHandler {

    /**
     * Обрабатывает исключение {@link AuthorizeException}, возвращая HTTP ответ с кодом 401 (Unauthorized).
     *
     * @param exception Исключение {@link AuthorizeException}, которое было перехвачено
     * @return HTTP ответ с деталями об ошибке
     */
    @ExceptionHandler(AuthorizeException.class)
    ResponseEntity<AppException> handleAuthorizeException(AuthorizeException exception) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    /**
     * Создает HTTP ответ с указанным статусом и сообщением об ошибке.
     *
     * @param status  HTTP статус ответа
     * @param message Сообщение об ошибке
     * @return HTTP ответ с деталями об ошибке
     */
    private ResponseEntity<AppException> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(AppException.builder()
                .status(status.value())
                .message(message)
                .build());
    }
}
