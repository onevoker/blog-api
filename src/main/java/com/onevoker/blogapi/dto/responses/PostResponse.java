package com.onevoker.blogapi.dto.responses;

public record PostResponse(String username,
                           String title,
                           String content,
                           String publishedAt
) {
}
