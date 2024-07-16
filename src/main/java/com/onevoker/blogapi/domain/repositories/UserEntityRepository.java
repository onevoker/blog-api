package com.onevoker.blogapi.domain.repositories;

import com.onevoker.blogapi.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {
}