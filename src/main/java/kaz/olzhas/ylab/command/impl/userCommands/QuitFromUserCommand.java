package kaz.olzhas.ylab.command.impl.userCommands;

import kaz.olzhas.ylab.command.Command;
import kaz.olzhas.ylab.in.CoworkingServiceIn;

/**
 * Выход из аккаунта пользователя
 */

public class QuitFromUserCommand implements Command {
    @Override
    public void execute() {
        CoworkingServiceIn.whoLogged = null;
        CoworkingServiceIn.loggedIn = false;

    }
}
