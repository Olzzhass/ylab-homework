package kaz.olzhas.ylab.command;

import kaz.olzhas.ylab.command.impl.adminCommands.*;
import kaz.olzhas.ylab.command.impl.mainCommands.AuthorizationCommand;
import kaz.olzhas.ylab.command.impl.mainCommands.RegistrationCommand;
import kaz.olzhas.ylab.command.impl.mainCommands.TurnOffApplicationCommand;
import kaz.olzhas.ylab.command.impl.userCommands.*;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.service.WorkspaceService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Фабрика команд для создания мап команд для различных типов пользователей.
 * Этот класс предоставляет методы для создания команд, которые могут быть использованы в приложении
 * в зависимости от роли пользователя (mainPanel, userPanel, adminPanel).
 */
public class CommandFactory {

    /**
     * Создает мап команд для главной панели.
     *
     * @param userService сервис для управления пользователями
     * @param sc сканер для ввода пользовательских данных
     * @return мап команд для главной панели
     */
    public static Map<Integer, Command> createMainCommands(UserService userService, Scanner sc){
        Map<Integer, Command> mainCommands = new HashMap<>();
        mainCommands.put(1, new RegistrationCommand(userService, sc));
        mainCommands.put(2, new AuthorizationCommand(userService, sc));
        mainCommands.put(3, new TurnOffApplicationCommand());
        return mainCommands;
    }

    /**
     * Создает мап команд для пользовательской панели.
     *
     * @param userService сервис для управления пользователями
     * @param workspaceService сервис для управления рабочими местами
     * @param sc сканер для ввода пользовательских данных
     * @return мап команд для пользовательской панели
     */
    public static Map<Integer, Command> createUserCommands(UserService userService, WorkspaceService workspaceService, Scanner sc){
        Map<Integer, Command> userCommands = new HashMap<>();
        userCommands.put(1, new SeeAllReservationsCommand(userService, sc));
        userCommands.put(2, new ReservationOfSpaceCommand(workspaceService, sc));
        userCommands.put(3, new DeleteReservationCommand(workspaceService, userService, sc));
        userCommands.put(4, new AllAvailableSlotsByDateCommand(workspaceService, sc));
        userCommands.put(5, new QuitFromUserCommand());
        return userCommands;
    }

    /**
     * Создает мап команд для админ панели.
     *
     * @param adminService сервис для управления администратором
     * @param userService сервис для управления пользователями
     * @param sc сканер для ввода пользовательских данных
     * @return мап команд для админ панели
     */
    public static Map<Integer, Command> createAdminCommands(AdminService adminService, UserService userService, Scanner sc){
        Map<Integer, Command> adminCommands = new HashMap<>();
        adminCommands.put(1, new AddNewWorkspaceCommand(adminService, sc));
        adminCommands.put(2, new QuitFromAdminCommand());
        adminCommands.put(3, new SeeAllWorkspacesCommand(adminService, sc));
        adminCommands.put(4, new SeeAllUsers(adminService, sc));
        adminCommands.put(5, new SeeByWorkspaceCommand(adminService, userService, sc));
        adminCommands.put(6, new SeeByUser(adminService, sc));
        return adminCommands;
    }
}
