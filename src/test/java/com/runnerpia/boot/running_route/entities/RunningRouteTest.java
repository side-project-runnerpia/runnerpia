package com.runnerpia.boot.running_route.entities;

import com.runnerpia.boot.running_route.dto.CoordinateDto;
import com.runnerpia.boot.running_route.repository.RunningRouteRepository;
import com.runnerpia.boot.user.entities.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RunningRouteTest {
  @Autowired
  private RunningRouteRepository runningRouteRepository;

  @Test
  @Transactional
  public void testInsertRunningRoute() {
    // given
    User user = new User("1", "1");
    RunningRoute runningRoute = RunningRoute.builder()
            .routeName("Test Route")
            .arrayOfPos(Arrays.asList(new CoordinateDto(37.1234, -122.5678), new CoordinateDto(37.5678, -122.1234)))
            .runningTime(LocalTime.of(1, 30, 33))
            .runningDate(LocalDate.of(2023, 10, 16).atStartOfDay())
            .review("Great route!")
            .distance(10.5f)
            .location("Test Location")
            .user(user) // 적절한 User 객체 생성 또는 Mocking
            .build();

    // when
    RunningRoute savedRoute = runningRouteRepository.save(runningRoute);

    // then
    assertNotNull(savedRoute);
    assertNotNull(savedRoute.getId());
    assertEquals("Test Route", savedRoute.getRouteName());
    assertEquals(2, savedRoute.getArrayOfPos().size());
    assertEquals("Great route!", savedRoute.getReview());
    assertEquals(10.5f, savedRoute.getDistance());
    assertEquals("Test Location", savedRoute.getLocation());
    assertNotNull(savedRoute.getUser());
  }
}
