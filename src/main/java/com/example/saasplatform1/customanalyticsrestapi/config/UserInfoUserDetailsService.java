package com.example.saasplatform1.customanalyticsrestapi.config;

import com.example.saasplatform1.customanalyticsrestapi.exception.UserNotFoundException;
import com.example.saasplatform1.customanalyticsrestapi.model.User;
import com.example.saasplatform1.customanalyticsrestapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findByName(username);
        return userOptional.map(UserInfoUserDetails::new)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
