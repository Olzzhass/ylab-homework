package kaz.olzhas.ylab.service;

import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.entity.Workspace;

import java.awt.print.Book;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private static Map<String, User> users;

    //Конструктор для Сервиса пользователь класса
    public UserService(){
        users = new HashMap<>();
    }

    /*
    Метод для регистрации пользователя
     */
    public boolean registerUser(String registrationUsername, String registrationPassword) {
        if(!users.containsKey(registrationUsername)){
            User user = new User(registrationUsername, registrationPassword);
            users.put(registrationUsername, user);
            return true;
        }
        return false;
    }

    /*
    Метод для проверки данных введенных пользователем
     */
    public boolean authenticateUser(String authUsername, String authPassword) {

        // Можно было бы хранить в другом месте
        if(authUsername.equalsIgnoreCase("admin") && authPassword.equalsIgnoreCase("admin123")){
            return true;
        }

        User user = users.get(authUsername);
        if(user != null){
            return user.getUsername().equals(authUsername) && user.getPassword().equals(authPassword);
        }else{
            return false;
        }
    }

    /*
    Метод для просмотра всех броней пользователя
     */
    public void showAllReservations(String whoLogged) {
        List<Workspace> workspaces = users.get(whoLogged).getWorkspaceList();

        if(workspaces.size() == 0){
            System.out.println("Вы ранее не бронировали место. Пожалуйста, сперва забронируйте себе место.");
        }else{
            for(int i = 0; i < workspaces.size(); i++){
                Workspace workspace = workspaces.get(i);
                List<Booking> bookings = workspace.getBookings();
                System.out.println(workspace.getId() + " : " + workspace.getName());
                for(Booking booking : bookings){
                    System.out.println(booking.getId() + " - " + booking.getStart() + " : " + booking.getEnd());
                }
            }
        }
    }

    /*
    Метод который возвращает пользователя по его username
     */
    public User getUserByUsername(String whoLogged) {
        return users.get(whoLogged);
    }

    /*
    Просмотр броней по зарезервированному помещению
     */
    public void showAllBookingsInReservation(String whoLogged, int workspaceNumber) {
        List<Workspace> workspaces = users.get(whoLogged).getWorkspaceList();

        for(Workspace workspace : workspaces){
            if(workspace.getId() == workspaceNumber){
                for(Booking booking : workspace.getBookings()){
                    System.out.println(booking.getId() + " - " + booking.getStart() + " : " + booking.getEnd());
                }
                break;
            }
        }
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public static void setUsers(Map<String, User> users) {
        UserService.users = users;
    }
}
