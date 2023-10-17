package com.runnerpia.boot.running_route.repository;

import com.runnerpia.boot.running_route.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
}
