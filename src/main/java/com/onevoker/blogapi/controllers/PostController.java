package com.onevoker.blogapi.controllers;

import com.onevoker.blogapi.domain.services.interfaces.PostService;
import com.onevoker.blogapi.dto.requests.PostRequest;
import com.onevoker.blogapi.dto.responses.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public List<PostResponse> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public PostResponse getPostById(@PathVariable int id) {
        return postService.getPostById(id);
    }

    @GetMapping("/between-dates")
    public List<PostResponse> getPostsBetweenDates(@RequestParam OffsetDateTime startDate,
                                                   @RequestParam OffsetDateTime endDate) {
        return postService.getAllPostsBetweenDates(startDate, endDate);
    }

    @PostMapping("/users/{userId}")
    public void createPost(@PathVariable int userId, @RequestBody @Validated PostRequest postRequest) {
        postService.createPost(userId, postRequest);
    }

    @PutMapping("/{id}")
    public void updatePost(@PathVariable int id, @RequestBody @Validated PostRequest postRequest) {
        postService.updatePost(id, postRequest);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable int id) {
        postService.deletePost(id);
    }
}
