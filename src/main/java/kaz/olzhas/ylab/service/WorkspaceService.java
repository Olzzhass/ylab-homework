package kaz.olzhas.ylab.service;

import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.entity.Workspace;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class WorkspaceService {

    private UserService userService;

    private List<Workspace> workspaceList = new ArrayList<>();
    //Конструктор для Сервиса рабочего места

    public WorkspaceService(UserService userService){
        this.userService = userService;
    }

    /*
    Просмотр всех доступных рабочих мест
     */
    public void showAllAvailableWorkspaces() {
        System.out.println("Доступные рабочие места:");
        for(Workspace workspace : workspaceList){
            System.out.println(workspace.getId() + " : " + workspace.getName());
        }
    }

    /*
    Метод для брони помещения
     */
    public boolean bookWorkspace(int workspaceId, LocalDateTime start, LocalDateTime end, String whoLogged) {

        Workspace workspace = getWorkspaceById(workspaceId);
        if(workspace.isAvailable(start, end)){
            Booking booking = new Booking(start, end);
            workspace.addBooking(booking);

            User user = userService.getUserByUsername(whoLogged);
            List<Workspace> userWorkspaceList = user.getWorkspaceList();
            Optional<Workspace> userWorkspace = userWorkspaceList.stream()
                    .filter(w -> w.getId() == workspaceId)
                    .findFirst();

            if (userWorkspace.isPresent()) {
                userWorkspace.get(); //.addBooking()
            } else {
                userWorkspaceList.add(workspace);
            }
            return true;
        }
        return false;
    }

    /*
    Метод для удаления брони
     */
    public void deleteReservation(int workspaceNumber, int bookingNumber, String whoLogged) {
        boolean workspaceDeleted = false;
        boolean userBookingDeleted = false;

        Workspace workspace = getWorkspaceById(workspaceNumber);
        List<Booking> bookings = workspace.getBookings();
        Iterator<Booking> iterator = bookings.iterator();
        while (iterator.hasNext()) {
            Booking booking = iterator.next();
            if (booking.getId() == bookingNumber) {
                iterator.remove();
                workspaceDeleted = true;
            }
        }
        User user = userService.getUserByUsername(whoLogged);
        List<Workspace> userWorkspaceList = user.getWorkspaceList();

        for(Workspace workspace1 : userWorkspaceList){
            if(workspace1.getId() == workspaceNumber){
                List<Booking> userWorkspaceBookings = workspace1.getBookings();
                if(userWorkspaceBookings.size() == 0){
                    userWorkspaceList.remove(workspace1);
                    userBookingDeleted = true;
                    break;
                }
                userWorkspaceBookings.removeIf(booking -> booking.getId() == bookingNumber);
                userBookingDeleted = true;
            }
        }

        if( workspaceDeleted && userBookingDeleted){
            System.out.println("Ваш бронь успешно удалено!");
        }else{
            System.out.println("Ошибка при удалении брони.");
        }
    }

    /*
    Метод чтобы получить доступные слоты по помещению на определенную дату
     */
    public List<LocalDateTime> getAvailableSlots(int workspaceId, LocalDateTime date){
        Workspace workspace = getWorkspaceById(workspaceId);
        return workspace.getAvailableSlots(date);
    }

    /*
    Метод для получения помещения по его id
     */
    public Workspace getWorkspaceById(int id){
        for(Workspace workspace : workspaceList){
            if(workspace.getId() == id){
                return workspace;
            }
        }
        return null;
    }

    /*
    Методы чтобы получить все брони
     */
    public List<Booking> getAllBookings(){
        List<Booking> allBookings = new ArrayList<>();
        for(Workspace workspace : workspaceList){
            allBookings.addAll(workspace.getBookings());
        }
        return allBookings;
    }

    public List<Workspace> getWorkspaceList() {
        return workspaceList;
    }

    public void setWorkspaceList(List<Workspace> workspaceList) {
        this.workspaceList = workspaceList;
    }
}
