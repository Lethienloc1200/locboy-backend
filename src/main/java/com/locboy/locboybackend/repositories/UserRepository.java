package com.locboy.locboybackend.repositories;

import com.locboy.locboybackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
   boolean existsByPhoneNumber(String phoneNumber);
   Optional<User> findByPhoneNumber(String phoneNumber);
}
