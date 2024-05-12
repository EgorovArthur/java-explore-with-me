package ru.yandex.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.enums.RequestStatus;
import ru.yandex.practicum.request.model.ParticipationRequest;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    //Получаем запрос на участие в мероприятии, у которого идентификатор совпадает с `id`
    // и который был запрошен пользователем с идентификатором `requesterId`.
    ParticipationRequest findByIdAndRequesterId(long id, long requesterId);

    //Проверяет наличие участия в мероприятии, у которого идентификатор мероприятия совпадает с `eventId`
    // и которое было запрошено пользователем `requesterId`.
    boolean existsByEventIdAndRequesterId(long eventId, long requesterId);

    //Получаем все запросы на участие в мероприятии, у которых идентификатор мероприятия совпадает с  `eventId`
    List<ParticipationRequest> findAllByEventId(long eventId);

    //Получаем все запросы на участие в мероприятии, которые были запрошены пользователем `requesterId`
    List<ParticipationRequest> findAllByRequesterId(long requesterId);

    //Выводит количество запросов на участие в мероприятии, у определенного события
    long countByEventIdAndStatus(Long eventId, RequestStatus status);

    //Получаем список запросов на участие в мероприятии, определенного мероприятия и статуса.
    List<ParticipationRequest> findAllByEventIdInAndStatus(List<Long> eventsIds, RequestStatus status);

    //Получаем список всех запросов на участие в мероприятии соответствующий определенному списку запросов, мероприятия и статусу
    List<ParticipationRequest> findAllByIdInAndEventIdAndStatus(List<Long> requestIds, Long eventId,
                                                                RequestStatus status);
}
