package ru.yandex.practicum.comment.service;

import ru.yandex.practicum.comment.dto.CommentDto;
import ru.yandex.practicum.comment.dto.NewCommentDto;

import java.util.List;

public interface PrivateCommentService {
    CommentDto save(Long userId, Long eventId, NewCommentDto dto);

    CommentDto update(Long userId, Long commentId, NewCommentDto dto);

    void delete(Long userId, Long commentId);

    List<CommentDto> findAllByAuthorId(Long userId, int from, int size);
}
