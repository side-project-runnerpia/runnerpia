package com.runnerpia.boot.running_route.controller;

import com.runnerpia.boot.running_route.dto.response.TagRecordResponseDto;
import com.runnerpia.boot.running_route.entities.enums.TagStatus;
import com.runnerpia.boot.running_route.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
@Slf4j
public class TagController {
  private final TagService tagService;

  @Operation(summary = "실시간 인기 태그")
  @GetMapping("/popularTags")
  public ResponseEntity<Map<TagRecordResponseDto, TagStatus>> getPopularTags() {
    return ResponseEntity.ok(tagService.getPopularTags());
  }
}
