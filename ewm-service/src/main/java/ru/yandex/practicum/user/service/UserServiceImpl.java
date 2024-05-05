package ru.yandex.practicum.user.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exceptions.UserNotFoundException;
import ru.yandex.practicum.user.dto.NewUserRequest;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.mapper.UserMapper;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Data
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto addUser(NewUserRequest newUserRequest) {
        User user = UserMapper.toUser(newUserRequest);
        log.info("Пользователь {} добавлен", user.getName());
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        getExistingUser(userId);
        log.info("Пользователь с ID= {} удален", userId);
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<UserDto> getAllUsers(List<Long> ids, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from, size);
        List<UserDto> users;
        if (ids != null) {
            users = userRepository.findAllByIdIn(ids, page).stream().map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            users = userRepository.findAll(page).stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        }
        log.info("Возвращён список пользователей: {}", users);
        return users;
    }

    public User getExistingUser(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Пользователь с id " + userId + " не найден")
        );
    }
}
