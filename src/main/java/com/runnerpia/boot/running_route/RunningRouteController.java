package com.runnerpia.boot.running_route;

import com.runnerpia.boot.running_route.dto.CreateRunningRouteRequestDto;
import com.runnerpia.boot.running_route.dto.CreateRunningRouteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/running-route")
@RequiredArgsConstructor
public class RunningRouteController {

  private final RunningRouteService runningRouteService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<CreateRunningRouteResponseDto> create(@RequestBody CreateRunningRouteRequestDto request) {
    CreateRunningRouteResponseDto response = runningRouteService.create(request);
    return ResponseEntity.ok().body(response);
  }

}