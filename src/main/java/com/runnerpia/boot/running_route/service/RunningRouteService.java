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
import com.runnerpia.boot.user.repository.UserRepository;
import com.runnerpia.boot.user.service.UserService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.dao.DataIntegrityViolationException;
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
  private final UserRepository userRepository; // 임시
  private final ImageService imageService;
  private final TagService tagService;
  private final UserService userService;
  private final GeometryService geometryService;
  private static final User dummyUser = User.builder().userId("test").nickname("test").build(); // 임시 데이터

  @Transactional
  public User saveDummyUser(User user) {
    return userRepository.save(user);
  }

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
//    saveDummyUser(dummyUser); // 임시
    User dummyUser = userRepository.findByUserId("test").get(); // 임시
    route.setUser(dummyUser);

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
    RunningRoute route = findById(id);

    MainRouteDetailResponseDto response = route.toResponse();

    Set<RunningRoute> allRoutes = findAllRelatedRoutesInList(List.of(route));

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
  public CreateRunningRouteResponseDto update(CreateRunningRouteRequestDto request, String id) {
    RunningRoute targetRoute = findById(id);

    targetRoute.setReview(request.getReview());

    Optional.ofNullable(request.getFiles())
            .ifPresent(files -> imageService.update(files, targetRoute));

    tagService.updateSecureTags(request.getSecureTags(), targetRoute);
    tagService.updateRecommendTags(request.getRecommendTags(), targetRoute);

    return new CreateRunningRouteResponseDto(targetRoute.getId());
  }

  @Transactional
  public void delete(String id) {
    RunningRoute targetRoute = findById(id);
    runningRouteRepository.delete(targetRoute);
  }

  public CheckRunningExperienceDto checkRunningExperience(String id) {
    User targetUser = userRepository.findByUserId("test").get(); // 임시
    RunningRoute targetRoute = findById(id);

    Boolean isExperienceRoute = findAllRelatedRoutesInList(userService.findAllRunningRoutesByUser(targetUser))
            .stream()
            .anyMatch(route -> route.getId().equals(targetRoute.getId()));

    return new CheckRunningExperienceDto(isExperienceRoute);
  }

  public List<MainRouteDetailResponseDto> getAllRoutesByFilter(Predicate<RunningRoute> filter) {
    User targetUser = userRepository.findByUserId("test").orElseThrow(); // 임시
    return userService.findAllRunningRoutesByUser(targetUser)
            .stream()
            .filter(filter)
            .map(RunningRoute::toResponse)
            .collect(Collectors.toList());
  }

  public List<MainRouteDetailResponseDto> getAllMainRoutes() {
    Predicate<RunningRoute> isMainRoute = runningRoute -> runningRoute.getMainRoute() == null;
    return getAllRoutesByFilter(isMainRoute);
  }

  public List<MainRouteDetailResponseDto> getAllSubRoutes() {
    Predicate<RunningRoute> isSubRoute = runningRoute -> runningRoute.getMainRoute() != null;
    return getAllRoutesByFilter(isSubRoute);
  }

  public List<SearchNearbyRouteResponseDto> getNearbyRouteList(Double longitude, Double latitude, int range) {
    Point point = geometryService.createPoint(latitude, longitude);
    return runningRouteRepository.findNearbyRouteList(point, range);
  }
}
