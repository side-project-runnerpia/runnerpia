package com.runnerpia.boot.running_route.repository;

import com.runnerpia.boot.running_route.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {
  Tag findByDescription(String tag);
}
