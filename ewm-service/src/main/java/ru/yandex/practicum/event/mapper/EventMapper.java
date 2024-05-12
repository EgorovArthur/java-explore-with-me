package ru.yandex.practicum.event.mapper;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.event.dto.EventFullDto;
import ru.yandex.practicum.event.dto.EventShortDto;
import ru.yandex.practicum.event.dto.NewEventDto;
import ru.yandex.practicum.event.model.Event;

import static ru.yandex.practicum.category.mapper.CategoryMapper.toCategoryDto;
import static ru.yandex.practicum.user.mapper.UserMapper.toUserShortDto;

@Data
@Component
public class EventMapper {
    public static Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .title(newEventDto.getTitle())
                .annotation(newEventDto.getAnnotation())
                .eventDate(newEventDto.getEventDate())
                .description(newEventDto.getDescription())
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .title(event.getTitle())
                .eventDate(event.getEventDate())
                //.confirmedRequests(event.getConfirmedRequests())
                .category(toCategoryDto(event.getCategory()))
                .initiator(toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .category(toCategoryDto(event.getCategory()))
                .location(event.getLocation())
                .initiator(toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .build();
    }
}
