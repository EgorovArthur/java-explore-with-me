package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.model.Hit;
import ru.yandex.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Hit, Long> {

    //Получает статистику просмотров для указанного списка URI в заданный период времени.
    @Query(" SELECT new ru.yandex.practicum.model.Stat(h.app, h.uri, COUNT(h.ip) AS hits " +
            "FROM Hit h" +
            "WHERE h.timestamp between ?1 AND ?2 " +
            "AND h.uri IN(?3) " +
            "GROUP BY h.app, h.uri" +
            "ORDER BY hits DESC ")
    List<Stat> getStatByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    //Получает статистику просмотров для всех URI в заданный период времени.
    @Query(" SELECT new ru.yandex.practicum.model.Stat(h.app, h.uri, COUNT(h.ip) AS hits " +
            "FROM Hit h" +
            "WHERE h.timestamp between ?1 AND ?2 " +
            "GROUP BY h.app, h.uri" +
            "ORDER BY hits DESC ")
    List<Stat> getAllStat(LocalDateTime start, LocalDateTime end);

    //Получает статистику просмотров с учетом уникальных IP-адресов для указанного списка URI в заданный период времени.
    @Query(" SELECT DISTINCT new ru.yandex.practicum.model.Stat(h.app, h.uri, COUNT(DISTINCT h.ip) AS hits " +
            "FROM Hit h" +
            "WHERE h.timestamp between ?1 AND ?2 " +
            "AND h.uri IN(?3) " +
            "GROUP BY h.app, h.uri" +
            "ORDER BY hits DESC ")
    List<Stat> getUniqueStatByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    //Получает статистику просмотров с учетом уникальных IP-адресов для всех URI в заданный период времени.
    @Query(" SELECT DISTINCT new ru.yandex.practicum.model.Stat(h.app, h.uri, COUNT(DISTINCT h.ip) AS hits " +
            "FROM Hit h" +
            "WHERE h.timestamp between ?1 AND ?2 " +
            "GROUP BY h.app, h.uri" +
            "ORDER BY hits DESC ")
    List<Stat> getUniqueAllStat(LocalDateTime start, LocalDateTime end);
}
