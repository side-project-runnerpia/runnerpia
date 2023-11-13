package com.runnerpia.boot.user.repository;

import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.user.entities.Bookmark;
import com.runnerpia.boot.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByUser(User user);
    Long deleteByUserAndRunningRoute(User user, RunningRoute runningRoute);
}
