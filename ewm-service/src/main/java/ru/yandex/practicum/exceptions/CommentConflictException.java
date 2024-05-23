package ru.yandex.practicum.exceptions;

public class CommentConflictException extends ConflictException {
    public CommentConflictException(String message) {
        super(message);
    }
}