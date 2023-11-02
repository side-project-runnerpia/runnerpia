package com.runnerpia.boot.running_route.dto.response;

import com.runnerpia.boot.running_route.dto.CoordinateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchNearbyRouteResponseDto {
  UUID seq;
  String routeName;
  Double distance;
  String location;
  CoordinateDto startPoint;
  List<CoordinateDto> course;
  Double distanceFromMyPosition;
}
