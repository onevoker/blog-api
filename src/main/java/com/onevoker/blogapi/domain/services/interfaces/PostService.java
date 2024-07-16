package com.onevoker.blogapi.domain.services.interfaces;

import com.onevoker.blogapi.dto.requests.PostRequest;
import com.onevoker.blogapi.dto.responses.PostResponse;

import java.util.List;

public interface PostService {
    List<PostResponse> getAllPosts();

    PostResponse getPostById(int id);

    void createPost(int userId, PostRequest postRequest);

    void updatePost(int id, PostRequest postRequest);

    void deletePost(int id);
}
