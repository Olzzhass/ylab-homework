package kaz.olzhas.ylab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Класс броней
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private Long id;
    private Long user_id;
    private Long workspace_id;
    private LocalDateTime start; //start time
    private LocalDateTime end; //end time

    /**
     *
     * @param start
     * @param end
     *
     * Конструктор для броней
     */
    public Booking(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }


    /**
     *
     * @param start
     * @param end
     * @return
     *
     * Данный метод проверяет по временам брони свободность помещения
     */
    public boolean overlaps(LocalDateTime start, LocalDateTime end){
        return (this.start.isBefore(end) && this.end.isAfter(start));
    }

}
