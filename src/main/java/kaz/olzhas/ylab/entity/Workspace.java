package kaz.olzhas.ylab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс помещения.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workspace {
    private Long id; // id номер помещения
    private String name; //имя помещения
}
