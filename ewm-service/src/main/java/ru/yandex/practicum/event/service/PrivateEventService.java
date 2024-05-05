package ru.yandex.practicum.event.service;

import ru.yandex.practicum.event.dto.EventFullDto;
import ru.yandex.practicum.event.dto.EventShortDto;
import ru.yandex.practicum.event.dto.NewEventDto;
import ru.yandex.practicum.event.dto.UpdateEventUserRequest;

import java.util.List;

public interface PrivateEventService {
    List<EventShortDto> findUserEvents(Long userId, Integer from, Integer size);

    EventFullDto findUserFullEvent(Long userId, Long eventId);

    EventFullDto save(Long userId, NewEventDto dto);

    EventFullDto updateByUser(Long userId, Long eventId, UpdateEventUserRequest dto);
}
