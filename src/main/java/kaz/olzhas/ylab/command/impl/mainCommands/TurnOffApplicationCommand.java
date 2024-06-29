package kaz.olzhas.ylab.command.impl.mainCommands;

import kaz.olzhas.ylab.command.Command;
import kaz.olzhas.ylab.handler.MainHandler;

/**
 * Выход из приложения
 */
public class TurnOffApplicationCommand implements Command {
    @Override
    public void execute() {
        MainHandler mainHandler = new MainHandler();
        mainHandler.quit();
    }
}
