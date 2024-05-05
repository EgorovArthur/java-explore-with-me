package ru.yandex.practicum.category.service;

import ru.yandex.practicum.category.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {
    List<CategoryDto> findAll(int from, int size);

    CategoryDto findById(Long catId);
}
