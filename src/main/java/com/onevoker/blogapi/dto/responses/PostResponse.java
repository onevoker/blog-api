package com.onevoker.blogapi.dto.responses;

import java.time.OffsetDateTime;

public record PostResponse(String username,
                           String title,
                           String content,
                           OffsetDateTime publishedAt
) {
}
