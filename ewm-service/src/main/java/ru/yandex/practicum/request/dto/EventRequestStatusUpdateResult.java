package ru.yandex.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//Результат подтверждения/отклонения заявок на участие в событии
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResult {
    List<ParticipationRequestDto> confirmedRequests; //Подтвержденные запросы
    List<ParticipationRequestDto> rejectedRequests; //Неподтвержденные запросы
}
