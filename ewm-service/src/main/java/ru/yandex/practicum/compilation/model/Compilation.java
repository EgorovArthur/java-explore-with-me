package ru.yandex.practicum.compilation.model;

import lombok.*;
import ru.yandex.practicum.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id", nullable = false)
    private Long id;
    @Column
    private String title; //Заголовок подборки
    @Column
    private Boolean pinned; //Закреплена ли подборка на главной странице сайта
    @ManyToMany
    @JoinTable(name = "compilation_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    @ToString.Exclude
    private List<Event> events; //Список идентификаторов событий входящих в подборку
}
