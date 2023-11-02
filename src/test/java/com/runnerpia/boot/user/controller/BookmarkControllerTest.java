package com.runnerpia.boot.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runnerpia.boot.running_route.dto.CoordinateDto;
import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.running_route.repository.RunningRouteRepository;
import com.runnerpia.boot.user.dto.request.BookmarkInfoReqDto;
import com.runnerpia.boot.user.dto.request.UserInfoReqDto;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.user.service.BookmarkService;
import com.runnerpia.boot.user.service.UserService;
import com.runnerpia.boot.util.GeometryConverter;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    BookmarkService bookmarkService;
    @Autowired
    UserService userService;
    @Autowired
    RunningRouteRepository runningRouteRepository;

    private static final String BASE_URL = "/user/bookmark";
    private List<CoordinateDto> sampleCoordinate = Arrays.asList(new CoordinateDto(37.1234, -122.5678), new CoordinateDto(37.5678, -122.1234));
    private User user;
    private UUID userUUID;
    private UUID runnigRouteUUID;
    private BookmarkInfoReqDto request;
    private BookmarkInfoReqDto badRequest;

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
                .distance(10.5f)
                .location("Test Location")
                .user(user)
                .build();
        RunningRoute saveRunningRoute = runningRouteRepository.save(runningRoute);
        runnigRouteUUID = saveRunningRoute.getId();

        request = BookmarkInfoReqDto
                .builder().runningRouteId(runnigRouteUUID.toString())
                .userId(userUUID.toString())
                .build();

        badRequest = BookmarkInfoReqDto
                .builder().runningRouteId(UUID.randomUUID().toString())
                .userId(UUID.randomUUID().toString())
                .build();
    }

    @Test
    @DisplayName("북마크 추가 테스트")
    public void createBookmarkTest() throws Exception {

        //정상
        mockMvc.perform(MockMvcRequestBuilders
                .post(BASE_URL + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        //에러발생
        mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(badRequest)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("북마크 삭제 테스트")
    public void deleteBookmarkTest() throws Exception {

        //정상의 경우
        bookmarkService.createBookmark(request);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL + "/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        //에러가 발생한 경우
        mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL + "/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(badRequest)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("유저의 모든 북마크 가져오기 테스트")
    void getAllUserBookmarkTest() throws Exception {

        RunningRoute runningRoute2 = RunningRoute
                .builder()
                .routeName("Test Route2")
                .arrayOfPos(GeometryConverter.convertToLineString(sampleCoordinate))
                .runningTime(LocalTime.of(1, 30, 33))
                .runningDate(LocalDateTime.of(2023, 12, 2, 1,30,33))
                .review("Great route!")
                .distance(10.5f)
                .location("Test Location")
                .user(user)
                .build();

        RunningRoute saveRunningRoute = runningRouteRepository.save(runningRoute2);
        UUID runningRouteUUID2 = saveRunningRoute.getId();

        BookmarkInfoReqDto newRequest = BookmarkInfoReqDto
                .builder()
                .runningRouteId(saveRunningRoute.getId().toString())
                .userId(userUUID.toString())
                .build();

        bookmarkService.createBookmark(request);
        bookmarkService.createBookmark(newRequest);

        //정상의 경우
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/getAll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("runningRouteIdList").exists())
                .andReturn();

        //에러가 발생한 경우
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/getAll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(badRequest)))
                .andExpect(status().isNotFound())
                .andReturn();

    }


}