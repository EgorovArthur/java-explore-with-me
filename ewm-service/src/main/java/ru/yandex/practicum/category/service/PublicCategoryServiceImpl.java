package ru.yandex.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.category.dto.CategoryDto;
import ru.yandex.practicum.category.mapper.CategoryMapper;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.category.repository.CategoryRepository;
import ru.yandex.practicum.exceptions.CategoryNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.category.mapper.CategoryMapper.toCategoryDto;

@Service
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements PublicCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> findAll(int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);
        return categoryRepository.findAll(page)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto findById(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(
                () -> new CategoryNotFoundException("Категория с id " + catId + " не найдена"));
        return toCategoryDto(category);
    }
}
