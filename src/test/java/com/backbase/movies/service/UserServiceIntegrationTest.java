package com.backbase.movies.service;

import com.backbase.movies.AbstractContainerBaseTest;
import com.backbase.movies.dto.CreateUserRequest;
import com.backbase.movies.entity.User;
import com.backbase.movies.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceIntegrationTest extends AbstractContainerBaseTest {

    @AfterAll
    static void tearDown() {
        mySQLContainer.stop();
    }

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("This should create a Admin User")
    void shouldCreateNewUser() {
        CreateUserRequest request = new CreateUserRequest("superadmin", "$2a$10$AoH4RE0tmBhmxa3JINjtjOUk2Ict90Hsz/EYcCbGDC5MWw99TwiFO", "superadmin@gmail.com", "Role_SUPER_ADMIN");
        userService.createUser(request);
        User user = userRepository.findByUserName("superadmin").get();
        assertEquals("superadmin", user.getUserName());
        assertEquals("Role_SUPER_ADMIN", user.getRoles());
    }
}