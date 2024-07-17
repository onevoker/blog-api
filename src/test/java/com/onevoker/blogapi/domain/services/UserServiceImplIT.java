package com.onevoker.blogapi.domain.services;

import com.onevoker.blogapi.IntegrationTest;
import com.onevoker.blogapi.domain.entities.PostEntity;
import com.onevoker.blogapi.domain.entities.UserEntity;
import com.onevoker.blogapi.domain.repositories.PostEntityRepository;
import com.onevoker.blogapi.domain.repositories.UserEntityRepository;
import com.onevoker.blogapi.dto.requests.UserRequest;
import com.onevoker.blogapi.dto.responses.PostResponse;
import com.onevoker.blogapi.dto.responses.UserResponse;
import com.onevoker.blogapi.exceptions.DuplicateUsernameException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
public class UserServiceImplIT extends IntegrationTest {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private PostEntityRepository postEntityRepository;

    private static final String USERNAME = "James";
    private static final String PASSWORD = "1111";
    private static final String POST_TILE = "I ate disgusting ice cream";
    private static final String POST_CONTENT = "It was so sour!";
    private static final UserResponse EXIST_USER = new UserResponse(USERNAME);
    private static final String USERNAME_IN_USE_MESSAGE = "This username already in use, try another one";

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setUsername(USERNAME);
        userEntity.setPassword(PASSWORD);
        userEntityRepository.save(userEntity);

        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(POST_TILE);
        postEntity.setContent(POST_CONTENT);
        postEntity.setUserEntity(userEntity);
        postEntityRepository.save(postEntity);
        userEntity.getPosts().add(postEntity);
    }

    @Test
    void testGetAllUsers() {
        List<UserResponse> result = userService.getAllUsers();

        assertThat(result).contains(EXIST_USER);
    }

    @Test
    void testGetUserById() {
        UserResponse result = userService.getUserById(userEntity.getId());

        assertThat(result).isEqualTo(EXIST_USER);
    }

    @Test
    void testGetUserPosts() {
        List<PostResponse> posts = userService.getUserPosts(userEntity.getId());
        PostResponse post = posts.getFirst();

        assertAll(
                () -> assertThat(post.username()).isEqualTo(USERNAME),
                () -> assertThat(post.title()).isEqualTo(POST_TILE),
                () -> assertThat(post.content()).isEqualTo(POST_CONTENT)
        );
    }

    @Test
    void testGetUserPostsBetweenDates() {
        OffsetDateTime startDate = OffsetDateTime.now().minusDays(1);
        OffsetDateTime endDate = OffsetDateTime.now().plusDays(1);

        List<PostResponse> posts = userService.getUserPostsBetweenDates(userEntity.getId(), startDate, endDate);
        PostResponse post = posts.getFirst();

        assertAll(
                () -> assertThat(post.username()).isEqualTo(USERNAME),
                () -> assertThat(post.title()).isEqualTo(POST_TILE),
                () -> assertThat(post.content()).isEqualTo(POST_CONTENT)
        );
    }

    @Test
    void testCreateUser() {
        String username = "Norm";
        String password = "999";
        UserRequest userRequest = new UserRequest(username, password);

        userService.createUser(userRequest);

        List<UserEntity> allUsers = userEntityRepository.findAll();

        assertAll(
                () -> assertThat(allUsers).hasSize(2),
                () -> assertThat(allUsers.get(1).getUsername()).isEqualTo(username)
        );
    }

    @Test
    void testCreateUserThrowsDuplicateUsernameException() {
        assertThatThrownBy(() -> userService.createUser(new UserRequest(USERNAME, PASSWORD)))
                .isInstanceOf(DuplicateUsernameException.class)
                .hasMessage(USERNAME_IN_USE_MESSAGE);
    }

    @Test
    void testUpdateUser() {
        String newUsername = "RealMan2005";
        String newPassword = "8888";
        UserRequest userRequest = new UserRequest(newUsername, newPassword);

        userService.updateUser(userEntity.getId(), userRequest);
        UserEntity existUser = userEntityRepository.findAll().getFirst();

        assertAll(
                () -> assertThat(existUser.getUsername()).isEqualTo(newUsername),
                () -> assertThat(existUser.getPassword()).isEqualTo(newPassword)
        );
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(userEntity.getId());

        List<UserEntity> allUsers = userEntityRepository.findAll();

        assertThat(allUsers).isEmpty();
    }
}
