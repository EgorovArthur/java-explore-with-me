package ru.yandex.practicum.mapper;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.StatDto;
import ru.yandex.practicum.model.Stat;

@Data
@Component
public class StatMapper {
    public StatDto toStatDto(Stat stat) {
        return StatDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .hits(stat.getHits())
                .build();
    }
}
