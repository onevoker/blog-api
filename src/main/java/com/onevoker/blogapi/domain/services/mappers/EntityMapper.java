package com.onevoker.blogapi.domain.services.mappers;

import com.onevoker.blogapi.domain.entities.PostEntity;
import com.onevoker.blogapi.domain.entities.UserEntity;
import com.onevoker.blogapi.dto.requests.PostRequest;
import com.onevoker.blogapi.dto.requests.UserRequest;
import com.onevoker.blogapi.dto.responses.PostResponse;
import com.onevoker.blogapi.dto.responses.UserResponse;

public interface EntityMapper {
    PostResponse getPostResponse(PostEntity postEntity);

    UserResponse getUserResponse(UserEntity userEntity);

    PostEntity getPostEntity(PostRequest postRequest);

    UserEntity getUserEntity(UserRequest userRequest);
}
