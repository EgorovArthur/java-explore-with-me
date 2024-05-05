package ru.yandex.practicum.compilation.mapper;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.compilation.dto.CompilationDto;
import ru.yandex.practicum.compilation.dto.NewCompilationDto;
import ru.yandex.practicum.compilation.model.Compilation;
import ru.yandex.practicum.event.mapper.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .id(compilation.getId())
                .events(compilation.getEvents().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList()))
                .build();
    }

    public static Compilation toCompilation(NewCompilationDto dto) {
        return Compilation.builder()
                .pinned(dto.getPinned())
                .title(dto.getTitle())
                .events(List.of())
                .build();
    }
}
