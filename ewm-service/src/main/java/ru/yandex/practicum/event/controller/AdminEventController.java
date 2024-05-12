package ru.yandex.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.dto.EventFullDto;
import ru.yandex.practicum.event.dto.UpdateEventAdminRequest;
import ru.yandex.practicum.event.service.AdminEventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
@Slf4j
public class AdminEventController {
    private final AdminEventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> findAllFullEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                                       @RequestParam(required = false) List<String> states,
                                                       @RequestParam(required = false) List<Long> categories,
                                                       @RequestParam(required = false)
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                       LocalDateTime rangeStart,
                                                       @RequestParam(required = false)
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                       LocalDateTime rangeEnd,
                                                       @RequestParam(value = "from", required = false, defaultValue = "0")
                                                       @PositiveOrZero(message = "Значение 'from' должно быть положительным") final Integer from,
                                                       @RequestParam(value = "size", required = false, defaultValue = "10")
                                                       @Positive(message = "Значение 'size' должно быть положительным") final Integer size) {
        log.info("Поиск событий");
        return eventService.findAllFullEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateByAdmin(@PathVariable Long eventId, @RequestBody @Valid UpdateEventAdminRequest dto) {
        log.info("Редактирование данных события и его статуса (отклонение/публикация)");

        return eventService.updateByAdmin(eventId, dto);
    }
}
