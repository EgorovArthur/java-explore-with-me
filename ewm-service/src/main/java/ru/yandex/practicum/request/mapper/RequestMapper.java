package ru.yandex.practicum.request.mapper;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.request.dto.ParticipationRequestDto;
import ru.yandex.practicum.request.model.ParticipationRequest;

@Data
@Component
public class RequestMapper {
    public static ParticipationRequestDto toRequestDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .created(request.getCreated())
                .status(request.getStatus())
                .build();
    }
}
