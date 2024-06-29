package kaz.olzhas.ylab.command.impl.adminCommands;

import kaz.olzhas.ylab.command.Command;
import kaz.olzhas.ylab.service.AdminService;

import java.util.Scanner;

/**
 * Класс который реализует функциональность увидеть все брони по определенному пользователю
 */

public class SeeByUser implements Command {

    AdminService adminService;
    Scanner sc;

    public SeeByUser(AdminService adminService, Scanner sc){
        this.adminService = adminService;
        this.sc = sc;
    }

    @Override
    public void execute() {
        adminService.seeAllUsersWithoutPassword();
        System.out.print("Введите имя пользователя: ");
        String username = sc.nextLine();

        adminService.bookingsByUser(username);

    }
}
