package ru.yandex.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.user.dto.NewUserRequest;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserController {
    private final UserService userService;

    //Создание пользователя
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        return userService.addUser(newUserRequest);
    }

    //Удаление пользователя по ID
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    //Получение всех пользователей
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> getAllUsers(@RequestParam(value = "ids", required = false) List<Long> ids,
                                           @RequestParam(value = "from", required = false, defaultValue = "0")
                                           @PositiveOrZero(message = "Значение 'from' должно быть положительным") final Integer from,
                                           @RequestParam(value = "size", required = false, defaultValue = "10")
                                           @Positive(message = "Значение 'size' должно быть положительным") final Integer size) {
        return userService.getAllUsers(ids, from, size);
    }
}
