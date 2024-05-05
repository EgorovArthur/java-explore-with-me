package ru.yandex.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.compilation.dto.CompilationDto;
import ru.yandex.practicum.compilation.dto.NewCompilationDto;
import ru.yandex.practicum.compilation.dto.UpdateCompilationDto;
import ru.yandex.practicum.compilation.mapper.CompilationMapper;
import ru.yandex.practicum.compilation.model.Compilation;
import ru.yandex.practicum.compilation.repository.CompilationRepository;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.service.PublicEventServiceImpl;
import ru.yandex.practicum.exceptions.CompilationNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.compilation.mapper.CompilationMapper.toCompilation;
import static ru.yandex.practicum.compilation.mapper.CompilationMapper.toCompilationDto;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final PublicEventServiceImpl eventService;

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> findAll(Boolean pinned, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);

        return compilationRepository.findAllByPinned(pinned, page).stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto findById(Long compId) {
        Compilation compilation = getExistingCompilation(compId);

        return toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public CompilationDto save(NewCompilationDto dto) {
        Compilation compilation = toCompilation(dto);

        if (dto.getPinned() == null) {
            compilation.setPinned(false);
        }

        if (dto.getEvents() != null) {
            List<Event> events = eventService.findAllEventsWithIdIn(dto.getEvents());
            compilation.setEvents(events);
        }

        return toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        getExistingCompilation(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto update(Long compId, UpdateCompilationDto dto) {
        Compilation updated = getExistingCompilation(compId);

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            updated.setTitle(dto.getTitle());
        }

        if (dto.getPinned() != null) {
            updated.setPinned(dto.getPinned());
        }

        if (dto.getEvents() != null) {
            List<Event> events = eventService.findAllEventsWithIdIn(dto.getEvents());
            updated.setEvents(events);
        }

        return toCompilationDto(compilationRepository.save(updated));
    }

    public Compilation getExistingCompilation(long compId) {
        return compilationRepository.findById(compId).orElseThrow(
                () -> new CompilationNotFoundException("Подборка событий с id " + compId + " не найдена")
        );
    }
}
