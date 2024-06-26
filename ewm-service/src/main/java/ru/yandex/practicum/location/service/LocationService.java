package ru.yandex.practicum.location.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.location.model.Location;
import ru.yandex.practicum.location.repository.LocationRepository;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public Location findByLatAndLon(Location location) {
        if (locationRepository.existsByLatAndLon(location.getLat(), location.getLon())) {
            return locationRepository.findByLatAndLon(location.getLat(), location.getLon());
        } else {
            return locationRepository.save(location);
        }
    }
}
