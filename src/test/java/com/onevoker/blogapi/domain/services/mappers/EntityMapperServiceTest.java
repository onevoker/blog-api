package com.onevoker.blogapi.domain.services.mappers;

import com.onevoker.blogapi.domain.entities.PostEntity;
import com.onevoker.blogapi.domain.entities.UserEntity;
import com.onevoker.blogapi.dto.requests.PostRequest;
import com.onevoker.blogapi.dto.requests.UserRequest;
import com.onevoker.blogapi.dto.responses.PostResponse;
import com.onevoker.blogapi.dto.responses.UserResponse;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


public class EntityMapperServiceTest {
    private final EntityMapperService entityMapperService = new EntityMapperService();

    private static final String USERNAME = "James";
    private static final String PASSWORD = "1111";
    private static final String POST_TILE = "I ate disgusting ice cream";
    private static final String POST_CONTENT = "It was so sour";

    @Test
    void testGetPostResponse() {
        OffsetDateTime timeNow = OffsetDateTime.now();

        UserEntity userEntity = getTestUserEntity();

        PostEntity postEntity = new PostEntity();
        postEntity.setUserEntity(userEntity);
        postEntity.setTitle(POST_TILE);
        postEntity.setContent(POST_CONTENT);
        postEntity.setPublishedAt(timeNow);

        PostResponse expectedPostResponse = new PostResponse(USERNAME, POST_TILE, POST_CONTENT, timeNow);
        PostResponse result = entityMapperService.getPostResponse(postEntity);

        assertThat(result).isEqualTo(expectedPostResponse);
    }

    @Test
    void testGetUserResponse() {
        UserEntity userEntity = getTestUserEntity();

        UserResponse expectedUserResponse = new UserResponse(USERNAME);
        UserResponse result = entityMapperService.getUserResponse(userEntity);

        assertThat(result).isEqualTo(expectedUserResponse);
    }

    @Test
    void testGetPostEntity() {
        PostRequest postRequest = new PostRequest(POST_TILE, POST_CONTENT);

        PostEntity expectedPostEntity = new PostEntity();
        expectedPostEntity.setTitle(POST_TILE);
        expectedPostEntity.setContent(POST_CONTENT);

        PostEntity result = entityMapperService.getPostEntity(postRequest);

        assertAll(
                () -> assertThat(result.getContent()).isEqualTo(expectedPostEntity.getContent()),
                () -> assertThat(result.getTitle()).isEqualTo(expectedPostEntity.getTitle())
        );
    }

    @Test
    void testGetUserEntity() {
        UserRequest userRequest = new UserRequest(USERNAME, PASSWORD);

        UserEntity expectedUserEntity = new UserEntity();
        expectedUserEntity.setUsername(USERNAME);
        expectedUserEntity.setPassword(PASSWORD);

        UserEntity result = entityMapperService.getUserEntity(userRequest);

        assertAll(
                () -> assertThat(result.getUsername()).isEqualTo(expectedUserEntity.getUsername()),
                () -> assertThat(result.getPassword()).isEqualTo(expectedUserEntity.getPassword())
        );
    }

    private UserEntity getTestUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(USERNAME);

        return userEntity;
    }
}
