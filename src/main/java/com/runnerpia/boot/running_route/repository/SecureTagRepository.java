package com.runnerpia.boot.running_route.repository;

import com.runnerpia.boot.running_route.entities.SecureTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SecureTagRepository extends JpaRepository<SecureTag, UUID> {
}
