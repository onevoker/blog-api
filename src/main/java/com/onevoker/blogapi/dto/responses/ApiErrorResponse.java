package com.onevoker.blogapi.dto.responses;

import lombok.Builder;

@Builder
public record ApiErrorResponse(
        String exceptionName,
        String exceptionMessage,
        String code
) {
}