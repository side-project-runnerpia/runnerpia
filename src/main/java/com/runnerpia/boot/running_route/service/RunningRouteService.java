package com.runnerpia.boot.running_route.service;

import com.runnerpia.boot.running_route.dto.*;
import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.running_route.entities.enums.TagStatus;
import com.runnerpia.boot.running_route.repository.RunningRouteRepository;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.user.repository.UserRepository;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RunningRouteService {
  private final RunningRouteRepository runningRouteRepository;
  private final UserRepository userRepository; // 임시
  private final ImageService imageService;
  private final TagService tagService;

  @Transactional
  public User saveDummyUser(User user) {
    return userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public CheckRouteResponseDto checkDuplicatedRouteName(String routeName) {
    boolean isExists = runningRouteRepository.existsByRouteName(routeName);
    return new CheckRouteResponseDto(isExists);
  }

  @Transactional(readOnly = true)
  public CheckRouteResponseDto existsById(String id) {
    boolean isExists = runningRouteRepository.existsById(UUID.fromString(id));
    return new CheckRouteResponseDto(isExists);
  }

  @Transactional(readOnly = true)
  public RunningRoute findById(String id) {
    return runningRouteRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new NoResultException("해당 ID 값을 가진 경로는 존재하지 않아요."));
  }

  @Transactional
  public CreateRunningRouteResponseDto create(CreateRunningRouteRequestDto request) throws RuntimeException {
    RunningRoute route = request.toEntity();

    User dummyUser = saveDummyUser(User.builder() // 임시 데이터
            .userId("1")
            .nickname("1")
            .build());
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

    return CreateRunningRouteResponseDto.builder()
            .id(response.getId())
            .build();
  }

  @Transactional(readOnly = true)
  public MainRouteDetailResponseDto getMainRouteDetail(String id) {
    RunningRoute route = findById(id);

    MainRouteDetailResponseDto response = route.toResponse();

    response.setMainRoute(Optional.ofNullable(route.getMainRoute())
            .map(RunningRoute::getId)
            .orElse(null));

    List<RunningRoute> allRoutes = runningRouteRepository.findAllByIdOrMainRoute(route.getId(), route);

    List<String> images = imageService.findAllByRunningRouteList(allRoutes)
            .stream()
            .filter(tag -> !tag.isEmpty())
            .collect(Collectors.toList());

    response.setImages(images.isEmpty() || images == null ? null : images);

    Map<String, Long> secureTags = tagService.orderTagRecordsByRunningRoute(TagStatus.SECURE, allRoutes);
    response.setSecureTags(secureTags.isEmpty() || secureTags.size() == 0 ? null : secureTags);

    Map<String, Long> recommendTags = tagService.orderTagRecordsByRunningRoute(TagStatus.RECOMMEND, allRoutes);
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

    return CreateRunningRouteResponseDto.builder()
            .id(targetRoute.getId())
            .build();
  }

  @Transactional
  public void delete(String id) {
    RunningRoute targetRoute = findById(id);
    runningRouteRepository.delete(targetRoute);
  }
}
