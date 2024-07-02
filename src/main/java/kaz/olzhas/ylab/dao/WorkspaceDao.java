package kaz.olzhas.ylab.dao;

import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.util.ConnectionManager;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для взаимодействия с рабочими пространствами в базе данных.
 */
public interface WorkspaceDao {

    /**
     * Извлекает все рабочие пространства из базы данных.
     *
     * @return список всех рабочих пространств.
     */
    List<Workspace> findAll();

    /**
     * Находит рабочее пространство по его идентификатору.
     *
     * @param id идентификатор рабочего пространства для поиска.
     * @return Optional, содержащий найденное рабочее пространство, или пустой Optional, если рабочее пространство не найдено.
     */
    Optional<Workspace> findById(Long id);

    /**
     * Сохраняет рабочее пространство в базе данных.
     *
     * @param workspace рабочее пространство для сохранения.
     * @return true, если рабочее пространство было успешно сохранено, иначе false.
     */
    boolean save(Workspace workspace); //CASE WHEN NO
}
