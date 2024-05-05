package ru.yandex.practicum.category.service;

import ru.yandex.practicum.category.dto.CategoryDto;
import ru.yandex.practicum.category.dto.NewCategoryDto;

public interface AdminCategoryService {
    CategoryDto save(NewCategoryDto dto);

    void delete(Long catId);

    CategoryDto update(Long catId, NewCategoryDto dto);
}
