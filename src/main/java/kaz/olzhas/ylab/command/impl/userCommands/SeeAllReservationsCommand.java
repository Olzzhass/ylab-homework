package kaz.olzhas.ylab.command.impl.userCommands;

import kaz.olzhas.ylab.command.Command;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.in.CoworkingServiceIn;
import kaz.olzhas.ylab.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Просмотр всех броней пользователя
 */
public class SeeAllReservationsCommand implements Command {

    UserService userService;
    Scanner sc;

    public SeeAllReservationsCommand(UserService userService, Scanner sc){
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
    }
}
