package kaz.olzhas.ylab.command.impl.userCommands;

import kaz.olzhas.ylab.command.Command;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.in.CoworkingServiceIn;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.service.WorkspaceService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Удаление брони пользователя
 */
public class DeleteReservationCommand implements Command {

    WorkspaceService workspaceService;
    UserService userService;
    Scanner sc;

    public DeleteReservationCommand(WorkspaceService workspaceService, UserService userService, Scanner sc){
        this.workspaceService = workspaceService;
        this.userService = userService;
        this.sc = sc;
    }

    @Override
    public void execute() {
        List<Booking> bookings = userService.showAllReservations(CoworkingServiceIn.whoLogged);

        if(bookings.size() == 0){
            System.out.println("Вы ранее не бронировали место. Пожалуйста, сперва забронируйте себе место.");
        }else{
            for(Booking booking : bookings){
                Optional<Workspace> workspace = userService.getWorkspacesById(booking.getWorkspace_id());
                System.out.println(workspace.get().getId() + " : " + workspace.get().getName());
                System.out.println(booking.getId() + " - " + booking.getStart() + " : " + booking.getEnd());
            }
        }

        System.out.print("Введите id номер брони которую вы хотите удалить: ");
        long bookingId = sc.nextLong();
        sc.nextLine();

        if(workspaceService.deleteReservation(bookingId)){
            System.out.println("Ваш бронь успешно удалено!");
        }else{
            System.out.println("Ошибка при удалении брони.");
        }

    }
}
