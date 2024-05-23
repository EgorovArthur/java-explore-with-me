package ru.yandex.practicum.comment.service;

import ru.yandex.practicum.comment.dto.CommentDto;

import java.util.List;

public interface PublicCommentService {
    List<CommentDto> findAllByEventId(Long eventId, int from, int size);

    CommentDto findById(Long commentId);
}
