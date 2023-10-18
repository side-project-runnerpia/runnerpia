package com.runnerpia.boot.running_route.repository;

import com.runnerpia.boot.running_route.entities.RunningRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RunningRouteRepository extends JpaRepository<RunningRoute, UUID> {
  RunningRoute findTop1ByOrderByCreatedDateDesc();
  Optional<RunningRoute> findByRouteName(String routeName);
}
