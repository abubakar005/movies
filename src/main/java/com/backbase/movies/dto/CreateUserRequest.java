package com.backbase.movies.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    @NotNull
    private String userName;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String roles;
}
