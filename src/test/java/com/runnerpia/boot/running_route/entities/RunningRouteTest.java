package com.runnerpia.boot.running_route.entities;

import com.runnerpia.boot.running_route.dto.CoordinateDto;
import com.runnerpia.boot.running_route.entities.enums.TagStatus;
import com.runnerpia.boot.running_route.repository.TagRepository;
import com.runnerpia.boot.user.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE,
        connection = EmbeddedDatabaseConnection.H2
)
public class RunningRouteTest {
  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private TagRepository tagRepository;

  private RunningRoute runningRoute;
  private User dummyUser;

  @BeforeEach
  void initData() {
    // given
    dummyUser = User.builder()
            .userId("test-id")
            .nickname("test-nickname")
            .build();
    entityManager.persistAndFlush(dummyUser);

    runningRoute = RunningRoute.builder()
            .routeName("Test Route")
            .arrayOfPos(Arrays.asList(new CoordinateDto(37.1234, -122.5678), new CoordinateDto(37.5678, -122.1234)))
            .runningTime(LocalTime.of(1, 30, 33))
            .runningDate(LocalDateTime.of(2023, 12, 2, 19, 30))
            .review("Great route!")
            .distance(10.5f)
            .location("Test Location")
            .user(dummyUser)
            .build();

    entityManager.persistAndFlush(runningRoute);
  }

  @Test
  @DisplayName("라우트 엔티티 조회 테스트")
  void readRouteById() {
    // when
    RunningRoute targetRoute = entityManager.find(RunningRoute.class, runningRoute.getId());

    // then
    assertEquals(targetRoute, runningRoute);
    assertNotNull(targetRoute);
    assertTrue(targetRoute.getId() instanceof UUID);
    assertEquals("Test Route", targetRoute.getRouteName());
    assertEquals(2, targetRoute.getArrayOfPos().size());
    assertEquals("Great route!", targetRoute.getReview());
    assertEquals(10.5f, targetRoute.getDistance());
    assertEquals("Test Location", targetRoute.getLocation());
    assertEquals("test-id", targetRoute.getUser().getUserId());
    assertEquals("test-nickname", targetRoute.getUser().getNickname());
  }

  @Test
  @DisplayName("라우트 엔티티 수정 테스트")
  void updateRoute() {
    // when
    RunningRoute targetRoute = entityManager.find(RunningRoute.class, runningRoute.getId());
    targetRoute.setReview("Great route!!!");
    entityManager.persistAndFlush(targetRoute);

    // then
    assertEquals("Great route!!!", runningRoute.getReview());
  }

  @Test
  @DisplayName("라우트 엔티티 삭제 테스트")
  void deleteRoute() {
    // when
    RunningRoute targetRoute = entityManager.find(RunningRoute.class, runningRoute.getId());
    entityManager.remove(targetRoute);
    entityManager.flush();

    // then
    RunningRoute deletedRoute = entityManager.find(RunningRoute.class, runningRoute.getId());
    assertNull(deletedRoute);
  }

  @Test
  @DisplayName("라우트 삭제 시 고아 이미지 객체 자동 삭제 테스트")
  void deleteImageByRoute() {
    // when
    Image image1 = Image.builder().url("https://fake-s3-url.com/image1.jpg").runningRoute(runningRoute).build();
    Image image2 = Image.builder().url("https://fake-s3-url.com/image2.jpg").runningRoute(runningRoute).build();
    Image image3 = Image.builder().url("https://fake-s3-url.com/image3.jpg").runningRoute(runningRoute).build();

    List<Image> images = Arrays.asList(image1, image2, image3);
    images.stream().forEach(image -> entityManager.persist(image));
    entityManager.flush();

    RunningRoute targetRoute = entityManager.find(RunningRoute.class, runningRoute.getId());
    targetRoute.setImages(images);
    entityManager.persistAndFlush(targetRoute);

    assertIterableEquals(targetRoute.getImages(), images);

    entityManager.remove(targetRoute);
    entityManager.flush();

    // then
    RunningRoute deletedRoute = entityManager.find(RunningRoute.class, runningRoute.getId());
    Image deletedImage1 = entityManager.find(Image.class, image1.getId());
    Image deletedImage2 = entityManager.find(Image.class, image2.getId());
    Image deletedImage3 = entityManager.find(Image.class, image3.getId());

    assertNull(deletedRoute);
    assertNull(deletedImage1);
    assertNull(deletedImage2);
    assertNull(deletedImage3);
  }

  @Test
  @DisplayName("라우트 삭제 시 고아 안심 태그 객체 자동 삭제 테스트")
  void deleteSecureTagsByRoute() {
    // when
    List<SecureTag> secureTagList = tagRepository.findAllByStatus(TagStatus.SECURE)
            .stream()
            .map(tag -> new SecureTag(tag, runningRoute)).toList();

    secureTagList.stream().forEach(tag -> entityManager.persist(tag));
    entityManager.flush();

    RunningRoute targetRoute = entityManager.find(RunningRoute.class, runningRoute.getId());
    targetRoute.setSecureTags(secureTagList);
    entityManager.persistAndFlush(targetRoute);

    assertIterableEquals(targetRoute.getSecureTags(), secureTagList);

    entityManager.remove(targetRoute);
    entityManager.flush();

    // then
    RunningRoute deletedRoute = entityManager.find(RunningRoute.class, runningRoute.getId());
    SecureTag deletedSecureTag1 = entityManager.find(SecureTag.class, secureTagList.get(0).getId());
    SecureTag deletedSecureTag2 = entityManager.find(SecureTag.class, secureTagList.get(1).getId());
    SecureTag deletedSecureTag3 = entityManager.find(SecureTag.class, secureTagList.get(2).getId());

    assertNull(deletedRoute);
    assertNull(deletedSecureTag1);
    assertNull(deletedSecureTag2);
    assertNull(deletedSecureTag3);
  }
}
