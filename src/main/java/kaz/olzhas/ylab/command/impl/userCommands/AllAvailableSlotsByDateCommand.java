package kaz.olzhas.ylab.command.impl.userCommands;

import kaz.olzhas.ylab.command.Command;
import kaz.olzhas.ylab.service.WorkspaceService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 * Класс для просмотра всех доступных слотов по дате
 */
public class AllAvailableSlotsByDateCommand implements Command {

    WorkspaceService workspaceService;
    Scanner sc;

    public AllAvailableSlotsByDateCommand(WorkspaceService workspaceService, Scanner sc){
        this.workspaceService = workspaceService;
        this.sc = sc;
    }

    @Override
    public void execute() {
        workspaceService.showAllAvailableWorkspaces();
        System.out.print("Введите id места у которого хотите посмотреть свободные места: ");
        long idOfWorkSpace = sc.nextLong();
        sc.nextLine();

        System.out.print("Введите дату (формат: yyyy-MM-dd): ");
        String dateInput = sc.nextLine();
        LocalDateTime date = LocalDateTime.parse(dateInput + "T00:00:00");

        List<LocalDateTime> availableSlots = workspaceService.getAvailableSlots(idOfWorkSpace, date);

        System.out.println("Доступные слоты для рабочего места " + idOfWorkSpace + " на дату " + dateInput + ":");
        for (LocalDateTime slot : availableSlots) {
            System.out.println(slot + " - " + slot.plusHours(1));
        }
    }
}
