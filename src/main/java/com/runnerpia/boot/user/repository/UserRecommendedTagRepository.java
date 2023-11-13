package com.runnerpia.boot.user.repository;

import com.runnerpia.boot.user.entities.UserRecommendedTag;
import com.runnerpia.boot.user.entities.UserSecureTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRecommendedTagRepository extends JpaRepository<UserRecommendedTag, UUID> {
}
