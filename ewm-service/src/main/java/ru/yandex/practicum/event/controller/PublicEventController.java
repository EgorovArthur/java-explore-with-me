package ru.yandex.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.dto.EventFullDto;
import ru.yandex.practicum.event.dto.EventShortDto;
import ru.yandex.practicum.event.service.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
@Slf4j
public class PublicEventController {
    private final PublicEventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> findAllPublishedEvents(@RequestParam(required = false) String text,
                                                      @RequestParam(required = false) List<Long> categories,
                                                      @RequestParam(required = false) Boolean paid,
                                                      @RequestParam(required = false)
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                      @RequestParam(required = false)
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                      @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                      @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                                      @RequestParam(value = "from", required = false, defaultValue = "0")
                                                      @PositiveOrZero(message = "Значение 'from' должно быть положительным") final Integer from,
                                                      @RequestParam(value = "size", required = false, defaultValue = "10")
                                                      @Positive(message = "Значение 'size' должно быть положительным") final Integer size,
                                                      HttpServletRequest request) {
        log.info("Получение событий с возможностью фильтрации");
        return eventService.findAllPublishedEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findById(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получение подробной информации об опубликованном событии по его идентификатору");
        return eventService.findById(id, request);
    }
}
