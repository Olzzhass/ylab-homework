package kaz.olzhas.ylab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность {@code Workspace} представляет информацию о помещении в системе.
 *
 * <p>Класс содержит поля, описывающие идентификатор помещения ({@code id}) и его название ({@code name}).</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workspace {
    private Long id; // Идентификатор помещения
    private String name; // Название помещения
}
