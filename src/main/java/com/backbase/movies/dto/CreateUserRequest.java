package com.backbase.movies.dto;

import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(@NotNull String username, @NotNull String password, @NotNull String email, @NotNull String roles) {
}
