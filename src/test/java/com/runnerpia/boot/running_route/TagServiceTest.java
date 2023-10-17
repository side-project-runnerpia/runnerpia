package com.runnerpia.boot.running_route;

import com.runnerpia.boot.running_route.repository.RunningRouteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@SpringBootTest
public class TagServiceTest {
  @Autowired
  private TagService tagService;
  @Autowired
  private RunningRouteRepository runningRouteRepository;

  @Test
  @Transactional
  void 안심태그_선택_후_러닝루트_저장_테스트() {
    tagService.addSecureTags(
            Arrays.asList("근처에 어린이 보호구역이 있어요", "가로등이 많아요", "안심등이 있어요"),
            runningRouteRepository.findTop1ByOrderByCreatedDateDesc()
    );
  }
}
