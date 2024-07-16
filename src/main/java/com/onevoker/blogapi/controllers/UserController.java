package com.onevoker.blogapi.controllers;

import com.onevoker.blogapi.domain.services.interfaces.UserService;
import com.onevoker.blogapi.dto.requests.UserRequest;
import com.onevoker.blogapi.dto.responses.PostResponse;
import com.onevoker.blogapi.dto.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/posts")
    public List<PostResponse> getUserPosts(@PathVariable int id) {
        return userService.getUserPosts(id);
    }

    @GetMapping("/{id}/posts/between-dates")
    public List<PostResponse> getUserPostsBetweenDates(@PathVariable int id,
                                                       @RequestParam OffsetDateTime startDate,
                                                       @RequestParam OffsetDateTime endDate
    ) {
        return userService.getUserPostsBetweenDates(id, startDate, endDate);
    }

    @PostMapping
    public void createUser(@RequestBody @Validated UserRequest userRequest) {
        userService.createUser(userRequest);
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable int id, @Validated @RequestBody UserRequest userRequest) {
        userService.updateUser(id, userRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }
}
