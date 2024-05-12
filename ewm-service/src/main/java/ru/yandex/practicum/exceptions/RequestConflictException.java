package ru.yandex.practicum.exceptions;

public class RequestConflictException extends ConflictException {
    public RequestConflictException(String message) {
        super(message);
    }
}
