package com.onevoker.blogapi.domain.services.interfaces;

import com.onevoker.blogapi.dto.requests.UserRequest;
import com.onevoker.blogapi.dto.responses.PostResponse;
import com.onevoker.blogapi.dto.responses.UserResponse;

import java.time.OffsetDateTime;
import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();

    UserResponse getUserById(int id);

    List<PostResponse> getUserPosts(int id);

    List<PostResponse> getUserPostsBetweenDates(int id, OffsetDateTime startDate, OffsetDateTime endDate);

    void createUser(UserRequest userRequest);

    void updateUser(int id, UserRequest userRequest);

    void deleteUser(int id);
}
