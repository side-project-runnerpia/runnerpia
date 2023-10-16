package com.runnerpia.boot.running_route.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class CreateRunningRouteRequestDto {
  private List<CoordinateDto> arrayOfPos;
  private String routeName;
  private float distance;
  private String review;
  private String location;
  private LocalTime runningTime;
  private LocalDateTime runningDate;
  private List<MultipartFile> images;

}