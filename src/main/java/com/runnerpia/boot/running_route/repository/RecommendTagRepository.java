package com.runnerpia.boot.running_route.repository;

import com.runnerpia.boot.running_route.dto.TagRecordResponseDto;
import com.runnerpia.boot.running_route.entities.RecommendTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RecommendTagRepository extends JpaRepository<RecommendTag, UUID> {
  @Query("SELECT NEW com.runnerpia.boot.running_route.dto.TagRecordResponseDto(t.description, COUNT(rt.tag)) " +
          "FROM Tag t " +
          "LEFT JOIN RecommendTag rt ON t.id = rt.tag.id " +
          "LEFT JOIN RunningRoute r ON rt.runningRoute.id = r.id " +
          "WHERE rt.id = :routeId " +
          "AND t.id = :tagId " +
          "GROUP BY t.description")
  TagRecordResponseDto countTagsByRoute(@Param("routeId") UUID routeId, @Param("tagId") UUID tagId);

  @Query("SELECT NEW com.runnerpia.boot.running_route.dto.TagRecordResponseDto(t.description, COUNT(rt.tag)) " +
          "FROM Tag t " +
          "JOIN RecommendTag rt ON t.id = rt.tag.id " +
          "GROUP BY t.id, t.description " +
          "ORDER BY COUNT(rt.tag) DESC")
  List<TagRecordResponseDto> getPopularTags();
}
