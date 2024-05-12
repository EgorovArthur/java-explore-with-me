package ru.yandex.practicum.exceptions;

public class EventConflictException extends ConflictException {
    public EventConflictException(String message) {
        super(message);
    }
}
