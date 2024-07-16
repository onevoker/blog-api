package com.onevoker.blogapi.domain.services.mappers;

import com.onevoker.blogapi.domain.entities.PostEntity;
import com.onevoker.blogapi.domain.entities.UserEntity;
import com.onevoker.blogapi.dto.requests.PostRequest;
import com.onevoker.blogapi.dto.requests.UserRequest;
import com.onevoker.blogapi.dto.responses.PostResponse;
import com.onevoker.blogapi.dto.responses.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class EntityMapperService implements EntityMapper {
    @Override
    public PostResponse getPostResponse(PostEntity postEntity) {
        return new PostResponse(
                postEntity.getUserEntity().getUsername(),
                postEntity.getTitle(),
                postEntity.getContent(),
                postEntity.getPublishedAt()
        );
    }

    @Override
    public UserResponse getUserResponse(UserEntity userEntity) {
        return new UserResponse(userEntity.getUsername());
    }

    @Override
    public PostEntity getPostEntity(PostRequest postRequest) {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(postRequest.title());
        postEntity.setContent(postRequest.content());

        return postEntity;
    }

    @Override
    public UserEntity getUserEntity(UserRequest userRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRequest.username());
        userEntity.setPassword(userRequest.password());

        return userEntity;
    }
}
