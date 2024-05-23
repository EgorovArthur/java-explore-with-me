package ru.yandex.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.comment.dto.CommentDto;
import ru.yandex.practicum.comment.dto.NewCommentDto;
import ru.yandex.practicum.comment.mapper.CommentMapper;
import ru.yandex.practicum.comment.model.Comment;
import ru.yandex.practicum.comment.repository.CommentRepository;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.service.PrivateEventServiceImpl;
import ru.yandex.practicum.exceptions.CommentBadRequestException;
import ru.yandex.practicum.exceptions.CommentNotFoundException;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.comment.mapper.CommentMapper.toComment;
import static ru.yandex.practicum.comment.mapper.CommentMapper.toCommentDto;

@RequiredArgsConstructor
@Service
public class PrivateCommentServiceImpl implements PrivateCommentService {
    private final CommentRepository commentRepository;
    private final UserServiceImpl userService;
    private final PrivateEventServiceImpl eventService;

    @Override
    public CommentDto save(Long userId, Long eventId, NewCommentDto dto) {
        User user = userService.getExistingUser(userId);
        Event event = eventService.getExistingEvent(eventId);
        eventService.validateEventToAddComment(event);

        Comment comment = toComment(dto);
        comment.setCreated(LocalDateTime.now());
        comment.setEvent(event);
        comment.setAuthor(user);
        return toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto update(Long userId, Long commentId, NewCommentDto dto) {
        Comment updated = getExistingComment(commentId);
        validateAuthorOfComment(updated, userId);
        updated.setText(dto.getText());

        return toCommentDto(commentRepository.save(updated));
    }

    @Override
    @Transactional
    public void delete(Long userId, Long commentId) {
        Comment comment = getExistingComment(commentId);
        validateAuthorOfComment(comment, userId);

        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAllByAuthorId(Long userId, int from, int size) {
        userService.getExistingUser(userId);
        PageRequest page = PageRequest.of(from / size, size);

        return commentRepository.findAllByAuthorId(userId, page).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public Comment getExistingComment(long id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new CommentNotFoundException("Комментарий с id " + id + " не найден.")
        );
    }

    private void validateAuthorOfComment(Comment comment, Long userId) {
        userService.getExistingUser(userId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new CommentBadRequestException("Комментарий может изменить только его создатель");
        }
    }
}
