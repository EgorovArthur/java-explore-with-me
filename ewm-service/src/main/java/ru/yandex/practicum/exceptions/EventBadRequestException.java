package ru.yandex.practicum.exceptions;

public class EventBadRequestException extends BadRequestException {
    public EventBadRequestException(String message) {
        super(message);
    }
}
