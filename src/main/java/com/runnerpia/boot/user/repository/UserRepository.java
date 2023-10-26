package com.runnerpia.boot.user.repository;

import com.runnerpia.boot.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, UserRepositoryCustom {

    Boolean existsByUserId(String userId);
    Boolean existsByNickname(String nickname);

    Optional<User> findByUserId(String userId);

}
