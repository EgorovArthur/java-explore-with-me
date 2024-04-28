package ru.yandex.practicum;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatDto {
    private String app;
    private String uri;
    private long hits;
}
