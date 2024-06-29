package kaz.olzhas.ylab.command.impl.adminCommands;

import kaz.olzhas.ylab.command.Command;
import kaz.olzhas.ylab.in.CoworkingServiceIn;

public class QuitFromAdminCommand implements Command {

    /**
     * Метод для выхода из аккаунта админа
     */

    @Override
    public void execute() {
        CoworkingServiceIn.whoLogged = null;
        CoworkingServiceIn.loggedIn = false;
    }
}
