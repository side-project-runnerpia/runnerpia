package com.runnerpia.boot.running_route.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagRecordResponseDto {
  private String tagDescription;
  private Long count;

  public static List<TagRecordResponseDto> mapToTagRecordResponseDtoList(Map<String, Long> tagCounts) {
    return tagCounts.entrySet().stream()
            .map(entry -> new TagRecordResponseDto(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
  }
}
