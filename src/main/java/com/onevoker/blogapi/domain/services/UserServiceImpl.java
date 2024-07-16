package com.onevoker.blogapi.domain.services;

import com.onevoker.blogapi.domain.entities.UserEntity;
import com.onevoker.blogapi.domain.repositories.PostEntityRepository;
import com.onevoker.blogapi.domain.repositories.UserEntityRepository;
import com.onevoker.blogapi.domain.services.entityFinder.EntityFinder;
import com.onevoker.blogapi.domain.services.interfaces.UserService;
import com.onevoker.blogapi.domain.services.mappers.EntityMapper;
import com.onevoker.blogapi.dto.requests.UserRequest;
import com.onevoker.blogapi.dto.responses.PostResponse;
import com.onevoker.blogapi.dto.responses.UserResponse;
import com.onevoker.blogapi.exceptions.DuplicateUsernameException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserEntityRepository userEntityRepository;
    private final EntityFinder entityFinder;
    private final EntityMapper entityMapper;
    private final PostEntityRepository postEntityRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        return userEntityRepository.findAll().stream()
                .map(entityMapper::getUserResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(int id) {
        UserEntity userEntity = entityFinder.getUserEntity(id);
        return entityMapper.getUserResponse(userEntity);
    }

    @Override
    public List<PostResponse> getUserPosts(int id) {
        UserEntity userEntity = entityFinder.getUserEntity(id);

        return userEntity.getPosts().stream()
                .map(entityMapper::getPostResponse)
                .toList();
    }

    @Override
    public List<PostResponse> getUserPostsBetweenDates(int id, OffsetDateTime startDate, OffsetDateTime endDate) {
        return postEntityRepository.findByUserEntityIdAndPublishedAtBetween(id, startDate, endDate).stream()
                .map(entityMapper::getPostResponse)
                .toList();
    }

    @Override
    public void createUser(UserRequest userRequest) {
        checkIsUsernameAvailable(userRequest.username());
        UserEntity userEntity = entityMapper.getUserEntity(userRequest);
        userEntityRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void updateUser(int id, UserRequest userRequest) {
        UserEntity existUserEntity = entityFinder.getUserEntity(id);
        String newUsername = userRequest.username();

        if (existUserEntity.getUsername().equals(newUsername)) {
            existUserEntity.setPassword(userRequest.password());
        } else {
            checkIsUsernameAvailable(newUsername);

            existUserEntity.setUsername(userRequest.username());
            existUserEntity.setPassword(userRequest.password());
        }
    }

    @Override
    public void deleteUser(int id) {
        userEntityRepository.deleteById(id);
    }

    private void checkIsUsernameAvailable(String username) {
        if (userEntityRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException();
        }
    }
}
