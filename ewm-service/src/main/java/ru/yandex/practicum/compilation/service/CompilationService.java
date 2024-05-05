package ru.yandex.practicum.compilation.service;

import ru.yandex.practicum.compilation.dto.CompilationDto;
import ru.yandex.practicum.compilation.dto.NewCompilationDto;
import ru.yandex.practicum.compilation.dto.UpdateCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> findAll(Boolean pinned, Integer from, Integer size);

    CompilationDto findById(Long compId);

    CompilationDto save(NewCompilationDto dto);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationDto dto);
}
