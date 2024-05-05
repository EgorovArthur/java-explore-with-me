package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.HitDto;
import ru.yandex.practicum.StatDto;
import ru.yandex.practicum.exception.BadRequestException;
import ru.yandex.practicum.mapper.HitMapper;
import ru.yandex.practicum.mapper.StatMapper;
import ru.yandex.practicum.model.Stat;
import ru.yandex.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;
    private final HitMapper hitMapper;
    private final StatMapper statMapper;

    @Override
    @Transactional
    public void addHit(HitDto hitDto) {
        statRepository.save(hitMapper.toHit(hitDto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)) {
            throw new BadRequestException("Время окончания должно быть позднее времени начала!");
        }
        List<Stat> stats;
        if (unique) {
            if (uris != null) {
                stats = statRepository.getUniqueStatByUris(start, end, uris);
            } else {
                stats = statRepository.getUniqueAllStat(start, end);
            }
        } else {
            if (uris != null) {
                stats = statRepository.getStatByUris(start, end, uris);
            } else {
                stats = statRepository.getAllStat(start, end);
            }
        }
        return stats.stream().map(statMapper::toStatDto).collect(Collectors.toList());
    }
}
