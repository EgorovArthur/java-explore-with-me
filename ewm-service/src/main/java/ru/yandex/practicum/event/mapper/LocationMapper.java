package ru.yandex.practicum.event.mapper;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.event.dto.LocationDto;
import ru.yandex.practicum.location.model.Location;

@Data
@Component
public class LocationMapper {
    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public static Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }
}
