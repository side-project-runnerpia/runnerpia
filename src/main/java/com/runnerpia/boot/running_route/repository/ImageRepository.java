package com.runnerpia.boot.running_route.repository;

import com.runnerpia.boot.running_route.entities.Image;
import com.runnerpia.boot.running_route.entities.RunningRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
  @Query("SELECT i FROM Image i WHERE i.runningRoute IN :runningRouteList")
  Collection<Image> findAllByRunningRouteList(@Param("runningRouteList") List<RunningRoute> runningRouteList);
}
