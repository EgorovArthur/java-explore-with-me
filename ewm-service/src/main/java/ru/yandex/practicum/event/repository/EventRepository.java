package ru.yandex.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.event.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorId(Long userId, Pageable pageable); //Получение списка событий опредленного пользователя

    Event findByIdAndInitiatorId(Long eventId, Long userId); //Получение конкретного события определенного пользователя

    List<Event> findAll(Specification<Event> specification, Pageable pageable); //Получения отфильтрованного списка событий

    List<Event> findAllEventsByIdIn(List<Long> ids); //Получение списка событий с определенными идентификаторами

    List<Event> findAllByCategoryId(Long catId); // получение списка событий определенной категории
}
