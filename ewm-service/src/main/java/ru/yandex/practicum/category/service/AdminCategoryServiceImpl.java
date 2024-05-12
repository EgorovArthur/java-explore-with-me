package ru.yandex.practicum.category.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.category.dto.CategoryDto;
import ru.yandex.practicum.category.dto.NewCategoryDto;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.category.repository.CategoryRepository;
import ru.yandex.practicum.event.service.PublicEventServiceImpl;
import ru.yandex.practicum.exceptions.CategoryConflictException;
import ru.yandex.practicum.exceptions.CategoryNotFoundException;

import static ru.yandex.practicum.category.mapper.CategoryMapper.toCategory;
import static ru.yandex.practicum.category.mapper.CategoryMapper.toCategoryDto;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final PublicEventServiceImpl eventService;

    @Autowired
    public AdminCategoryServiceImpl(CategoryRepository categoryRepository, @Lazy PublicEventServiceImpl eventService) {
        this.categoryRepository = categoryRepository;
        this.eventService = eventService;
    }

    @Override
    @Transactional
    public CategoryDto save(NewCategoryDto dto) {
        return toCategoryDto(categoryRepository.save(toCategory(dto)));
    }

    @Override
    @Transactional
    public void delete(Long catId) {
        getExistingCategory(catId);
        if (eventService.getCountOfEventsByCategory(catId) > 0) {
            throw new CategoryConflictException("Нельзя удалить категорию с привязанными к ней событиями");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryDto update(Long catId, NewCategoryDto dto) {
        Category category = getExistingCategory(catId);
        updateCategoryName(category, dto.getName());
        category = categoryRepository.save(category);
        return toCategoryDto(category);
    }

    public Category getExistingCategory(long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new CategoryNotFoundException("Категория с id " + catId + " не найдена")
        );
    }

    private void updateCategoryName(Category category, String name) {
        if (name != null) {
            category.setName(name);
        }
    }
}
