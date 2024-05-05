package ru.yandex.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.enums.RequestStatus;

import java.util.List;

//Изменение статуса запроса на участие в событии текущего пользователя
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds; //Идентификаторы запросов на участие в событии текущего пользователя
    RequestStatus status; //Новый статус запроса на участие в событии текущего пользователя
}
