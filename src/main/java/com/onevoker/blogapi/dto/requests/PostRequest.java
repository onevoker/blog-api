package com.onevoker.blogapi.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record PostRequest(
        @NotBlank
        String title,
        String content) {
}
