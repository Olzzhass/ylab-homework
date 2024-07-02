package kaz.olzhas.ylab.command;

import java.util.Map;
import java.util.Scanner;

public class CommandExecutor {

    private Scanner sc;

    public CommandExecutor(Scanner sc) {
        this.sc = sc;
    }

    /**
     * Метод для выполнения команды на основе выбранного пользователем действия.
     *
     * @param commands карта команд, с которыми можно взаимодействовать
     */
    public void executeCommand(Map<Integer, Command> commands) {
        System.out.print("Выберите действие: ");
        int choice = sc.nextInt();
        sc.nextLine();
        Command command = commands.get(choice);
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Был введен неверный выбор. Попробуйте снова.");
        }
    }
}
