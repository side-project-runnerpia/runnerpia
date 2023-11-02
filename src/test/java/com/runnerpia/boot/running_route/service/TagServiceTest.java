package com.runnerpia.boot.running_route.service;

import com.runnerpia.boot.running_route.entities.enums.TagStatus;
import com.runnerpia.boot.running_route.repository.RunningRouteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TagServiceTest {
  @Autowired
  private TagService tagService;
  @Autowired
  private RunningRouteRepository runningRouteRepository;

  @Test
  @DisplayName("태그 타입 별로 적합한 중간 테이블과 매핑 테스트")
  void validateTagStatusTest() {
    boolean errorMessage = assertThrows(DataIntegrityViolationException.class, () -> {
      tagService.validateTagStatus(
              Arrays.asList("근처에 어린이 보호구역이 있어요", "나무가 많아요", "가파른 구간이 없어요"),
              TagStatus.RECOMMEND
      );
    }).getMessage().contains(" 태그가 아니에요");

    assertTrue(errorMessage);

  }

  @Test
  @DisplayName("안심태그 선택 후 러닝루트 저장 테스트")
  void addSecureTagsTest() {
    tagService.addSecureTags(
            Arrays.asList("근처에 어린이 보호구역이 있어요", "가로등이 많아요", "안심등이 있어요"),
            runningRouteRepository.findTop1ByOrderByCreatedDateDesc()
    );
  }
}
