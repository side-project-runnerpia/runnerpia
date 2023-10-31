package com.runnerpia.boot.running_route.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.runnerpia.boot.user.dto.request.UserInfoReqDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MainRouteDetailResponseDto {
  private List<CoordinateDto> arrayOfPos;
  private String routeName;
  private Float distance;
  private String review;
  private String location;
  private String runningTime;
  private String runningDate;
  private List<String> images;
  private Map<String, Long> recommendTags;
  private Map<String, Long> secureTags;
  private UUID mainRoute;
  private UserInfoReqDto user;
}
