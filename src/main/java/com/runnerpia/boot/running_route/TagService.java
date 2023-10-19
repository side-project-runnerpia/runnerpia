package com.runnerpia.boot.running_route;

import com.runnerpia.boot.running_route.dto.TagRecordResponseDto;
import com.runnerpia.boot.running_route.entities.*;
import com.runnerpia.boot.running_route.entities.enums.TagStatus;
import com.runnerpia.boot.running_route.repository.RecommendTagRepository;
import com.runnerpia.boot.running_route.repository.TagRepository;
import com.runnerpia.boot.running_route.repository.SecureTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {
  private final TagRepository tagRepository;
  private final SecureTagRepository secureTagRepository;
  private final RecommendTagRepository recommendTagRepository;
  private List<Tag> setTagEntityList(List<String> tags) {
    return tags.parallelStream()
            .map(tagRepository::findByDescription)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
  }
//  @Async
//  public void addSecureTags(List<String> secureTags, RunningRoute runningRoute) {
//    List<Tag> secureTagList = setTagEntityList(secureTags);
//
//    List<SecureTag> resultList = secureTagList.stream()
//            .map(tag -> {
//              SecureTag secureTag = new SecureTag();
//              secureTag.setTag(tag);
//              secureTag.setRunningRoute(runningRoute);
//              return secureTag;
//            })
//            .collect(Collectors.toList());
//
//    secureTagRepository.saveAll(resultList);
//  }
//
//  @Async
//  public void addRecommendTags(List<String> recommendTags, RunningRoute runningRoute) {
//    List<Tag> recommendTagList = setTagEntityList(recommendTags);
//
//    List<RecommendTag> resultList = recommendTagList.stream()
//            .map(tag -> {
//              RecommendTag recommendTag = new RecommendTag();
//              recommendTag.setTag(tag);
//              recommendTag.setRunningRoute(runningRoute);
//              return recommendTag;
//            })
//            .collect(Collectors.toList());
//
//    recommendTagRepository.saveAll(resultList);
//  }

  @Async
  public void addSecureTags(List<String> secureTags, RunningRoute runningRoute) {
    mapTagToRoute(secureTags, runningRoute, secure -> new SecureTag(), secureTagRepository);
  }
  @Async
  public void addRecommendTags(List<String> recommendTags, RunningRoute runningRoute) {
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


//  public Map<String, Long> orderTagRecordsByRunningRoute(TagStatus status, List<RunningRoute> runningRouteList) {
//    TagRecordResponseDto tagsByRoute;
//    Map<String, Long> tagCountMap = new HashMap<>();
//
//    if (status.equals(TagStatus.SECURE)) {
//      List<Tag> tagList = tagRepository.findAllByStatus(status);
//      for (Tag tag : tagList) {
//        for (RunningRoute route : runningRouteList) {
//          tagsByRoute = secureTagRepository.countTagsByRoute(route.getId(), tag.getId());
//          System.out.println("TagStatus.SECURE" + tagsByRoute);
//          if (tagsByRoute != null) {
//            tagCountMap.compute(tagsByRoute.getTagDescription(),
//                    (key, existingCount) -> (existingCount == null) ? 1 : existingCount + 1);
//          }
//          System.out.println(tagCountMap);
//        }
//      }
//    }
//    else  {
//      List<Tag> tagList = tagRepository.findAllByStatus(status);
//      for (Tag tag : tagList) {
//        for (RunningRoute route : runningRouteList) {
//          tagsByRoute = recommendTagRepository.countTagsByRoute(route.getId(), tag.getId());
//          System.out.println("TagStatus.RECOMMEND" + tagsByRoute);
//          if (tagsByRoute != null) {
//            tagCountMap.compute(tagsByRoute.getTagDescription(),
//                    (key, existingCount) -> (existingCount == null)
//                            ? 1 : existingCount + 1);
//          }
//          System.out.println(tagCountMap);
//        }
//      }
//    }
//
//    return tagCountMap;
//  }

//  private Map<String, Long> countTagsByRoute(TagStatus status, List<RunningRoute> runningRouteList,
//                                             BiFunction<UUID, UUID, TagRecordResponseDto> countFunction) {
//    Map<String, Long> tagCountMap = new HashMap<>();
//
//    if (status != null) {
//      List<Tag> tagList = tagRepository.findAllByStatus(status);
//      for (Tag tag : tagList) {
//        for (RunningRoute route : runningRouteList) {
//          TagRecordResponseDto tagsByRoute = countFunction.apply(route.getId(), tag.getId());
//          if (tagsByRoute != null) {
//            tagCountMap.compute(tagsByRoute.getTagDescription(),
//                    (key, existingCount) -> (existingCount == null) ? 1 : existingCount + 1);
//          }
//        }
//      }
//    }
//
//    return tagCountMap;
//  }
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
}
