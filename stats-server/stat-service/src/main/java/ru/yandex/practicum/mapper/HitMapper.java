package ru.yandex.practicum.mapper;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.HitDto;
import ru.yandex.practicum.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Component
public class HitMapper {
    public Hit toHit(HitDto hitDto) {
        return Hit.builder()
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .timestamp(LocalDateTime.parse(hitDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
