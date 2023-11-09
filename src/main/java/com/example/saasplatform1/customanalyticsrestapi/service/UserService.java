package com.example.saasplatform1.customanalyticsrestapi.service;

import com.example.saasplatform1.customanalyticsrestapi.contract.UserRequest;
import com.example.saasplatform1.customanalyticsrestapi.contract.UserResponse;
import com.example.saasplatform1.customanalyticsrestapi.model.User;
import com.example.saasplatform1.customanalyticsrestapi.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public UserResponse addUser(UserRequest userRequest) {
        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();
        User saved = userRepository.save(user);
        return modelMapper.map(saved, UserResponse.class);
    }
}
