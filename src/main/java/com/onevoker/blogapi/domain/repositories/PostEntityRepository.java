package com.onevoker.blogapi.domain.repositories;

import com.onevoker.blogapi.domain.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostEntityRepository extends JpaRepository<PostEntity, Integer> {
}