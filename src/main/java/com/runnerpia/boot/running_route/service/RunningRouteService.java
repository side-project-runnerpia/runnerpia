package com.runnerpia.boot.running_route.service;

import com.runnerpia.boot.running_route.dto.request.CreateRunningRouteRequestDto;
import com.runnerpia.boot.running_route.dto.response.SearchNearbyRouteResponseDto;
import com.runnerpia.boot.running_route.dto.response.TagRecordResponseDto;
import com.runnerpia.boot.running_route.dto.simple.CheckRouteResponseDto;
import com.runnerpia.boot.running_route.dto.simple.CheckRunningExperienceDto;
import com.runnerpia.boot.running_route.dto.simple.CreateRunningRouteResponseDto;
import com.runnerpia.boot.running_route.dto.response.MainRouteDetailResponseDto;
import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.running_route.entities.enums.TagStatus;
import com.runnerpia.boot.running_route.repository.RunningRouteRepository;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.user.service.UserService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class RunningRouteService {
  private final RunningRouteRepository runningRouteRepository;
  private final ImageService imageService;
  private final TagService tagService;
  private final UserService userService;
  private final GeometryService geometryService;

  private void checkDuplicatedRouteNameForApi(String routeName) {
    runningRouteRepository.findByRouteName(routeName).ifPresent(name -> {
      throw new DataIntegrityViolationException("이미 존재하는 경로 이름이에요!");
    });
  }

  private void isValidUUID(String id) {
    try {
      UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("유효하지 않은 UUID입니다: " + id);
    }
  }

  @Transactional(readOnly = true)
  public CheckRouteResponseDto checkDuplicatedRouteName(String routeName) {
    boolean isExists = runningRouteRepository.existsByRouteName(routeName);
    return new CheckRouteResponseDto(isExists);
  }

  @Transactional(readOnly = true)
  public CheckRouteResponseDto existsById(String id) {
    isValidUUID(id);
    boolean isExists = runningRouteRepository.existsById(UUID.fromString(id));
    return new CheckRouteResponseDto(isExists);
  }

  @Transactional(readOnly = true)
  public RunningRoute findById(String id) {
    isValidUUID(id);
    return runningRouteRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new NoResultException("해당 ID 값을 가진 경로는 존재하지 않아요."));
  }

  @Transactional(readOnly = true)
  public void checkPermissionToDeleteData(RunningRoute targetRoute, String userSeq) {
    isValidUUID(userSeq);
    Optional.of(targetRoute.getUser().getId())
            .filter(id -> id.equals(UUID.fromString(userSeq)))
            .orElseThrow(() -> new AccessDeniedException("해당 경로에 대한 권한이 없어요!"));
  }

  public Set<RunningRoute> findAllRelatedRoutesInList(List<RunningRoute> routeList) {
    return routeList.stream()
            .flatMap(route -> {
              if (route.getMainRoute() == null) {
                return runningRouteRepository.findAllByIdOrMainRoute(route.getId(), route).stream();
              } else {
                return Stream.of(runningRouteRepository.findById(route.getMainRoute().getId()).orElse(null));
              }
            }).collect(Collectors.toSet());
  }

  @Transactional
  public CreateRunningRouteResponseDto create(CreateRunningRouteRequestDto request) throws RuntimeException {
    RunningRoute route = request.toEntity();
    checkDuplicatedRouteNameForApi(route.getRouteName());
    User user = userService.findUserByUserSeq(String.valueOf(request.getUser()));
    route.setUser(user);

    Optional.ofNullable(request.getMainRoute())
            .map(mainRouteId -> runningRouteRepository.findById(mainRouteId)
                    .orElse(null))
            .ifPresent(route::setMainRoute);

    RunningRoute response = runningRouteRepository.save(route);

    Optional.ofNullable(request.getFiles())
            .ifPresent(files -> imageService.saveAll(files, response));

    Optional.ofNullable(request.getSecureTags())
            .filter(tags -> !tags.isEmpty())
            .ifPresent(tags -> tagService.addSecureTags(tags, response));

    Optional.ofNullable(request.getRecommendTags())
            .filter(tags -> !tags.isEmpty())
            .ifPresent(tags -> tagService.addRecommendTags(tags, response));

    return new CreateRunningRouteResponseDto(response.getId());
  }

  @Transactional(readOnly = true)
  public MainRouteDetailResponseDto getMainRouteDetail(String id) {
    RunningRoute targetRoute = findById(id);

    MainRouteDetailResponseDto response = targetRoute.toResponse();

    Set<RunningRoute> allRoutes = findAllRelatedRoutesInList(List.of(targetRoute));

    List<String> images = imageService.findAllByRunningRouteList(allRoutes)
            .stream()
            .filter(tag -> !tag.isEmpty())
            .collect(Collectors.toList());

    response.setImages(images.isEmpty() || images == null ? null : images);

    List<TagRecordResponseDto> secureTags = tagService.orderTagRecordsByRunningRoute(TagStatus.SECURE, allRoutes);
    response.setSecureTags(secureTags.isEmpty() || secureTags.size() == 0 ? null : secureTags);

    List<TagRecordResponseDto> recommendTags = tagService.orderTagRecordsByRunningRoute(TagStatus.RECOMMEND, allRoutes);
    response.setRecommendTags(recommendTags.isEmpty() || recommendTags.size() == 0 ? null : recommendTags);

    return response;
  }

  @Transactional
  public CreateRunningRouteResponseDto update(CreateRunningRouteRequestDto request, String id, String userSeq) {
    RunningRoute targetRoute = findById(id);
    checkPermissionToDeleteData(targetRoute, userSeq);

    targetRoute.setReview(request.getReview());

    Optional.ofNullable(request.getFiles())
            .ifPresent(files -> imageService.update(files, targetRoute));

    tagService.updateSecureTags(request.getSecureTags(), targetRoute);
    tagService.updateRecommendTags(request.getRecommendTags(), targetRoute);

    return new CreateRunningRouteResponseDto(targetRoute.getId());
  }

  @Transactional
  public void delete(String id, String userSeq) {
    RunningRoute targetRoute = findById(id);
    checkPermissionToDeleteData(targetRoute, userSeq);
    runningRouteRepository.delete(targetRoute);
  }

  public CheckRunningExperienceDto checkRunningExperience(String id, String userSeq) {
    User targetUser = userService.findUserByUserSeq(userSeq);
    RunningRoute targetRoute = findById(id);

    Boolean isExperienceRoute = findAllRelatedRoutesInList(userService.findAllRunningRoutesByUser(targetUser))
            .stream()
            .anyMatch(route -> route.getId().equals(targetRoute.getId()));

    return new CheckRunningExperienceDto(isExperienceRoute);
  }

  public List<MainRouteDetailResponseDto> getAllRoutesByFilter(Predicate<RunningRoute> filter, String userSeq) {
    User targetUser = userService.findUserByUserSeq(userSeq);
    return userService.findAllRunningRoutesByUser(targetUser)
            .stream()
            .filter(filter)
            .map(RunningRoute::toResponse)
            .collect(Collectors.toList());
  }

  public List<MainRouteDetailResponseDto> getAllMainRoutes(String userSeq) {
    Predicate<RunningRoute> isMainRoute = runningRoute -> runningRoute.getMainRoute() == null;
    return getAllRoutesByFilter(isMainRoute, userSeq);
  }

  public List<MainRouteDetailResponseDto> getAllSubRoutes(String userSeq) {
    Predicate<RunningRoute> isSubRoute = runningRoute -> runningRoute.getMainRoute() != null;
    return getAllRoutesByFilter(isSubRoute, userSeq);
  }

  public List<SearchNearbyRouteResponseDto> getNearbyRouteList(Double longitude, Double latitude, int range) {
    Point point = geometryService.createPoint(latitude, longitude);
    return runningRouteRepository.findNearbyRouteList(point, range);
  }
}
