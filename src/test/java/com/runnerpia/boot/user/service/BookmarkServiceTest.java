package com.runnerpia.boot.user.service;

import com.runnerpia.boot.running_route.dto.CoordinateDto;
import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.running_route.repository.RunningRouteRepository;
import com.runnerpia.boot.user.dto.request.BookmarkInfoReqDto;
import com.runnerpia.boot.user.dto.request.UserInfoReqDto;
import com.runnerpia.boot.user.dto.response.BookmarkInfoRespDto;
import com.runnerpia.boot.user.entities.Bookmark;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.util.GeometryConverter;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class BookmarkServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    BookmarkService bookmarkService;

    @Autowired
    RunningRouteRepository runningRouteRepository;

    private User user;
    private UUID userUUID;
    private UUID runnigRouteUUID;
    private BookmarkInfoReqDto request;

    private List<CoordinateDto> sampleCoordinate = Arrays.asList(new CoordinateDto(37.1234, -122.5678), new CoordinateDto(37.5678, -122.1234));


    @BeforeEach
    void initData() {
        user = userService.createUser(UserInfoReqDto
                .builder().userId("userId")
                .build()
        );
        userUUID = user.getId();

        RunningRoute runningRoute = RunningRoute
                .builder()
                .routeName("Test Route1")
                .arrayOfPos(GeometryConverter.convertToLineString(sampleCoordinate))
                .runningTime(LocalTime.of(1, 30, 33))
                .runningDate(LocalDateTime.of(2023, 12, 2, 1,30,33))
                .review("Great route!")
                .location("Test Location")
                .user(user)
                .build();
        RunningRoute saveRunningRoute = runningRouteRepository.save(runningRoute);
        runnigRouteUUID = saveRunningRoute.getId();

        request = BookmarkInfoReqDto
                .builder()
                .runningRouteId(runnigRouteUUID.toString())
                .build();
    }

    @Test
    @DisplayName("북마크 생성 테스트")
    void createBookmarkTest() {

        Bookmark savedBookmark = bookmarkService.createBookmark(request, userUUID.toString());
        assertThat(savedBookmark.getRunningRoute().getId()).isEqualTo(runnigRouteUUID);
    }

    @Test
    @DisplayName("북마크 삭제 테스트")
    void deleteBookmarkTest() {

        bookmarkService.createBookmark(request, userUUID.toString());
        Long deleteCount = bookmarkService.deleteBookmark(request, userUUID.toString());

        assertThat(deleteCount).isEqualTo(1);
    }

    @Test
    @DisplayName("유저의 모든 북마크 가져오기 테스트")
    void getAllUserBookmarkTest() {

        RunningRoute runningRoute2 = RunningRoute
                .builder()
                .routeName("Test Route2")
                .arrayOfPos(GeometryConverter.convertToLineString(sampleCoordinate))
                .runningTime(LocalTime.of(1, 30, 33))
                .runningDate(LocalDateTime.of(2023, 12, 2, 1,30,33))
                .review("Great route!")
                .location("Test Location")
                .user(user)
                .build();

        RunningRoute saveRunningRoute = runningRouteRepository.save(runningRoute2);
        UUID runningRouteUUID2 = saveRunningRoute.getId();

        BookmarkInfoReqDto newRequest = BookmarkInfoReqDto
                .builder()
                .runningRouteId(saveRunningRoute.getId().toString())
                .build();

        bookmarkService.createBookmark(request, userUUID.toString());
        bookmarkService.createBookmark(newRequest, userUUID.toString());

        BookmarkInfoRespDto response = bookmarkService.getAllUserBookmark(userUUID.toString());
        List<String> runningRouteIdList = response.runningRouteIdList();

        assertThat(runningRouteIdList)
                .containsExactly(runnigRouteUUID.toString(), runningRouteUUID2.toString());
    }

}