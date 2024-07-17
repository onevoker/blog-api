package com.onevoker.blogapi.domain.services;

import com.onevoker.blogapi.IntegrationTest;
import com.onevoker.blogapi.domain.entities.PostEntity;
import com.onevoker.blogapi.domain.entities.UserEntity;
import com.onevoker.blogapi.domain.repositories.PostEntityRepository;
import com.onevoker.blogapi.domain.repositories.UserEntityRepository;
import com.onevoker.blogapi.dto.requests.PostRequest;
import com.onevoker.blogapi.dto.responses.PostResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
public class PostServiceImplIT extends IntegrationTest {
    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private PostEntityRepository postEntityRepository;

    private static final String POST_TILE = "I ate disgusting ice cream";
    private static final String POST_CONTENT = "It was so sour!";
    private static final String USERNAME = "James";
    private static final String PASSWORD = "1111";

    private static final PostResponse EXPECTED_RESPONSE = new PostResponse(
            USERNAME,
            POST_TILE,
            POST_CONTENT,
            OffsetDateTime.now()
    );

    private PostEntity postEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setUsername(USERNAME);
        userEntity.setPassword(PASSWORD);
        userEntityRepository.save(userEntity);

        postEntity = new PostEntity();
        postEntity.setTitle(POST_TILE);
        postEntity.setContent(POST_CONTENT);
        postEntity.setUserEntity(userEntity);
        postEntityRepository.save(postEntity);
        userEntity.getPosts().add(postEntity);
    }

    @Test
    void testGetAllPosts() {
        PostResponse result = postService.getAllPosts().getFirst();

        assertThat(postResponsesAreEqualsIgnoringDate(result, EXPECTED_RESPONSE)).isTrue();
    }

    @Test
    void testGetPostById() {
        PostResponse result = postService.getPostById(postEntity.getId());

        assertThat(postResponsesAreEqualsIgnoringDate(result, EXPECTED_RESPONSE)).isTrue();
    }

    @Test
    void testCreatePost() {
        String newTitle = "New title";
        String newContent = "New content";
        PostRequest postRequest = new PostRequest(newTitle, newContent);

        PostResponse newPost = new PostResponse(USERNAME, newTitle, newContent, OffsetDateTime.now());

        postService.createPost(userEntity.getId(), postRequest);
        List<PostResponse> allPostResponses = postService.getAllPosts();

        assertAll(
                () -> assertThat(allPostResponses).hasSize(2),
                () -> assertThat(allPostResponses)
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("publishedAt")
                        .contains(newPost)
        );
    }

    @Test
    void testUpdatePost() {
        String newTitle = "Woops";
        String newContent = "Woops content";
        PostRequest updatePostRequest = new PostRequest(newTitle, newContent);

        postService.updatePost(postEntity.getId(), updatePostRequest);

        PostResponse updatedPostResponse = postService.getAllPosts().getFirst();
        PostResponse expected = new PostResponse(USERNAME, newTitle, newContent, OffsetDateTime.now());

        assertThat(postResponsesAreEqualsIgnoringDate(updatedPostResponse, expected)).isTrue();
    }

    @Test
    void testDeletePost() {
        postService.deletePost(postEntity.getId());

        PostEntity deletedPost = postEntityRepository.findById(postEntity.getId()).orElse(null);

        assertThat(deletedPost).isNull();
    }

    private boolean postResponsesAreEqualsIgnoringDate(PostResponse result, PostResponse expected) {
        return result.username().equals(expected.username())
                && result.title().equals(expected.title())
                && result.content().equals(expected.content());
    }
}
