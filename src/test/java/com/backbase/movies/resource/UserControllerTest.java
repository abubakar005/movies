package com.backbase.movies.resource;

import com.backbase.movies.dto.AuthenticationRequest;
import com.backbase.movies.dto.AuthenticationResponse;
import com.backbase.movies.dto.CreateUserRequest;
import com.backbase.movies.service.UserService;
import com.backbase.movies.service.impl.UserServiceImpl;
import com.backbase.movies.util.JwtServiceUtil;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @MockBean
    private JwtServiceUtil jwtService;

    @MockBean
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;


    //@Test
    @DisplayName("Helloooo")
    public void getToken() throws Exception {
        /*mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/authenticate"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.expiresIn").isNumber())
                .andDo(MockMvcResultHandlers.print());*/
        System.out.println("jwtService " + jwtService);
        System.out.println("authenticationManager " + authenticationManager);
        System.out.println("userService " + userService);

        UserController userController = new UserController();
        AuthenticationRequest authRequest = new AuthenticationRequest("user", "pass");
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "pass");
        //authentication.setAuthenticated(true);

        //AuthenticationResponse authenticationResponse = new AuthenticationResponse("token");

        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn("token");

        ResponseEntity<AuthenticationResponse> authenticationResponseResponseEntity = userController.authenticateAndGetToken(authRequest);
    }

    //@Test
    @DisplayName("Helloooo")
    public void getToken22222() throws Exception {
        ArgumentCaptor<CreateUserRequest> valueCapture = ArgumentCaptor.forClass(CreateUserRequest.class);
        CreateUserRequest userRequest = new CreateUserRequest("abc", "abc", "abc", "abc");
        doNothing().when(userService).createUser(valueCapture.capture());
        userService.createUser(userRequest);
        System.out.println("=====" + valueCapture.getValue());
    }

}