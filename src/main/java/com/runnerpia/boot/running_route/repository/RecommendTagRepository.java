package com.runnerpia.boot.running_route.repository;

import com.runnerpia.boot.running_route.dto.TagRecordResponseDto;
import com.runnerpia.boot.running_route.entities.RecommendTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface RecommendTagRepository extends JpaRepository<RecommendTag, UUID> {
  @Query("SELECT NEW com.runnerpia.boot.running_route.dto.TagRecordResponseDto(t.description, COUNT(s.tag)) " +
          "FROM Tag t " +
          "LEFT JOIN RecommendTag s ON t.id = s.tag.id " +
          "LEFT JOIN RunningRoute r ON s.runningRoute.id = r.id " +
          "WHERE r.id = :routeId " +
          "AND t.id = :tagId " +
          "GROUP BY t.description")
  TagRecordResponseDto countTagsByRoute(@Param("routeId") UUID routeId, @Param("tagId") UUID tagId);
}
