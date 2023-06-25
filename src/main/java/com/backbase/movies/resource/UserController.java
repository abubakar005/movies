package com.backbase.movies.resource;

import com.backbase.movies.dto.AuthenticationRequest;
import com.backbase.movies.dto.AuthenticationResponse;
import com.backbase.movies.dto.CreateUserRequest;
import com.backbase.movies.service.UserService;
import com.backbase.movies.util.JwtServiceUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private JwtServiceUtil jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateAndGetToken(@Valid @RequestBody AuthenticationRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(new AuthenticationResponse(jwtService.generateToken(authRequest.getUsername())));
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequest userRequest) {
        userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
