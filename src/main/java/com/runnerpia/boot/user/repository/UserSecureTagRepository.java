package com.runnerpia.boot.user.repository;

import com.runnerpia.boot.user.entities.UserSecureTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserSecureTagRepository extends JpaRepository<UserSecureTag, UUID> {
}
