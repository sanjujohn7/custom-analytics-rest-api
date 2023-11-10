package com.example.saasplatform1.customanalyticsrestapi.controller;

import com.example.saasplatform1.customanalyticsrestapi.contract.AuthRequest;
import com.example.saasplatform1.customanalyticsrestapi.contract.AuthResponse;
import com.example.saasplatform1.customanalyticsrestapi.contract.UserRequest;
import com.example.saasplatform1.customanalyticsrestapi.contract.UserResponse;
import com.example.saasplatform1.customanalyticsrestapi.service.JwtService;
import com.example.saasplatform1.customanalyticsrestapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void testAddUser() throws Exception{
        UserRequest userRequest = UserRequest.builder()
                .name("test")
                .email("test@gmail.com")
                .password("pass")
                .build();
        UserResponse response = UserResponse.builder()
                .id(1L)
                .name("test")
                .email("test@gmail.com")
                .password("pass")
                .build();

        when(userService.addUser(userRequest)).thenReturn(response);
        String path = "/custom-analytics/sign-up";
        mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userRequest)))
                .andExpect(status().isCreated());
    }

//    @Test
//    public void testAuthenticateAndGetToken() throws Exception{
//        AuthRequest authRequest = AuthRequest.builder()
//                .name("test")
//                .password("test-pass")
//                .build();
//        AuthResponse authResponse = AuthResponse.builder()
//                .name("test")
//                .token("token123")
//                .build();
//
//        when(authenticationManager.authenticate(any()))
//                .thenReturn(new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword()));
//
//        when(jwtService.generateToken(authRequest.getName()))
//                    .thenReturn(authResponse);
//
//        mockMvc.perform(post("/custom-analytics/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(authRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").value("token123"));
//    }
}
