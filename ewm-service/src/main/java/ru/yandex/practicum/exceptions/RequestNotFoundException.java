package ru.yandex.practicum.exceptions;

public class RequestNotFoundException extends NotFoundException {
    public RequestNotFoundException(String message) {
        super(message);
    }
}
