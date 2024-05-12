package ru.yandex.practicum.request.service;

import ru.yandex.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.yandex.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.yandex.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    //Создание запроса
    ParticipationRequestDto save(Long userId, Long eventId);

    //Отменить запрос
    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    //Обновление статусов запросов на мероприятия
    EventRequestStatusUpdateResult updateEventRequestsStatuses(Long userId, Long eventId,
                                                               EventRequestStatusUpdateRequest request);

    //Поиск всех запросов на другие мероприятия
    List<ParticipationRequestDto> findAllRequestsForOtherEvents(Long userId);

    //Поиск зпросов на мероприятия по Владельцам
    List<ParticipationRequestDto> findEventRequestsByOwner(Long userId, Long eventId);

}
