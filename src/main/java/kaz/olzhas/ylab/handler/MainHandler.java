package kaz.olzhas.ylab.handler;

/**
 * Класс для отображения основной панели и управления основными действиями приложения.
 */
public class MainHandler {

    /**
     * Метод для отображения основной панели с возможными действиями для всех пользователей.
     */
    public void displayMainPanel(){
        System.out.println("Добро пожаловать в наш Coworking Centre.");
        System.out.println("       Выберите действие:");
        System.out.println("1. Зарегестрироваться");
        System.out.println("2. Войти в существующий аккаунт");
        System.out.println("3. Выключить приложение"); //TODO
    }

    /**
     * Метод для завершения работы приложения.
     */
    public void quit(){
        System.out.println("До скорой встречи!");
        System.exit(0);
    }
}
