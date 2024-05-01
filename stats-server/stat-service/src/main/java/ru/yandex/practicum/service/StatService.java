package ru.yandex.practicum.service;

import ru.yandex.practicum.HitDto;
import ru.yandex.practicum.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void addHit(HitDto hitDto);

    List<StatDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
