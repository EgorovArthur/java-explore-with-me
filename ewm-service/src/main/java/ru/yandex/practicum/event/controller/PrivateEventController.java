package ru.yandex.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.dto.EventFullDto;
import ru.yandex.practicum.event.dto.EventShortDto;
import ru.yandex.practicum.event.dto.NewEventDto;
import ru.yandex.practicum.event.dto.UpdateEventUserRequest;
import ru.yandex.practicum.event.service.PrivateEventService;
import ru.yandex.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.yandex.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.yandex.practicum.request.dto.ParticipationRequestDto;
import ru.yandex.practicum.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
public class PrivateEventController {
    private final PrivateEventService eventService;
    private final RequestService requestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> findUserEvents(@PathVariable Long userId,
                                              @RequestParam(value = "from", required = false, defaultValue = "0")
                                              @PositiveOrZero(message = "Значение 'from' должно быть положительным") final Integer from,
                                              @RequestParam(value = "size", required = false, defaultValue = "10")
                                              @Positive(message = "Значение 'size' должно быть положительным") final Integer size) {
        log.info("Получение событий, добавленных текущим пользователем");
        return eventService.findUserEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto save(@PathVariable Long userId, @RequestBody @Valid NewEventDto dto) {
        log.info("Добавление нового события");
        return eventService.save(userId, dto);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findUserFullEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получение полной информации о событии добавленном текущим пользователем");

        return eventService.findUserFullEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateByUser(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody @Valid UpdateEventUserRequest dto) {
        log.info("Изменение события добавленного текущим пользователем");

        return eventService.updateByUser(userId, eventId, dto);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> findEventRequestsByOwner(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получение информации о запросах на участие в событии текущего пользователя");
        return requestService.findEventRequestsByOwner(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateEventRequestsStatuses(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя");
        return requestService.updateEventRequestsStatuses(userId, eventId, request);
    }


}
