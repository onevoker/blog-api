package com.onevoker.blogapi.domain.services.entityFinder;

import com.onevoker.blogapi.domain.entities.PostEntity;
import com.onevoker.blogapi.domain.entities.UserEntity;

public interface EntityFinder {
    UserEntity getUserEntity(int userId);

    PostEntity getPostEntity(int postId);
}
