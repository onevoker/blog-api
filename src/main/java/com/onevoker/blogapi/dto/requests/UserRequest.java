package com.onevoker.blogapi.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank
        String username,
        @NotBlank
        String password) {
}
