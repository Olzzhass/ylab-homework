package kaz.olzhas.ylab.command.impl.adminCommands;

import kaz.olzhas.ylab.command.Command;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.service.AdminService;

import java.util.List;
import java.util.Scanner;

/**
 * Командный класс который имеет функциональность чтобы просмотреть всех пользователей
 */

public class SeeAllUsers implements Command {

    AdminService adminService;
    Scanner sc;

    public SeeAllUsers(AdminService adminService, Scanner sc){
        this.adminService = adminService;
        this.sc = sc;
    }

    @Override
    public void execute() {
        List<User> users = adminService.seeAllUsers();
        if(users.size() == 0){
            System.out.println("Пока что пользователей нет.");
        }else{
            for(User user : users){
                System.out.println("Username: " + user.getUsername());
                System.out.println("Password: " + user.getPassword() + '\n');
            }
        }
    }
}
