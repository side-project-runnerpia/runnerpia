package com.runnerpia.boot.running_route.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CreateRunningRouteResponseDto {
  @NotBlank
  private UUID id;
}
