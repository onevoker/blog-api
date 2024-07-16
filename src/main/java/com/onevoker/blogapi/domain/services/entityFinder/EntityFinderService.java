package com.onevoker.blogapi.domain.services.entityFinder;

import com.onevoker.blogapi.domain.entities.PostEntity;
import com.onevoker.blogapi.domain.entities.UserEntity;
import com.onevoker.blogapi.domain.repositories.PostEntityRepository;
import com.onevoker.blogapi.domain.repositories.UserEntityRepository;
import com.onevoker.blogapi.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntityFinderService implements EntityFinder {
    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    private static final String POST_NOT_FOUND_MESSAGE = "This post does not exist";
    private static final String USER_NOT_FOUND_MESSAGE = "This user does not exist";

    @Override
    public UserEntity getUserEntity(int userId) {
        return userEntityRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    @Override
    public PostEntity getPostEntity(int postId) {
        return postEntityRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND_MESSAGE));
    }
}
