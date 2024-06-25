package kaz.olzhas.ylab.entity;

import java.time.LocalDateTime;

//Класс брони
public class Booking {
    private static int idCounter = 0;
    //у каждой брони будет id номер
    private int id;
    private LocalDateTime start; //start time
    private LocalDateTime end; //end time

    //Конструктор для броней
    public Booking(LocalDateTime start, LocalDateTime end) {
        this.id = generateId();
        this.start = start;
        this.end = end;
    }

    private synchronized static int generateId() {
        return idCounter++;
    }

    //Данный метод проверяет по временам брони свободность помещения
    public boolean overlaps(LocalDateTime start, LocalDateTime end){
        return (this.start.isBefore(end) && this.end.isAfter(start));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
