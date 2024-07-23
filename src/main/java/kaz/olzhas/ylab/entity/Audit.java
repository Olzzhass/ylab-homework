package kaz.olzhas.ylab.entity;

import kaz.olzhas.ylab.entity.types.ActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Сущность {@code Audit} представляет запись аудита действий пользователей в системе.
 *
 * <p>Класс содержит информацию о пользователе, выполнившем действие ({@code username}), типе действия ({@code actionType}),
 * и времени выполнения аудита ({@code auditTime}).</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Audit {
    private Long id;
    private String username;
    private ActionType actionType;
    private LocalDateTime auditTime;
}
