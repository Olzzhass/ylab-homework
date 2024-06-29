package kaz.olzhas.ylab.command.impl.mainCommands;

import kaz.olzhas.ylab.command.Command;
import kaz.olzhas.ylab.in.CoworkingServiceIn;
import kaz.olzhas.ylab.service.UserService;

import java.util.Scanner;

/**
 * Класс для авторизации пользователя
 */
public class AuthorizationCommand implements Command {

    UserService userService;

    Scanner sc;

    public AuthorizationCommand(UserService userService, Scanner sc) {
        this.userService = userService;
        this.sc = sc;
    }

    @Override
    public void execute() {
        System.out.print("Введите имя пользователя: ");
        String authUsername = sc.nextLine();
        System.out.print("Введите пароль: ");
        String authPassword = sc.nextLine();
        if(userService.authenticateUser(authUsername, authPassword)){
            System.out.println("Добро пожаловать в наш Coworking Centre");
            CoworkingServiceIn.loggedIn = true;
            CoworkingServiceIn.whoLogged = authUsername;
        }else{
            System.out.println("Ваши данные не верные. Пожалуйста, попробуйте снова!");
        }
    }
}
