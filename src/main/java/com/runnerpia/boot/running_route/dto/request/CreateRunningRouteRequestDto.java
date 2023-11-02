package com.runnerpia.boot.running_route.dto.request;

import com.runnerpia.boot.running_route.dto.CoordinateDto;
import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.util.GeometryConverter;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CreateRunningRouteRequestDto {
  private List<CoordinateDto> arrayOfPos;
  private String routeName;
  private String review;
  private String location;
  private String runningTime;
  private String runningDate;
  private List<MultipartFile> files;
  private List<String> recommendTags;
  private List<String> secureTags;
  private UUID mainRoute;
  private UUID user;

  public RunningRoute toEntity() {
    return RunningRoute.builder()
            .routeName(routeName)
            .arrayOfPos(GeometryConverter.convertToLineString(arrayOfPos))
            .distance(GeometryConverter.convertToLineString(arrayOfPos).getLength())
            .review(review)
            .location(location)
            .runningTime(LocalTime.parse(runningTime))
            .runningDate(LocalDate.parse(runningDate).atStartOfDay())
            .build();
  }
}