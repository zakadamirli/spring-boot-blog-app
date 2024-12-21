package com.zekademirli.questapp.controllers;

import com.zekademirli.questapp.entities.User;
import com.zekademirli.questapp.exceptions.UserNotFoundException;
import com.zekademirli.questapp.responses.UserResponse;
import com.zekademirli.questapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> getAll() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveOneUser(user);
    }

    @GetMapping("/{userId}")
    public UserResponse getOneUser(@PathVariable Long userId) {
        User user = userService.getOneUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return new UserResponse(user);
    }

    @PutMapping("/{userId}")
    public User updateUser(@RequestBody User user, @PathVariable Long userId) {
        return userService.updateOneUser(user, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteById(userId);
    }

    @GetMapping("/activity/{userId}")
    public List<Object> getUserActivity(@PathVariable Long userId) {
        return userService.getUserActivity(userId);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void handleUserNotFoundException() {

    }
}