package kaz.olzhas.ylab.command.impl.userCommands;

import kaz.olzhas.ylab.command.Command;
import kaz.olzhas.ylab.in.CoworkingServiceIn;
import kaz.olzhas.ylab.service.WorkspaceService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Бронирование места для пользователя
 */
public class ReservationOfSpaceCommand implements Command {

    WorkspaceService workspaceService;
    Scanner sc;

    public ReservationOfSpaceCommand(WorkspaceService workspaceService, Scanner sc){
        this.workspaceService = workspaceService;
        this.sc = sc;
    }

    @Override
    public void execute() {
        workspaceService.showAllAvailableWorkspaces();
        System.out.print("Введите номер того места который хотите забронировать: ");
        long workspaceId = sc.nextLong();
        sc.nextLine();

        //LOGIC OF BOOKING WITH TIME

        System.out.print("Введите дату и время начала брони (yyyy-MM-ddTHH:mm): ");
        String startDateTime = sc.nextLine();
        System.out.print("Введите дату и время окончания брони (yyyy-MM-ddTHH:mm): ");
        String endDateTime = sc.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime start = LocalDateTime.parse(startDateTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endDateTime, formatter);

        if (workspaceService.bookWorkspace(workspaceId, start, end, CoworkingServiceIn.whoLogged)) {
            System.out.println("Рабочее место успешно забронировано.");
        } else {
            System.out.println("Ошибка: Рабочее место недоступно в выбранное время.");
        }
    }
}
