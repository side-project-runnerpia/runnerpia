package com.runnerpia.boot.running_route.dto.simple;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CheckRouteResponseDto(@JsonProperty("isExists") Boolean isExists) {
}