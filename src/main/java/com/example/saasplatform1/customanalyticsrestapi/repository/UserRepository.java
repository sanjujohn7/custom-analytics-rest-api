package com.example.saasplatform1.customanalyticsrestapi.repository;

import com.example.saasplatform1.customanalyticsrestapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String username);
}
