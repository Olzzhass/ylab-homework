package kaz.olzhas.ylab.in;

import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.handler.AdminHandler;
import kaz.olzhas.ylab.handler.MainHandler;
import kaz.olzhas.ylab.handler.UserHandler;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.service.WorkspaceService;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class CoworkingServiceIn {

    UserService userService;
    WorkspaceService workspaceService;
    AdminService adminService;

    //Конструктор для входного класса
    public CoworkingServiceIn(){
        userService = new UserService();
        workspaceService = new WorkspaceService(userService);
        adminService = new AdminService(workspaceService, userService);
    }

    Scanner sc = new Scanner(System.in);

    static boolean loggedIn = false;
    static String whoLogged = null;

    public void start(MainHandler mainHandler, UserHandler userHandler, AdminHandler adminHandler){
        while (true){
            if(!loggedIn){

                mainHandler.displayMainPanel();
                System.out.print("Выберите действие: ");
                int userAction = sc.nextInt();
                sc.nextLine();

                switch (userAction){
                    case 1:
                        System.out.println("Круто! Давайте приступим к регистрации:");
                        System.out.print("Введите имя пользователя: ");
                        String registrationUsername = sc.nextLine();
                        System.out.print("Введите пароль: ");
                        String registrationPassword = sc.nextLine();
                        if(userService.registerUser(registrationUsername, registrationPassword)){
                            System.out.println("Вы успешно зарегестрировались на нашей системе.");
                            loggedIn = true;
                            whoLogged = registrationUsername;
                        }else{
                            System.out.println("Такой пользователь уже существует,");
                        }
                        break;
                    case 2:
                        System.out.print("Введите имя пользователя: ");
                        String authUsername = sc.nextLine();
                        System.out.print("Введите пароль: ");
                        String authPassword = sc.nextLine();
                        if(userService.authenticateUser(authUsername, authPassword)){
                            System.out.println("Добро пожаловать в наш Coworking Centre");
                            loggedIn = true;
                            whoLogged = authUsername;
                        }else{
                            System.out.println("Ваши данные не верные. Пожалуйста, попробуйте снова!");
                        }
                        break;
                    case 3:
                        mainHandler.quit();
                        break;
                    default:
                        System.out.println("Был введен неверный выбор. Попробуйте снова.");
                }
            } else if (whoLogged.equalsIgnoreCase("admin")) {

                adminHandler.displayAdminPanel();
                System.out.print("Выберите действие: ");
                int adminChoice = sc.nextInt();
                sc.nextLine();

                switch (adminChoice){
                    case 1:
                        //ADD A NEW WORKSPACE
                        System.out.print("Введите id номер добавляемого рабочего места: ");
                        int idOfWorkspace = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Введите имя рабочего места: ");
                        String nameOfWorkspace = sc.nextLine();
                        adminService.addWorkspace(idOfWorkspace, nameOfWorkspace);

                        break;
                    case 2:
                        //QUIT FROM ADMIN ACCOUNT
                        whoLogged = null;
                        loggedIn = false;

                        break;
                    case 3:
                        //SEE ALL WORKSPACES
                        adminService.seeAllWorkspaces();

                        break;
                    case 4:
                        //SEE ALL USERS
                        adminService.seeAllUsers();

                        break;
                    case 5:
                        //SEE BY WORKSPACE

                        adminService.seeAllWorkspaces();

                        System.out.print("Введите id рабочего места: ");
                        int idWorkspace = sc.nextInt();
                        sc.nextLine();

                        List<Booking> bookings1 = adminService.bookingsByWorkspace(idWorkspace);
                        for(Booking booking : bookings1){
                            System.out.println(booking.getStart() + " - " + booking.getEnd());
                        }

                        break;
                    case 6:
                        //SEE BY USER

                        adminService.seeAllUsersWithoutPassword();
                        System.out.print("Введите имя пользователя: ");
                        String username = sc.nextLine();

                        adminService.bookingsByUser(username);



                        break;
                    default:
                        System.out.println("Был введен неверный выбор. Попробуйте снова.");
                }



            } else if (whoLogged != null) {

                userHandler.displayUserPanel();
                System.out.print("Выберите действие: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice){
                    case 1:
                        //SEE ALL RESERVATIONS
                        userService.showAllReservations(whoLogged);
                        break;
                    case 2:
                        //RESERVATION OF SPACE
                        workspaceService.showAllAvailableWorkspaces();
                        System.out.print("Введите номер того места который хотите забронировать: ");
                        int workspaceId = sc.nextInt();
                        sc.nextLine();

                        //LOGIC OF BOOKING WITH TIME

                        System.out.print("Введите дату и время начала брони (yyyy-MM-ddTHH:mm): ");
                        String startDateTime = sc.nextLine();
                        System.out.print("Введите дату и время окончания брони (yyyy-MM-ddTHH:mm): ");
                        String endDateTime = sc.nextLine();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                        LocalDateTime start = LocalDateTime.parse(startDateTime, formatter);
                        LocalDateTime end = LocalDateTime.parse(endDateTime, formatter);

                        if (workspaceService.bookWorkspace(workspaceId, start, end, whoLogged)) {
                            System.out.println("Рабочее место успешно забронировано.");
                        } else {
                            System.out.println("Ошибка: Рабочее место недоступно в выбранное время.");
                        }

                        break;
                    case 3:
                        //LOGIC OF DELETE RESERVATIONS

                        User user = userService.getUserByUsername(whoLogged);
                        List<Workspace> workspaceList = user.getWorkspaceList();

                        if(workspaceList.size() == 0){
                            System.out.println("Вы еще не бронировали места. Пожалуйста сначала забронируйте место.");
                        }else{
                            userService.showAllReservations(whoLogged);
                            System.out.print("Введите id номер того места в котором вы хотите удалить бронь: ");
                            int workspaceNumber = sc.nextInt();
                            sc.nextLine();

                            userService.showAllBookingsInReservation(whoLogged, workspaceNumber);

                            System.out.print("Введите id номер брони которую вы хотите удалить: ");
                            int bookingNumber = sc.nextInt();
                            sc.nextLine();

                            workspaceService.deleteReservation(workspaceNumber, bookingNumber, whoLogged);
                        }


                        break;

                    case 4:
                        //GET ALL AVAILABLE SLOTS BY DATE

                        workspaceService.showAllAvailableWorkspaces();
                        System.out.print("Введите id места у которого хотите посмотреть свободные места: ");
                        int idOfWorkSpace = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Введите дату (формат: yyyy-MM-dd): ");
                        String dateInput = sc.nextLine();
                        LocalDateTime date = LocalDateTime.parse(dateInput + "T00:00:00");

                        List<LocalDateTime> availableSlots = workspaceService.getAvailableSlots(idOfWorkSpace, date);

                        System.out.println("Доступные слоты для рабочего места " + idOfWorkSpace + " на дату " + dateInput + ":");
                        for (LocalDateTime slot : availableSlots) {
                            System.out.println(slot + " - " + slot.plusHours(1));
                        }

                        break;
                    case 5:
                        //EXIT FROM ACCOUNT
                        whoLogged = null;
                        loggedIn = false;

                        break;
                    default:
                        System.out.println("Был введен неверный выбор. Попробуйте снова.");
                }

            }else{
                System.out.println("ПОКА ЧТО НЕ ПРИДУМАЛ ЧТО ТУТ МОЖЕТ БЫТЬ");
            }
        }
    }
}
