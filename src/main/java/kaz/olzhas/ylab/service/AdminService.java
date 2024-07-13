package kaz.olzhas.ylab.service;

import kaz.olzhas.ylab.annotations.Auditable;
import kaz.olzhas.ylab.entity.Admin;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.entity.types.ActionType;
import kaz.olzhas.ylab.repository.AdminRepository;
import kaz.olzhas.ylab.repository.BookingRepository;
import kaz.olzhas.ylab.repository.UserRepository;
import kaz.olzhas.ylab.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Сервисный класс для администратора, обеспечивающий доступ к операциям над помещениями, пользователями и бронированиями.
 */
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final BookingRepository bookingRepository;

    /**
     * Метод для аутентификации администратора.
     *
     * @param username имя пользователя администратора
     * @param password пароль администратора
     * @return true, если аутентификация успешна, иначе false
     */
    public boolean authenticateAdmin(String username, String password){
        Optional<Admin> maybeAdmin = adminRepository.findByUsername(username);

        if(maybeAdmin.isPresent()){
            Admin admin = maybeAdmin.get();
            return admin.getAdminPassword().equals(password);
        }

        return false;
    }

    /**
     * Метод для добавления нового помещения.
     *
     * @param name имя нового помещения
     * @return true, если добавление успешно, иначе false
     */
    @Auditable(actionType = ActionType.ADD_WORKSPACE)
    public boolean addWorkspace(String name){

        Workspace workspace = new Workspace();
        workspace.setName(name);

        return workspaceRepository.save(workspace);

    }

    /**
     * Метод для просмотра всех помещений.
     *
     * @return список всех помещений
     */
    @Auditable(actionType = ActionType.SHOW_ALL_WORKSPACES)
    public List<Workspace> showAllWorkspaces(){

        return workspaceRepository.findAll();

    }

    /**
     * Метод для просмотра всех пользователей.
     *
     * @return список всех пользователей
     */
    @Auditable(actionType = ActionType.SHOW_ALL_USERS)
    public List<User> showAllUsers(){

        return userRepository.findAll();

    }

    /**
     * Метод для просмотра всех пользователей без вывода пароля.
     */
    public void seeAllUsersWithoutPassword(){

        List<User> users = userRepository.findAll();
        if(users.size() == 0){
            System.out.println("Пока что пользователей нет.");
        }else{
            System.out.println("ВСЕ ПОЛЬЗОВАТЕЛИ:");
            for(User user : users){
                System.out.println("Username: " + user.getUsername());
            }
        }

    }

    /**
     * Метод для просмотра всех бронирований по определенному помещению.
     *
     * @param workspaceId идентификатор помещения
     * @return список бронирований для указанного помещения
     */
    @Auditable(actionType = ActionType.BOOK_WORKSPACE)
    public List<Booking> bookingsByWorkspace(Long workspaceId){

        return bookingRepository.getByWorkspaceId(workspaceId);

    }

    /**
     * Метод для просмотра всех бронирований по определенному пользователю.
     *
     * @param username имя пользователя
     * @return список бронирований для указанного пользователя
     */
    @Auditable(actionType = ActionType.BOOKINGS_BY_USER)
    public List<Booking> bookingsByUser(String username){

        Optional<User> maybeUser = userRepository.findByUsername(username);

        return bookingRepository.getByUserId(maybeUser.get().getId());


    }
}
