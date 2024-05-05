package ru.yandex.practicum.user.service;

import ru.yandex.practicum.user.dto.NewUserRequest;
import ru.yandex.practicum.user.dto.UserDto;

import java.util.Collection;
import java.util.List;

public interface UserService {
    UserDto addUser(NewUserRequest newUserRequest);

    void deleteUser(Long userId);

    Collection<UserDto> getAllUsers(List<Long> ids, Integer from, Integer size);
}
