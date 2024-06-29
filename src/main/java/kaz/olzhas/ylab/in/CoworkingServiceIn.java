package kaz.olzhas.ylab.in;

import kaz.olzhas.ylab.command.Command;
import kaz.olzhas.ylab.command.impl.adminCommands.*;
import kaz.olzhas.ylab.command.impl.mainCommands.AuthorizationCommand;
import kaz.olzhas.ylab.command.impl.mainCommands.RegistrationCommand;
import kaz.olzhas.ylab.command.impl.mainCommands.TurnOffApplicationCommand;
import kaz.olzhas.ylab.command.impl.userCommands.*;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.handler.AdminHandler;
import kaz.olzhas.ylab.handler.MainHandler;
import kaz.olzhas.ylab.handler.UserHandler;
import kaz.olzhas.ylab.liquibase.LiquibaseDemo;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.service.WorkspaceService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Главный класс приложения Coworking Service, управляющий выполнением команд и отображением интерфейса для пользователей и администратора.
 */
public class CoworkingServiceIn {

    private UserService userService;
    private WorkspaceService workspaceService;
    private AdminService adminService;
    private Scanner sc;
    private Map<Integer, Command> mainCommands;
    private Map<Integer, Command> userCommands;
    private Map<Integer, Command> adminCommands;
    public static boolean loggedIn = false;
    public static String whoLogged = null;

    /**
     * Конструктор для инициализации сервисов и команд.
     */

    public CoworkingServiceIn(){
        userService = new UserService();
        workspaceService = new WorkspaceService(userService);
        adminService = new AdminService(workspaceService, userService);
        sc = new Scanner(System.in);


        /*

        Использовал паттерн Command

         */
        mainCommands = new HashMap<>();
        mainCommands.put(1, new RegistrationCommand(userService, sc));
        mainCommands.put(2, new AuthorizationCommand(userService, sc));
        mainCommands.put(3, new TurnOffApplicationCommand());

        userCommands = new HashMap<>();
        userCommands.put(1, new SeeAllReservationsCommand(userService, sc));
        userCommands.put(2, new ReservationOfSpaceCommand(workspaceService, sc));
        userCommands.put(3, new DeleteReservationCommand(workspaceService, userService, sc));
        userCommands.put(4, new AllAvailableSlotsByDateCommand(workspaceService, sc));
        userCommands.put(5, new QuitFromUserCommand());

        adminCommands = new HashMap<>();
        adminCommands.put(1, new AddNewWorkspaceCommand(adminService, sc));
        adminCommands.put(2, new QuitFromAdminCommand());
        adminCommands.put(3, new SeeAllWorkspacesCommand(adminService, sc));
        adminCommands.put(4, new SeeAllUsers(adminService, sc));
        adminCommands.put(5, new SeeByWorkspaceCommand(adminService, userService, sc));
        adminCommands.put(6, new SeeByUser(adminService, sc));

    }

    /**
     * Метод для запуска приложения, управляющий основным циклом ввода и выполнения команд.
     *
     * @param mainHandler объект для отображения основной панели
     * @param userHandler объект для обработки действий пользователя
     * @param adminHandler объект для обработки действий администратора
     */
    public void start(MainHandler mainHandler, UserHandler userHandler, AdminHandler adminHandler){

        LiquibaseDemo liquibaseDemo = LiquibaseDemo.getInstance();
        liquibaseDemo.runMigrations();

        while (true){
            if(!loggedIn){

                mainHandler.displayMainPanel();
                executeCommand(mainCommands);

            } else if (whoLogged.equalsIgnoreCase("admin")) {

                adminHandler.displayAdminPanel();
                executeCommand(adminCommands);

            } else if (whoLogged != null) {

                userHandler.displayUserPanel();
                executeCommand(userCommands);

            }else{
                System.out.println("Неправильный выбор.");
            }
        }
    }

    /**
     * Метод для выполнения команды на основе выбранного пользователем действия.
     *
     * @param commands карта команд, с которыми можно взаимодействовать
     */
    private void executeCommand(Map<Integer, Command> commands) {
        System.out.print("Выберите действие: ");
        int choice = sc.nextInt();
        sc.nextLine();
        Command command = commands.get(choice);
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Был введен неверный выбор. Попробуйте снова.");
        }
    }

}
