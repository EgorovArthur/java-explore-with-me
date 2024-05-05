package ru.yandex.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.enums.UserStateAction;
import ru.yandex.practicum.location.model.Location;

import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {
    @Length(min = 20, max = 2000)
    private String annotation; //Новая аннотация
    private Long category; //Новая категория
    @Length(min = 20, max = 7000)
    private String description; //Новое описание
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; //Новые дата и время на которые намечено событие.
    private Location location; //Новые Широта и долгота места проведения события
    private Boolean paid; //Новое значение флага о платности мероприятия
    @PositiveOrZero
    private Long participantLimit; //Новый лимит пользователей
    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие
    private UserStateAction stateAction; //Новое состояние события
    @Length(min = 3, max = 120)
    private String title; //Новый заголовок
}
