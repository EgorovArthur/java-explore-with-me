package ru.yandex.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.compilation.dto.CompilationDto;
import ru.yandex.practicum.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
@Slf4j
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> findAll(@RequestParam(defaultValue = "false") Boolean pinned,
                                        @RequestParam(value = "from", required = false, defaultValue = "0")
                                        @PositiveOrZero(message = "Значение 'from' должно быть положительным") final Integer from,
                                        @RequestParam(value = "size", required = false, defaultValue = "10")
                                        @Positive(message = "Значение 'size' должно быть положительным") final Integer size) {
        log.info("Получение подборок событий");
        return compilationService.findAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto findById(@PathVariable Long compId) {
        log.info("Получение подборки событий по ее идентификатору");
        return compilationService.findById(compId);
    }

}

