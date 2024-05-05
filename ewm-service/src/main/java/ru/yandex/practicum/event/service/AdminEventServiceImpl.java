package ru.yandex.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.category.service.PublicCategoryService;
import ru.yandex.practicum.enums.AdminStateAction;
import ru.yandex.practicum.event.dto.EventFullDto;
import ru.yandex.practicum.event.dto.UpdateEventAdminRequest;
import ru.yandex.practicum.event.mapper.EventMapper;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.exceptions.EventBadRequestException;
import ru.yandex.practicum.exceptions.EventConflictException;
import ru.yandex.practicum.exceptions.EventNotFoundException;
import ru.yandex.practicum.location.model.Location;
import ru.yandex.practicum.location.service.LocationService;
import ru.yandex.practicum.request.service.RequestServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.category.mapper.CategoryMapper.toCategory;
import static ru.yandex.practicum.enums.EventState.*;
import static ru.yandex.practicum.event.mapper.EventMapper.toEventFullDto;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final RequestServiceImpl requestService;
    private final PublicCategoryService categoryService;
    private final LocationService locationService;

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> findAllFullEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from,
                                                       Integer size) {
        validateStartAndEndForGetQuery(rangeStart, rangeEnd);
        PageRequest page = PageRequest.of(from / size, size);
        Specification<Event> specification = Specification.where(null);
        if (users != null && !users.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("initiator").get("id").in(users));
        }

        if (states != null && !states.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("state").as(String.class).in(states));
        }

        if (categories != null && !categories.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(categories));
        }

        if (rangeStart != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        }

        if (rangeEnd != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }
        List<EventFullDto> events = getEventsBySpecification(specification, page);
        setConfirmedRequestsForEvents(events);

        return events;
    }

    @Override
    @Transactional
    public EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest dto) {
        Event updated = eventRepository.findById(eventId).orElseThrow(
                () -> new EventNotFoundException("Событие с id " + eventId + " не найдено.")
        );
        if (dto.getStateAction() != null) {
            String state = String.valueOf(dto.getStateAction());
            updateStateByAdmin(updated, AdminStateAction.valueOf(state));
        }

        if (dto.getEventDate() != null) {
            validateDateForUpdateByAdmin(dto.getEventDate());
            updated.setEventDate(dto.getEventDate());
        }

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            updated.setTitle(dto.getTitle());
        }

        if (dto.getAnnotation() != null && !dto.getAnnotation().isBlank()) {
            updated.setAnnotation(dto.getAnnotation());
        }

        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            updated.setDescription(dto.getDescription());
        }

        if (dto.getCategory() != null) {
            Category category = toCategory(categoryService.findById(dto.getCategory()));
            updated.setCategory(category);
        }

        if (dto.getLocation() != null) {
            Location location = locationService.findByLatAndLon(dto.getLocation());
            updated.setLocation(location);
        }

        if (dto.getPaid() != null) {
            updated.setPaid(dto.getPaid());
        }

        if (dto.getParticipantLimit() != null) {
            updated.setParticipantLimit(dto.getParticipantLimit());
        }

        if (dto.getRequestModeration() != null) {
            updated.setRequestModeration(dto.getRequestModeration());
        }

        return toEventFullDto(eventRepository.save(updated));
    }


    private void validateStartAndEndForGetQuery(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        boolean startAndEndAreNotNull = rangeStart != null && rangeEnd != null;
        if (startAndEndAreNotNull && rangeStart.isAfter(rangeEnd)) {
            throw new EventBadRequestException("Начало события не может быть позже конца");
        }
    }

    private List<EventFullDto> getEventsBySpecification(Specification<Event> specification, Pageable pageable) {
        return eventRepository.findAll(specification, pageable).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    private void setConfirmedRequestsForEvents(List<EventFullDto> events) {
        for (EventFullDto event : events) {
            Long count = requestService.getCountOfConfirmedRequestsForEvent(event.getId());
            event.setConfirmedRequests(count);
        }
    }

    private void validateDateForUpdateByAdmin(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new EventBadRequestException(
                    "Дата начала изменяемого события должна быть не ранее чем за час от даты публикации"
            );
        }
    }

    private void updateStateByAdmin(Event updated, AdminStateAction state) {
        if (state.equals(AdminStateAction.PUBLISH_EVENT)) {
            if (!updated.getState().equals(PENDING)) {
                throw new EventConflictException(
                        "Событие можно публиковать, только если оно в состоянии ожидания публикации"
                );
            }

            updated.setState(PUBLISHED);
            updated.setPublishedOn(LocalDateTime.now());
        } else if (state.equals(AdminStateAction.REJECT_EVENT)) {
            if (updated.getState().equals(PUBLISHED)) {
                throw new EventConflictException("Событие можно отклонить, только если оно еще не опубликовано");
            }
            updated.setState(CANCELED);
        }
    }
}
