package kaz.olzhas.ylab.handler;

/**
 * Класс для отображения панели пользователя и управления действиями пользователя.
 */
public class UserHandler {

    /**
     * Метод для отображения панели с действиями пользователя.
     */
    public void displayUserPanel(){
        System.out.println("       Выберите действие:");
        System.out.println("1. Посмотреть мои забронированные места. ");
        System.out.println("2. Забронировать место. ");
        System.out.println("3. Удалить мой бронь. ");
        System.out.println("4. Посмотреть места по дате. ");
        System.out.println("5. Выйти из моей учетной записи. ");
    }


}
