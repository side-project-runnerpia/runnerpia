package com.runnerpia.boot.running_route.repository;

import com.runnerpia.boot.running_route.entities.RecommendTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecommendTagRepository extends JpaRepository<RecommendTag, UUID> {
}
