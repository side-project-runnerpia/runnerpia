package com.runnerpia.boot.running_route.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagRecordResponseDto {
  private String tagDescription;
  private Long count;
}
