package com.runnerpia.boot.running_route.repository;

import com.runnerpia.boot.running_route.dto.response.TagRecordResponseDto;
import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.running_route.entities.SecureTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SecureTagRepository extends JpaRepository<SecureTag, UUID> {
    @Query("SELECT NEW com.runnerpia.boot.running_route.dto.response.TagRecordResponseDto(t.description, COUNT(s.tag)) " +
            "FROM Tag t " +
            "LEFT JOIN SecureTag s ON t.id = s.tag.id " +
            "LEFT JOIN RunningRoute r ON s.runningRoute.id = r.id " +
            "WHERE r.id = :routeId " +
            "AND t.id = :tagId " +
            "GROUP BY t.description")
    TagRecordResponseDto countTagsByRoute(@Param("routeId") UUID routeId, @Param("tagId") UUID tagId);

    @Query("SELECT NEW com.runnerpia.boot.running_route.dto.response.TagRecordResponseDto(t.description, COUNT(st.tag)) " +
            "FROM Tag t " +
            "JOIN SecureTag st ON t.id = st.tag.id " +
            "GROUP BY t.id, t.description " +
            "ORDER BY COUNT(st.tag) DESC")
    List<TagRecordResponseDto> getPopularTags();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM SecureTag st WHERE st.runningRoute = :runningRoute")
    void deleteAllByRunningRoute(@Param("runningRoute") RunningRoute runningRoute);
}
