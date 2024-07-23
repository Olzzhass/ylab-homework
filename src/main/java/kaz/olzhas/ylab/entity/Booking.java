package kaz.olzhas.ylab.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Сущность {@code Booking} представляет информацию о бронировании рабочего места.
 *
 * <p>Класс содержит поля, описывающие идентификатор брони ({@code id}), идентификатор пользователя ({@code userId}),
 * идентификатор рабочего места ({@code workspaceId}), начальное время бронирования ({@code start}) и конечное время бронирования ({@code end}).</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private Long id;
    private Long userId;
    private Long workspaceId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start; // начальное время бронирования
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end; // конечное время бронирования

    /**
     * Конструктор для создания объекта бронирования с указанием начального и конечного времени.
     *
     * @param start начальное время бронирования
     * @param end конечное время бронирования
     */
    public Booking(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Проверяет пересекаются ли временные интервалы текущей брони и переданного интервала.
     *
     * @param start начальное время для проверки
     * @param end конечное время для проверки
     * @return {@code true}, если временные интервалы пересекаются, иначе {@code false}
     */
    public boolean overlaps(LocalDateTime start, LocalDateTime end){
        return (this.start.isBefore(end) && this.end.isAfter(start));
    }
}
