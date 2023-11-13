package com.runnerpia.boot.running_route.repository;

import com.runnerpia.boot.running_route.entities.Tag;
import com.runnerpia.boot.running_route.entities.enums.TagStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {
  Tag findByDescription(String tag);
  List<Tag> findAllByStatus(TagStatus status);
}
