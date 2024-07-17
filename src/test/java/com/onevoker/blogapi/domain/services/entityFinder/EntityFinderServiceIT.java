package com.onevoker.blogapi.domain.services.entityFinder;

import com.onevoker.blogapi.IntegrationTest;
import com.onevoker.blogapi.domain.entities.PostEntity;
import com.onevoker.blogapi.domain.entities.UserEntity;
import com.onevoker.blogapi.domain.repositories.PostEntityRepository;
import com.onevoker.blogapi.domain.repositories.UserEntityRepository;
import com.onevoker.blogapi.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
public class EntityFinderServiceIT extends IntegrationTest {
    @Autowired
    private EntityFinderService entityFinderService;

    @Autowired
    private PostEntityRepository postEntityRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    private UserEntity user;
    private PostEntity post;

    private static final String USERNAME = "James";
    private static final String PASSWORD = "1111";
    private static final String POST_TILE = "I ate disgusting ice cream";
    private static final String POST_CONTENT = "It was so sour!";

    private static final int NOT_EXIST_USER_ID = 999;
    private static final int NOT_EXIST_POST_ID = 1000;

    private static final String POST_NOT_FOUND_MESSAGE = "This post does not exist";
    private static final String USER_NOT_FOUND_MESSAGE = "This user does not exist";

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        userEntityRepository.save(user);

        post = new PostEntity();
        post.setTitle(POST_TILE);
        post.setContent(POST_CONTENT);
        post.setUserEntity(user);
        postEntityRepository.save(post);
    }

    @Test
    void testGetUserEntity() {
        UserEntity result = entityFinderService.getUserEntity(user.getId());

        assertThat(result).isEqualTo(user);
    }

    @Test
    void testGetUserEntityThrowsNotFoundException() {
        assertThatThrownBy(() -> entityFinderService.getUserEntity(NOT_EXIST_USER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(USER_NOT_FOUND_MESSAGE);
    }

    @Test
    void testGetPostEntity() {
        PostEntity result = entityFinderService.getPostEntity(post.getId());

        assertThat(result).isEqualTo(post);
    }

    @Test
    void testGetPostEntityThrowsNotFoundException() {
        assertThatThrownBy(() -> entityFinderService.getPostEntity(NOT_EXIST_POST_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(POST_NOT_FOUND_MESSAGE);
    }
}
