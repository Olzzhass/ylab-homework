package kaz.olzhas.ylab.entity;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Это класс помещения
public class Workspace {
    private int id; // id номер помещения
    private String name; //имя помещения
    private List<Booking> bookings; // и так как у каждого помещения будут брони например по часам я их вывел в отдельную сущность

    //Конструктор для рабочего места
    public Workspace(int id, String name) {
        bookings = new ArrayList<>();
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    //Метод для проверки свободна ли помещение в определенное время
    public boolean isAvailable(LocalDateTime start, LocalDateTime end) {
        for(Booking booking : bookings){
            if(booking.overlaps(start, end)){
                return false;
            }
        }
        return true;
    }

    //Добавляет новую бронь
    public void addBooking(Booking booking){
        bookings.add(booking);
    }

    //Проходит по Bookings и определяет свободные слоты по дате
    public List<LocalDateTime> getAvailableSlots(LocalDateTime date) {
        List<LocalDateTime> availableSlots = new ArrayList<>();
        LocalDateTime startOfDay = date.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = date.withHour(23).withMinute(59).withSecond(59);

        for(LocalDateTime slot = startOfDay; slot.isBefore(endOfDay); slot = slot.plusHours(1)){
            boolean isSlotAvailable = true;
            for(Booking booking : bookings){
                if(booking.overlaps(slot, slot.plusHours(1))){
                    isSlotAvailable = false;
                    break;
                }
            }
            if(isSlotAvailable){
                availableSlots.add(slot);
            }
        }

        return availableSlots;
    }
}
