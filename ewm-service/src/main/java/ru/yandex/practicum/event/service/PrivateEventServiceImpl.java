package ru.yandex.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.category.service.PublicCategoryService;
import ru.yandex.practicum.enums.EventState;
import ru.yandex.practicum.enums.UserStateAction;
import ru.yandex.practicum.event.dto.EventFullDto;
import ru.yandex.practicum.event.dto.EventShortDto;
import ru.yandex.practicum.event.dto.NewEventDto;
import ru.yandex.practicum.event.dto.UpdateEventUserRequest;
import ru.yandex.practicum.event.mapper.EventMapper;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.exceptions.EventBadRequestException;
import ru.yandex.practicum.exceptions.EventConflictException;
import ru.yandex.practicum.exceptions.EventNotFoundException;
import ru.yandex.practicum.location.model.Location;
import ru.yandex.practicum.location.service.LocationService;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.category.mapper.CategoryMapper.toCategory;
import static ru.yandex.practicum.enums.EventState.*;
import static ru.yandex.practicum.enums.UserStateAction.CANCEL_REVIEW;
import static ru.yandex.practicum.enums.UserStateAction.SEND_TO_REVIEW;
import static ru.yandex.practicum.event.mapper.EventMapper.toEvent;
import static ru.yandex.practicum.event.mapper.EventMapper.toEventFullDto;

@Service
@RequiredArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {
    private final UserServiceImpl userService;
    private final EventRepository eventRepository;
    private final PublicCategoryService categoryService;
    private final LocationService locationService;

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> findUserEvents(Long userId, Integer from, Integer size) {
        userService.getExistingUser(userId);
        Pageable pageable = PageRequest.of(from / size, size);

        List<EventShortDto> result = eventRepository.findAllByInitiatorId(userId, pageable).stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());

        result = result.isEmpty() ? new ArrayList<>() : result;
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto findUserFullEvent(Long userId, Long eventId) {
        userService.getExistingUser(userId);
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        if (event == null) {
            throw new EventNotFoundException("Событие с id " + eventId + " не найдено.");
        }
        return toEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto save(Long userId, NewEventDto dto) {
        User user = userService.getExistingUser(userId);
        validateDateForUpdateAndCreateByUser(dto.getEventDate());
        Event event = toEvent(dto);
        setPropertiesWhenCreating(user, dto, event);
        return toEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto updateByUser(Long userId, Long eventId, UpdateEventUserRequest dto) {
        userService.getExistingUser(userId);
        Event updated = getExistingEvent(eventId);
        if (!updated.getInitiator().getId().equals(userId)) {
            throw new EventBadRequestException("Данные события может изменить только его организатор");
        }
        validateStateForUpdateByUser(updated.getState());
        updateState(dto.getStateAction(), updated);

        if (dto.getEventDate() != null) {
            validateDateForUpdateAndCreateByUser(dto.getEventDate());
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

    private void validateDateForUpdateAndCreateByUser(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventBadRequestException(
                    "Дата начала изменяемого события должна быть не ранее чем за 2 часа от даты публикации"
            );
        }
    }

    private void setPropertiesWhenCreating(User user, NewEventDto dto, Event event) {
        Category category = toCategory(categoryService.findById(dto.getCategory()));
        Location location = locationService.findByLatAndLon(dto.getLocation());

        event.setInitiator(user);
        event.setCategory(category);
        event.setLocation(location);
        event.setState(PENDING);
        event.setCreatedOn(LocalDateTime.now());
    }

    public Event getExistingEvent(long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new EventNotFoundException("Событие с id " + id + " не найдено.")
        );
    }

    private void validateStateForUpdateByUser(EventState state) {
        if (state.equals(PUBLISHED)) {
            throw new EventConflictException(
                    "Изменить можно только отмененные события или события в состоянии ожидания модерации"
            );
        }
    }

    private void updateState(UserStateAction state, Event event) {
        if (state != null) {
            if (state.equals(SEND_TO_REVIEW)) {
                event.setState(PENDING);
            } else if (state.equals(CANCEL_REVIEW)) {
                event.setState(CANCELED);
            }
        }
    }
}
