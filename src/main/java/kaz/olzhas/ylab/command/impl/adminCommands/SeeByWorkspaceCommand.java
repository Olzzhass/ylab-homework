package kaz.olzhas.ylab.command.impl.adminCommands;

import kaz.olzhas.ylab.command.Command;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.service.UserService;

import java.util.List;
import java.util.Scanner;

/**
 * Для того чтобы просмотреть все брони по определенному рабочему месту.
 */

public class SeeByWorkspaceCommand implements Command {
    UserService userService;

    AdminService adminService;
    Scanner sc;

    public SeeByWorkspaceCommand(AdminService adminService, UserService userService, Scanner sc){
        this.adminService = adminService;
        this.userService = userService;
        this.sc = sc;
    }

    @Override
    public void execute() {
        List<Workspace> workspaces = adminService.seeAllWorkspaces();
        if(workspaces.size() == 0){
            System.out.println("Пока что вы не добавили рабочие места.");
        }else{
            System.out.println("Доступные рабочие места:");
            for (Workspace workspace : workspaces) {
                System.out.println(workspace.getId() + " : " + workspace.getName());
            }
        }

        System.out.print("Введите id рабочего места: ");
        long idWorkspace = sc.nextLong();
        sc.nextLine();

        List<Booking> bookings1 = adminService.bookingsByWorkspace(idWorkspace);
        for(Booking booking : bookings1){
            User user = userService.getUserById(booking.getUserId());
            System.out.println(booking.getStart() + " - " + booking.getEnd() + " : " + user.getUsername());
        }
    }
}
