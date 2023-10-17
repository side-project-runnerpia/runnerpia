package com.runnerpia.boot.running_route;

import com.runnerpia.boot.running_route.entities.RecommendTag;
import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.running_route.entities.SecureTag;
import com.runnerpia.boot.running_route.entities.Tag;
import com.runnerpia.boot.running_route.repository.RecommendTagRepository;
import com.runnerpia.boot.running_route.repository.TagRepository;
import com.runnerpia.boot.running_route.repository.SecureTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
  @Async
  public void addSecureTags(List<String> secureTags, RunningRoute runningRoute) {
    List<Tag> secureTagList = setTagEntityList(secureTags);

    System.out.println("secureTagList :: " + secureTagList);

    List<SecureTag> resultList = secureTagList.stream()
            .map(tag -> {
              SecureTag secureTag = new SecureTag();
              secureTag.setTag(tag);
              secureTag.setRunningRoute(runningRoute);
              return secureTag;
            })
            .collect(Collectors.toList());

    secureTagRepository.saveAll(resultList);
  }

  @Async
  public void addRecommendTags(List<String> recommendTags, RunningRoute runningRoute) {
    List<Tag> recommendTagList = setTagEntityList(recommendTags);

    System.out.println("recommendTagList :: " + recommendTagList);

    List<RecommendTag> resultList = recommendTagList.stream()
            .map(tag -> {
              RecommendTag recommendTag = new RecommendTag();
              recommendTag.setTag(tag);
              recommendTag.setRunningRoute(runningRoute);
              return recommendTag;
            })
            .collect(Collectors.toList());

    recommendTagRepository.saveAll(resultList);
  }
}
