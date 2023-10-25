package com.runnerpia.boot.running_route.repository;

import com.runnerpia.boot.running_route.entities.Image;
import com.runnerpia.boot.running_route.entities.RunningRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
  Collection<Image> findAllByRunningRouteIn(List<RunningRoute> runningRouteList);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("DELETE FROM Image i WHERE i.runningRoute = :runningRoute")
  void deleteAllByRunningRoute(@Param("runningRoute") RunningRoute runningRoute);
}
