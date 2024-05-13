package ru.yandex.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.comment.dto.CommentDto;
import ru.yandex.practicum.comment.mapper.CommentMapper;
import ru.yandex.practicum.comment.model.Comment;
import ru.yandex.practicum.comment.repository.CommentRepository;
import ru.yandex.practicum.event.service.PrivateEventServiceImpl;
import ru.yandex.practicum.exceptions.CommentNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.comment.mapper.CommentMapper.toCommentDto;

@Service
@RequiredArgsConstructor
public class PublicCommentServiceImpl implements PublicCommentService {
    private final CommentRepository commentRepository;
    private final PrivateEventServiceImpl eventService;

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAllByEventId(Long eventId, int from, int size) {
        eventService.getExistingEvent(eventId);
        PageRequest page = PageRequest.of(from / size, size);
        return commentRepository.findAllByEventId(eventId, page).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto findById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException("Комментарий с id " + commentId + " не найден.")
        );
        return toCommentDto(comment);
    }
}
