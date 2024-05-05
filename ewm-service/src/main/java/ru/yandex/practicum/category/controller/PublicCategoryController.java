package ru.yandex.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.category.dto.CategoryDto;
import ru.yandex.practicum.category.service.PublicCategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
@Slf4j
public class PublicCategoryController {
    private final PublicCategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> findAll(@RequestParam(value = "from", required = false, defaultValue = "0")
                                     @PositiveOrZero(message = "Значение 'from' должно быть положительным") final Integer from,
                                     @RequestParam(value = "size", required = false, defaultValue = "10")
                                     @Positive(message = "Значение 'size' должно быть положительным") final Integer size) {
        log.info("Получение категорий");
        return categoryService.findAll(from, size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto findById(@PathVariable Long catId) {
        log.info("Получение информации о категории по ее идентификатору");
        return categoryService.findById(catId);
    }
}
