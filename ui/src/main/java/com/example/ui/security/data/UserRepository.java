package com.example.ui.security.data;

import com.example.ui.security.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmail(String email);
}
