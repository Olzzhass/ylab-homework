package kaz.olzhas.ylab.command.impl.mainCommands;

import kaz.olzhas.ylab.command.Command;
import kaz.olzhas.ylab.in.CoworkingServiceIn;
import kaz.olzhas.ylab.service.UserService;

import java.util.Scanner;

/**
 * Логика регистрации пользователя
 */
public class RegistrationCommand implements Command {

    private UserService userService;
    private Scanner sc;

    public RegistrationCommand(UserService userService, Scanner sc) {
        this.userService = userService;
        this.sc = sc;
    }

    @Override
    public void execute() {
        System.out.println("Круто! Давайте приступим к регистрации:");
        System.out.print("Введите имя пользователя: ");
        String registrationUsername = sc.nextLine();
        System.out.print("Введите пароль: ");
        String registrationPassword = sc.nextLine();
        if(userService.registerUser(registrationUsername, registrationPassword)){
            System.out.println("Вы успешно зарегестрировались на нашей системе.");
            CoworkingServiceIn.loggedIn = true;
            CoworkingServiceIn.whoLogged = registrationUsername;
        }else{
            System.out.println("Такой пользователь уже существует,");
        }
    }
}
