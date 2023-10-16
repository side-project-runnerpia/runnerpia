package com.runnerpia.boot.running_route;

import com.runnerpia.boot.running_route.dto.CreateRunningRouteRequestDto;
import com.runnerpia.boot.running_route.dto.CreateRunningRouteResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RunningRouteService {
  @Transactional
  public CreateRunningRouteResponseDto create(CreateRunningRouteRequestDto requestDto) {
    return null;
  }
}
