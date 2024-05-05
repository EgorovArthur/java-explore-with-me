package ru.yandex.practicum.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id; //уникальный идентификатор пользователя
    @Column(name = "name", nullable = false, unique = true, length = 256)
    private String name; //имя или логин пользователя
    @Column(name = "email", nullable = false, unique = true, length = 256)
    @Email
    private String email; //адрес эл.почты
}
