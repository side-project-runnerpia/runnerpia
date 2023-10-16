package com.runnerpia.boot.running_route.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateRunningRouteResponseDto {
  @NotBlank
  private UUID id;
}
