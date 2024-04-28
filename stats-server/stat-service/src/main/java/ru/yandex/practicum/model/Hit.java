package ru.yandex.practicum.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hits")

public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String app; //Идентификатор сервиса для которого записывается информация
    @Column
    private String uri; //URI для которого был осуществлен запрос
    @Column(nullable = false)
    private String ip; //IP-адрес пользователя, осуществившего запрос
    @Column(nullable = false)
    private LocalDateTime timestamp; //Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
}
