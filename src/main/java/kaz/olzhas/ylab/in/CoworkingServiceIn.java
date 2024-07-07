package kaz.olzhas.ylab.in;

import kaz.olzhas.ylab.command.Command;
import kaz.olzhas.ylab.command.CommandExecutor;
import kaz.olzhas.ylab.command.CommandFactory;
import kaz.olzhas.ylab.handler.AdminHandler;
import kaz.olzhas.ylab.handler.MainHandler;
import kaz.olzhas.ylab.handler.UserHandler;
import kaz.olzhas.ylab.liquibase.LiquibaseDemo;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.service.WorkspaceService;
import kaz.olzhas.ylab.util.ConnectionManager;
import kaz.olzhas.ylab.util.PropertiesUtil;

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
    private CommandExecutor commandExecutor;

    private ConnectionManager connectionManager = new ConnectionManager(PropertiesUtil.get("db.url"),
            PropertiesUtil.get("db.username"),
            PropertiesUtil.get("db.password"),
            PropertiesUtil.get("db.driver"));
    public static boolean loggedIn = false;
    public static String whoLogged = null;

    /**
     * Конструктор для инициализации сервисов и команд.
     */

    public CoworkingServiceIn(){
        userService = new UserService(connectionManager);
        workspaceService = new WorkspaceService(userService, connectionManager);
        adminService = new AdminService(workspaceService, userService, connectionManager);
        sc = new Scanner(System.in);
        commandExecutor = new CommandExecutor(sc);

        mainCommands = CommandFactory.createMainCommands(userService, sc);
        userCommands = CommandFactory.createUserCommands(userService, workspaceService, sc);
        adminCommands = CommandFactory.createAdminCommands(adminService, userService, sc);

    }

    /**
     * Метод для запуска приложения, управляющий основным циклом ввода и выполнения команд.
     *
     * @param mainHandler объект для отображения основной панели
     * @param userHandler объект для обработки действий пользователя
     * @param adminHandler объект для обработки действий администратора
     */
    public void start(MainHandler mainHandler, UserHandler userHandler, AdminHandler adminHandler){

        LiquibaseDemo liquibaseDemo = new LiquibaseDemo(connectionManager.getConnection(), "db/main-changelog.xml", "liquibase");
        liquibaseDemo.runMigrations();

        while (true){
            if(!loggedIn){

                mainHandler.displayMainPanel();
                commandExecutor.executeCommand(mainCommands);

            } else if (whoLogged.equalsIgnoreCase("admin")) {

                adminHandler.displayAdminPanel();
                commandExecutor.executeCommand(adminCommands);

            } else if (whoLogged != null) {

                userHandler.displayUserPanel();
                commandExecutor.executeCommand(userCommands);

            }else{
                System.out.println("Неправильный выбор.");
            }
        }
    }

}
