package com.runnerpia.boot.user.repository;

import com.runnerpia.boot.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
