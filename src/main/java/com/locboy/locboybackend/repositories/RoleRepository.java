package com.locboy.locboybackend.repositories;

import com.locboy.locboybackend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
