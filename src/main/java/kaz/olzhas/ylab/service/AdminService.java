package kaz.olzhas.ylab.service;

import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.entity.Workspace;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminService {
    private WorkspaceService workspaceService;
    private UserService userService;

    //Конструктор для Сервиса Админ класса
    public AdminService(WorkspaceService workspaceService, UserService userService) {
        this.workspaceService = workspaceService;
        this.userService = userService;
    }

    // Метод для добавления нового помещения
    public void addWorkspace(int id, String name){
        List<Workspace> workspaces = workspaceService.getWorkspaceList();
        workspaces.add(new Workspace(id, name));
        workspaceService.setWorkspaceList(workspaces);
        System.out.println("Рабочее место успешно добавлено.");
    }

    /*
    Метод для просмотра всех помещении
     */
    public void seeAllWorkspaces(){
        List<Workspace> workspaces = workspaceService.getWorkspaceList();
        if(workspaces.size() == 0){
            System.out.println("Пока что вы не добавили рабочие места.");
        }else {
            System.out.println("Доступные рабочие места:");
            for (Workspace workspace : workspaces) {
                System.out.println(workspace.getId() + " : " + workspace.getName());
            }
        }
    }

    /*
    Для просмотра всех пользователей
     */
    public void seeAllUsers(){
        Map<String, User> users = userService.getUsers();
        System.out.println("ВСЕ ПОЛЬЗОВАТЕЛИ:");
        for(Map.Entry<String, User> entry : users.entrySet()){
            User user = entry.getValue();
            System.out.println("Username: " + user.getUsername());
            System.out.println("Password: " + user.getPassword() + '\n');
        }
    }


    public void seeAllUsersWithoutPassword(){
        Map<String, User> users = userService.getUsers();
        System.out.println("ВСЕ ПОЛЬЗОВАТЕЛИ:");
        for(Map.Entry<String, User> entry : users.entrySet()){
            User user = entry.getValue();
            System.out.println("Username: " + user.getUsername());
        }
    }

    /*
    Просмот всех броней по определенной дате
     */
    public List<Booking> bookingsByDate(LocalDateTime start, LocalDateTime end){
        List<Booking> filteredBookings = new ArrayList<>();
        for (Booking booking : workspaceService.getAllBookings()) {
            if (!booking.getStart().isAfter(end) && !booking.getEnd().isBefore(start)) {
                filteredBookings.add(booking);
            }
        }
        return filteredBookings;
    }

    /*
   Просмотр всех броней по определенному помещению
     */
    public List<Booking> bookingsByWorkspace(int workspaceId){
        List<Booking> filteredBookings = new ArrayList<>();
        List<Workspace> workspaces = workspaceService.getWorkspaceList();
        for(Workspace workspace : workspaces){
            if(workspace.getId() == workspaceId){
                filteredBookings.addAll(workspace.getBookings());
                break;
            }
        }
        return filteredBookings;
    }

    /*
    Просмотр по определенному пользователю
     */
    public void bookingsByUser(String username){
        Map<String, User> users = userService.getUsers();
        for(Map.Entry<String, User> entry : users.entrySet()){
            User user = entry.getValue();
            if(user.getUsername().equals(username)){
                List<Workspace> workspaces = user.getWorkspaceList();
                for(Workspace workspace : workspaces){
                    System.out.println("Workspace: " + workspace.getName());
                    for(Booking booking : workspace.getBookings()){
                        System.out.println(booking.getStart() + " - " + booking.getEnd());
                    }
                }
            }
        }
    }
}
