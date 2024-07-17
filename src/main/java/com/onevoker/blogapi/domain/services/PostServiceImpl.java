package com.onevoker.blogapi.domain.services;

import com.onevoker.blogapi.domain.entities.PostEntity;
import com.onevoker.blogapi.domain.entities.UserEntity;
import com.onevoker.blogapi.domain.repositories.PostEntityRepository;
import com.onevoker.blogapi.domain.services.entityFinder.EntityFinder;
import com.onevoker.blogapi.domain.services.interfaces.PostService;
import com.onevoker.blogapi.domain.services.mappers.EntityMapper;
import com.onevoker.blogapi.dto.requests.PostRequest;
import com.onevoker.blogapi.dto.responses.PostResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostEntityRepository postEntityRepository;
    private final EntityMapper entityMapper;
    private final EntityFinder entityFinder;

    @Override
    public List<PostResponse> getAllPosts() {
        return postEntityRepository.findAll().stream()
                .map(entityMapper::getPostResponse)
                .toList();
    }

    @Override
    public PostResponse getPostById(int id) {
        PostEntity postEntity = entityFinder.getPostEntity(id);
        return entityMapper.getPostResponse(postEntity);
    }

    @Override
    public List<PostResponse> getAllPostsBetweenDates(OffsetDateTime startDate, OffsetDateTime endDate) {
        return postEntityRepository.findByPublishedAtBetween(startDate, endDate).stream()
                .map(entityMapper::getPostResponse)
                .toList();
    }

    @Override
    public void createPost(int userId, PostRequest postRequest) {
        PostEntity postEntity = entityMapper.getPostEntity(postRequest);
        UserEntity userEntity = entityFinder.getUserEntity(userId);

        postEntity.setUserEntity(userEntity);

        postEntityRepository.save(postEntity);
    }

    @Override
    @Transactional
    public void updatePost(int id, PostRequest postRequest) {
        PostEntity existingPostEntity = entityFinder.getPostEntity(id);

        existingPostEntity.setTitle(postRequest.title());
        existingPostEntity.setContent(postRequest.content());
    }

    @Override
    public void deletePost(int id) {
        postEntityRepository.deleteById(id);
    }
}
