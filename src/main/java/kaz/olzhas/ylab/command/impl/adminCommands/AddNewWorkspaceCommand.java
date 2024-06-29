package kaz.olzhas.ylab.command.impl.adminCommands;

import kaz.olzhas.ylab.command.Command;
import kaz.olzhas.ylab.service.AdminService;

import java.util.Scanner;

/**
 * Класс который будет добавлять новое рабочее место.
 */

public class AddNewWorkspaceCommand implements Command {

    AdminService adminService;
    Scanner sc;

    public AddNewWorkspaceCommand(AdminService adminService, Scanner sc){
        this.adminService = adminService;
        this.sc = sc;
    }

    @Override
    public void execute() {
        System.out.print("Введите имя рабочего места: ");
        String nameOfWorkspace = sc.nextLine();
        adminService.addWorkspace(nameOfWorkspace);
    }
}
