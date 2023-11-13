package com.runnerpia.boot.user.repository;

import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Boolean existsByUserId(String userId);
    Boolean existsByNickname(String nickname);
    Optional<User> findByUserId(String userId);
    @Query("SELECT r FROM RunningRoute r WHERE r.user = :user")
    List<RunningRoute> findAllRunningRoutesByUser(@Param("user") User user);

}
