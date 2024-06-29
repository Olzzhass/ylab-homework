package kaz.olzhas.ylab.command.impl.adminCommands;

import kaz.olzhas.ylab.command.Command;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.service.AdminService;

import java.util.List;
import java.util.Scanner;

/**
 * Класс для просмотра всех рабочих мест
 */

public class SeeAllWorkspacesCommand implements Command {

    AdminService adminService;
    Scanner sc;

    public SeeAllWorkspacesCommand(AdminService adminService, Scanner sc){
        this.adminService = adminService;
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
    }
}
