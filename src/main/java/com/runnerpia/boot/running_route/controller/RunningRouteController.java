package com.runnerpia.boot.running_route.controller;

import com.runnerpia.boot.running_route.dto.CheckRouteResponseDto;
import com.runnerpia.boot.running_route.dto.CreateRunningRouteRequestDto;
import com.runnerpia.boot.running_route.dto.CreateRunningRouteResponseDto;
import com.runnerpia.boot.running_route.dto.MainRouteDetailResponseDto;
import com.runnerpia.boot.running_route.service.RunningRouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/running-route")
@RequiredArgsConstructor
@Slf4j
public class RunningRouteController {
  private final RunningRouteService runningRouteService;

  @Operation(summary = "경로 이름 중복 체크")
  @GetMapping("/checkDuplicatedRouteName/{name}")
  public ResponseEntity<CheckRouteResponseDto> checkDuplicatedRouteName(@PathVariable String name) {
    return ResponseEntity.ok(runningRouteService.checkDuplicatedRouteName(name));
  }

  @Operation(summary = "경로 seq 존재 여부 체크")
  @GetMapping("/existsById/{id}")
  public ResponseEntity<CheckRouteResponseDto> existsById(@PathVariable String id) {
    return ResponseEntity.ok(runningRouteService.existsById(id));
  }

  @Operation(summary = "경로 등록")
  @ApiResponses({
          @ApiResponse(responseCode = "201", description = "Created")})
  @PostMapping
  public ResponseEntity<Void> create(
          @RequestPart(value = "files", required = false) List<MultipartFile> file,
          @RequestPart("request") CreateRunningRouteRequestDto request
  ) {
    request.setFiles(file);
    CreateRunningRouteResponseDto response = runningRouteService.create(request);

    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/main/{id}")
            .buildAndExpand(response.getId())
            .toUri();

    return ResponseEntity.created(location).build();
  }

  @Operation(summary = "특정 경로 상세 정보")
  @GetMapping("/main/{id}")
  public ResponseEntity<MainRouteDetailResponseDto> getMainRouteDetail(@PathVariable String id) {
    return ResponseEntity.ok(runningRouteService.getMainRouteDetail(id));
  }

  @Operation(summary = "등록한 경로 정보 수정")
  @PutMapping("/{id}")
  public ResponseEntity<CreateRunningRouteResponseDto> update(
          @PathVariable String id,
          @RequestPart(value = "files", required = false) List<MultipartFile> file,
          @RequestPart("request") CreateRunningRouteRequestDto request
  ) {
    request.setFiles(file);
    CreateRunningRouteResponseDto response = runningRouteService.update(request, id);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "id로 경로 삭제")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    runningRouteService.delete(id);
    return ResponseEntity.noContent().build();
  }
}