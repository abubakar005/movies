package com.backbase.movies.service.impl;

import com.backbase.movies.dto.CreateUserRequest;
import com.backbase.movies.exception.DataConflictException;
import com.backbase.movies.repository.UserRepository;
import com.backbase.movies.service.UserService;
import com.backbase.movies.util.Constants;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.backbase.movies.entity.User;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(CreateUserRequest userRequest) {

        Optional<User> byUserNameOrEmail = userRepository.findByUserNameOrEmail(userRequest.username(), userRequest.email());

        byUserNameOrEmail.ifPresent(user -> {
            if (user.getUserName().equals(userRequest.username()))
                throw new DataConflictException(Constants.ERROR_DUPLICATE_USER_NAME, Constants.ERROR_DUPLICATE_USER_NAME_MSG);

            if (user.getEmail().equals(userRequest.email()))
                throw new DataConflictException(Constants.ERROR_DUPLICATE_USER_EMAIL, Constants.ERROR_DUPLICATE_USER_EMAIL_MSG);
        });

        User newUser = User.builder()
                .userName(userRequest.username())
                .email(userRequest.email())
                .password(passwordEncoder.encode(userRequest.password()))
                .roles(userRequest.roles())
                .build();

        userRepository.save(newUser);
    }
}
