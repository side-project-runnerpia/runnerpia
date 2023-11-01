package com.runnerpia.boot.running_route.repository;

import com.runnerpia.boot.running_route.entities.RunningRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RunningRouteRepository extends JpaRepository<RunningRoute, UUID> {
  RunningRoute findTop1ByOrderByCreatedDateDesc();
  Optional<RunningRoute> findByRouteName(String routeName);
  List<RunningRoute> findAllByIdOrMainRoute(UUID id, RunningRoute mainRoute);
  Boolean existsByRouteName(String routeName);
  boolean existsById(UUID id);
}
