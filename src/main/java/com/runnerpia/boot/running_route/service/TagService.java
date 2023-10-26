package com.runnerpia.boot.running_route.service;

import com.runnerpia.boot.running_route.dto.TagRecordResponseDto;
import com.runnerpia.boot.running_route.entities.*;
import com.runnerpia.boot.running_route.entities.enums.TagStatus;
import com.runnerpia.boot.running_route.repository.RecommendTagRepository;
import com.runnerpia.boot.running_route.repository.TagRepository;
import com.runnerpia.boot.running_route.repository.SecureTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {
  private final TagRepository tagRepository;
  private final SecureTagRepository secureTagRepository;
  private final RecommendTagRepository recommendTagRepository;
  private List<Tag> setTagEntityList(List<String> tags) {
    return !tags.isEmpty() ? tags.parallelStream()
            .map(tagRepository::findByDescription)
            .filter(Objects::nonNull)
            .collect(Collectors.toList()) : null;
  }

  public void validateTagStatus(List<String> tags, TagStatus status) {
    String explainTag = status.equals(TagStatus.SECURE) ? "안심" : "추천";

    tags.parallelStream()
            .map(tagRepository::findByDescription)
            .filter(tag -> !tag.getStatus().equals(status))
            .findAny()
            .ifPresent(tag -> {
              throw new DataIntegrityViolationException(
                      "[" + tag.getDescription() + "] 태그는 " + explainTag + " 태그가 아니에요");
            });
  }

  @Async
  public void addSecureTags(List<String> secureTags, RunningRoute runningRoute) {
    validateTagStatus(secureTags, TagStatus.SECURE);
    mapTagToRoute(secureTags, runningRoute, secure -> new SecureTag(), secureTagRepository);
  }
  @Async
  public void addRecommendTags(List<String> recommendTags, RunningRoute runningRoute) {
    validateTagStatus(recommendTags, TagStatus.RECOMMEND);
    mapTagToRoute(recommendTags, runningRoute, recommend -> new RecommendTag(), recommendTagRepository);
  }

  private <T extends RouteTag> void mapTagToRoute(
          List<String> tagNames,
          RunningRoute runningRoute,
          Function<Tag, T> tagEntityConstructor,
          JpaRepository<T, UUID> tagRepository
  ) {
    List<Tag> tagList = setTagEntityList(tagNames);

    List<T> resultList = tagList.stream()
            .map(tag -> {
              T tagEntity = tagEntityConstructor.apply(tag);
              tagEntity.setTag(tag);
              tagEntity.setRunningRoute(runningRoute);
              return tagEntity;
            })
            .collect(Collectors.toList());

    tagRepository.saveAll(resultList);
  }

  private Map<String, Long> countTagsByRoute(TagStatus status, List<RunningRoute> runningRouteList,
                                             BiFunction<UUID, UUID, TagRecordResponseDto> countFunction) {

    if (status == null) return Collections.emptyMap();

    return tagRepository.findAllByStatus(status).stream()
            .flatMap(tag -> runningRouteList.stream()
                    .map(route -> countFunction.apply(route.getId(), tag.getId()))
                    .filter(Objects::nonNull)
                    .map(TagRecordResponseDto::getTagDescription))
            .collect(Collectors.groupingBy(
                    Function.identity(), Collectors.counting()
            ));
  }

  public Map<String, Long> orderTagRecordsByRunningRoute(TagStatus status, List<RunningRoute> runningRouteList) {
    BiFunction<UUID, UUID, TagRecordResponseDto> countFunction = null;

    if (status.equals(TagStatus.SECURE)) {
      countFunction = secureTagRepository::countTagsByRoute;
    }
    else if (status.equals(TagStatus.RECOMMEND)) {
      countFunction = recommendTagRepository::countTagsByRoute;
    }

    return countTagsByRoute(status, runningRouteList, countFunction);
  }

  public Map<TagRecordResponseDto, TagStatus> getPopularTags() {
    Map<TagRecordResponseDto, TagStatus> response = Stream.concat(
            secureTagRepository.getPopularTags().stream()
                    .map(tag -> new AbstractMap.SimpleEntry<>(tag, TagStatus.SECURE)),
            recommendTagRepository.getPopularTags().stream()
                    .map(tag -> new AbstractMap.SimpleEntry<>(tag, TagStatus.RECOMMEND))
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    return response.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey(Comparator.comparing(TagRecordResponseDto::getCount).reversed()))
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
            ));
  }

  public void updateSecureTags(List<String> tags, RunningRoute route) {
    deleteAllSecureTagsByRunningRoute(route);
    Optional.ofNullable(tags)
            .filter(tagList -> !tagList.isEmpty())
            .ifPresent(tag -> addSecureTags(tag, route));
  }

  public void updateRecommendTags(List<String> tags, RunningRoute route) {
    deleteAllRecommendTagsByRunningRoute(route);
    Optional.ofNullable(tags)
            .filter(tagList -> !tagList.isEmpty())
            .ifPresent(tag -> addRecommendTags(tag, route));
  }

  private void deleteAllSecureTagsByRunningRoute(RunningRoute runningRoute) {
    secureTagRepository.deleteAllByRunningRoute(runningRoute);
  }

  private void deleteAllRecommendTagsByRunningRoute(RunningRoute runningRoute) {
    recommendTagRepository.deleteAllByRunningRoute(runningRoute);
  }
}
