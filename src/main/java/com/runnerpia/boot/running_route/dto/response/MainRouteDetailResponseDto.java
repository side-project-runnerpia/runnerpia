package com.runnerpia.boot.running_route.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.runnerpia.boot.running_route.dto.CoordinateDto;
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
  private Double distance;
  private String review;
  private String location;
  private String runningTime;
  private String runningDate;
  private List<String> images;
  private List<TagRecordResponseDto> recommendTags;
//  private Map<String, Long> recommendTags;
//  private Map<String, Long> secureTags;
  private List<TagRecordResponseDto> secureTags;
  private UUID mainRoute;
  private UserInfoReqDto user;
}
