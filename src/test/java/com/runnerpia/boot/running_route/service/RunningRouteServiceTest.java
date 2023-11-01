package com.runnerpia.boot.running_route.service;

import com.runnerpia.boot.running_route.dto.CoordinateDto;
import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.running_route.repository.RunningRouteRepository;
import com.runnerpia.boot.user.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
public class RunningRouteServiceTest {
  @Autowired
  private EntityManager entityManager;
  @Autowired
  private RunningRouteService runningRouteService;
  @Autowired
  private RunningRouteRepository runningRouteRepository;
  private RunningRoute mainRoute;
  private RunningRoute subRoute1;
  private RunningRoute subRoute2;
  private RunningRoute subRoute3;
  private User targetUser;

  @BeforeEach
  void initData() {
    // given
    targetUser = User.builder()
            .userId("test-id")
            .nickname("test-nickname")
            .build();
    entityManager.persist(targetUser);

    mainRoute = RunningRoute.builder()
            .routeName("main_route")
            .arrayOfPos(Arrays.asList(new CoordinateDto(37.1234, -122.5678), new CoordinateDto(37.5678, -122.1234)))
            .runningTime(LocalTime.of(1, 30, 33))
            .runningDate(LocalDateTime.of(2023, 12, 2, 19, 30))
            .review("main_route!")
            .distance(10.5f)
            .mainRoute(null)
            .location("Test Location")
            .user(targetUser)
            .build();
    entityManager.persist(mainRoute);

    subRoute1 = RunningRoute.builder()
            .routeName("sub_route1")
            .arrayOfPos(Arrays.asList(new CoordinateDto(37.1234, -122.5678), new CoordinateDto(37.5678, -122.1234)))
            .runningTime(LocalTime.of(1, 30, 33))
            .runningDate(LocalDateTime.of(2023, 12, 2, 19, 30))
            .review("sub_route1!")
            .distance(10.5f)
            .mainRoute(mainRoute)
            .location("Test Location1")
            .user(targetUser)
            .build();
    entityManager.persist(subRoute1);

    subRoute2 = RunningRoute.builder()
            .routeName("sub_route2")
            .arrayOfPos(Arrays.asList(new CoordinateDto(37.1234, -122.5678), new CoordinateDto(37.5678, -122.1234)))
            .runningTime(LocalTime.of(1, 30, 33))
            .runningDate(LocalDateTime.of(2023, 12, 2, 19, 30))
            .review("sub_route2!")
            .distance(10.5f)
            .mainRoute(mainRoute)
            .location("Test Location2")
            .user(targetUser)
            .build();
    entityManager.persist(subRoute2);

    subRoute3 = RunningRoute.builder()
            .routeName("sub_route3")
            .arrayOfPos(Arrays.asList(new CoordinateDto(37.1234, -122.5678), new CoordinateDto(37.5678, -122.1234)))
            .runningTime(LocalTime.of(1, 30, 33))
            .runningDate(LocalDateTime.of(2023, 12, 2, 19, 30))
            .review("sub_route3!")
            .distance(10.5f)
            .mainRoute(mainRoute)
            .location("Test Location3")
            .user(targetUser)
            .build();
    entityManager.persist(subRoute3);

    entityManager.flush();
  }

  @Test
  @DisplayName("메인 라우트 null 체크")
  void isMainRouteTest() {
    assertNull(mainRoute.getMainRoute());
    assertEquals(subRoute1.getMainRoute(), mainRoute);
    assertEquals(subRoute2.getMainRoute(), mainRoute);
    assertEquals(subRoute3.getMainRoute(), mainRoute);
  }

  @Test
  @DisplayName("메인 라우트로 서브 라우트 다 가져오기")
  void getAllSubRouteWithMainRoute() {
    List<RunningRoute> allSubRoute = runningRouteRepository.findAllByIdOrMainRoute(mainRoute.getId(), mainRoute);
    assertEquals(allSubRoute.size(), 4);
    assertEquals(allSubRoute.contains(subRoute1), true);
    assertEquals(allSubRoute.contains(subRoute2), true);
    assertEquals(allSubRoute.contains(subRoute3), true);
  }

  @Test
  @DisplayName("연관 라우트 다 가져오기 테스트")
  void findAllRelatedRoutesInListTest() {
    List<RunningRoute> subList = List.of(mainRoute, subRoute3);
    List<RunningRoute> allRelatedList = runningRouteService.findAllRelatedRoutesInList(subList).stream().toList();
    assertEquals(allRelatedList.contains(subRoute1), true);
    assertEquals(allRelatedList.contains(subRoute2), true);
  }
}
