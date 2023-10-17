package com.runnerpia.boot.running_route.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CoordinateDto {
  private double latitude;
  private double longitude;
}
