package com.runnerpia.boot.running_route;

import com.runnerpia.boot.running_route.dto.CreateRunningRouteRequestDto;
import com.runnerpia.boot.running_route.dto.CreateRunningRouteResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/running-route")
@RequiredArgsConstructor
@Slf4j
public class RunningRouteController {

  private final RunningRouteService runningRouteService;

  @PostMapping
  public ResponseEntity<?> create(
          @RequestPart(value = "files", required = false) List<MultipartFile> file,
          @RequestPart("request") CreateRunningRouteRequestDto request
  ) {
    request.setFiles(file);
    CreateRunningRouteResponseDto response = runningRouteService.create(request);
    return ResponseEntity.ok().body(response);
  }

}