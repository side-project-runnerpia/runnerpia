package com.runnerpia.boot.running_route.controller;

import com.runnerpia.boot.running_route.dto.*;
import com.runnerpia.boot.running_route.service.RunningRouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
  @ApiResponse(responseCode = "201", description = "Created")
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

  @Operation(summary = "사용자가 해당 경로를 따라 뛴 경험이 있는지 확인")
  @GetMapping("/checkRunningExperience/{id}")
  public ResponseEntity<CheckRunningExperienceDto> checkRunningExperience(@PathVariable String id) {
    return ResponseEntity.ok(runningRouteService.checkRunningExperience(id));
  }

  @Operation(summary = "사용자 마이페이지 추천 경로 업로드 내역")
  @GetMapping("/allMainRoute")
  public ResponseEntity<List<MainRouteDetailResponseDto>> getAllMainRoute(@RequestBody(required = false) String user) {
    return ResponseEntity.ok(runningRouteService.getAllMainRoutes());
  }

  @Operation(summary = "사용자 마이페이지 작성한 리뷰")
  @GetMapping("/allSubRoute")
  public ResponseEntity<List<MainRouteDetailResponseDto>> getAllSubRoute(@RequestBody(required = false) String user) {
    return ResponseEntity.ok(runningRouteService.getAllSubRoutes());
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