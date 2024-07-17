package com.onevoker.blogapi.domain.repositories;

import com.onevoker.blogapi.domain.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface PostEntityRepository extends JpaRepository<PostEntity, Integer> {
    List<PostEntity> findByUserEntityIdAndPublishedAtBetween(int userId, OffsetDateTime startDate, OffsetDateTime endDate);
    List<PostEntity> findByPublishedAtBetween(OffsetDateTime startDate, OffsetDateTime endDate);
}