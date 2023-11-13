package com.runnerpia.boot.running_route.dto.simple;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CreateRunningRouteResponseDto(@JsonProperty("id") UUID id) {
}