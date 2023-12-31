package com.runnerpia.boot.running_route.controller;

import com.runnerpia.boot.running_route.dto.request.CreateRunningRouteRequestDto;
import com.runnerpia.boot.running_route.dto.response.SearchNearbyRouteResponseDto;
import com.runnerpia.boot.running_route.dto.simple.CheckRouteResponseDto;
import com.runnerpia.boot.running_route.dto.simple.CheckRunningExperienceDto;
import com.runnerpia.boot.running_route.dto.simple.CreateRunningRouteResponseDto;
import com.runnerpia.boot.running_route.dto.response.MainRouteDetailResponseDto;
import com.runnerpia.boot.running_route.service.RunningRouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

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

  @Operation(summary = "경로 등록", responses = @ApiResponse(responseCode = "201", description = "Created"))
  @PostMapping
  public ResponseEntity<Void> create(
          Authentication authentication,
          @RequestPart(value = "files", required = false) List<MultipartFile> file,
          @RequestPart("request") CreateRunningRouteRequestDto request
  ) {
    request.setFiles(file);
    request.setUser(UUID.fromString(authentication.getName()));
    CreateRunningRouteResponseDto response = runningRouteService.create(request);

    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/main/{id}")
            .buildAndExpand(response.id())
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
  public ResponseEntity<CheckRunningExperienceDto> checkRunningExperience(
          Authentication authentication,
          @PathVariable String id
  ) {
    return ResponseEntity.ok(runningRouteService.checkRunningExperience(id, authentication.getName()));
  }

  @Operation(summary = "사용자 마이페이지 추천 경로 업로드 내역")
  @GetMapping("/allMainRoute")
  public ResponseEntity<List<MainRouteDetailResponseDto>> getAllMainRoute(Authentication authentication) {
    return ResponseEntity.ok(runningRouteService.getAllMainRoutes(authentication.getName()));
  }

  @Operation(summary = "사용자 마이페이지 작성한 리뷰")
  @GetMapping("/allSubRoute")
  public ResponseEntity<List<MainRouteDetailResponseDto>> getAllSubRoute(Authentication authentication) {
    return ResponseEntity.ok(runningRouteService.getAllSubRoutes(authentication.getName()));
  }

  @Operation(summary = "위치 검색", description = "사용자 현재 위치 위도, 경도 파라미터로 받으면 요청한 반경 내 모든 경로 리턴")
  @GetMapping("/searchLocation")
  public ResponseEntity<List<SearchNearbyRouteResponseDto>> getAllRouteWithCurrentLocation(
          @RequestParam(value = "latitude") Double latitude, @RequestParam(value = "longitude") Double longitude, @RequestParam(value = "range") int range
  ) {
    return ResponseEntity.ok(runningRouteService.getNearbyRouteList(longitude, latitude, range));
  }

  @Operation(summary = "등록한 경로 정보 수정")
  @PutMapping("/{id}")
  public ResponseEntity<CreateRunningRouteResponseDto> update(
          Authentication authentication,
          @PathVariable String id,
          @RequestPart(value = "files", required = false) List<MultipartFile> file,
          @RequestPart("request") CreateRunningRouteRequestDto request
  ) {
    request.setFiles(file);
    CreateRunningRouteResponseDto response = runningRouteService.update(request, id, authentication.getName());
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "id로 경로 삭제")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(Authentication authentication, @PathVariable String id) {
    runningRouteService.delete(id, authentication.getName());
    return ResponseEntity.noContent().build();
  }
}