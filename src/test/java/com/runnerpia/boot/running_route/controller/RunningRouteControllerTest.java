package com.runnerpia.boot.running_route.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runnerpia.boot.running_route.dto.CoordinateDto;
import com.runnerpia.boot.running_route.dto.request.CreateRunningRouteRequestDto;
import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.util.GeometryConverter;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RunningRouteControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private EntityManager entityManager;
  @Autowired
  private ObjectMapper objectMapper;

  private static final String BASE_URL = "/running-route";
  private static final UUID ROUTE_SEQ = UUID.randomUUID();
  private static final String ROUTE_NAME = "test_route";
  private static final String NOT_EXIST = "NOT_EXIST";
  private List<CoordinateDto> sampleCoordinate = Arrays.asList(new CoordinateDto(37.1234, -122.5678), new CoordinateDto(37.5678, -122.1234));

  private RunningRoute runningRoute;
  private User dummyUser;
  private CreateRunningRouteRequestDto requestDto;


  @BeforeEach
  void initData() {
    // given
    dummyUser = User.builder()
            .userId("test-id")
            .nickname("test-nickname")
            .build();

    runningRoute = RunningRoute.builder()
            .routeName(ROUTE_NAME)
            .arrayOfPos(GeometryConverter.convertToLineString(sampleCoordinate))
            .runningTime(LocalTime.of(1, 30, 33))
            .runningDate(LocalDateTime.of(2023, 12, 2, 19, 30))
            .review("Great route!")
            .location("Test Location")
            .user(dummyUser)
            .build();

    requestDto = CreateRunningRouteRequestDto.builder()
            .routeName("Test Route1")
            .arrayOfPos(sampleCoordinate)
            .runningTime(String.valueOf(LocalTime.of(1, 30, 33)))
            .runningDate(String.valueOf(LocalDate.of(2023, 11, 11)))
            .review("Great route!")
            .location("Test Location")
            .user(dummyUser.getId())
            .build();

    entityManager.persist(dummyUser);
    entityManager.persist(runningRoute);
    entityManager.flush();
  }

  @Test
  void dataTest() {
    assertNotEquals(dummyUser, null);
    assertNotEquals(runningRoute, null);
  }

  @Test
  @DisplayName("경로 이름 중복 테스트")
  @Transactional(readOnly = true)
  void checkDuplicatedRouteNameTest() throws Exception {
    mockMvc.perform(
            MockMvcRequestBuilders
                    .get(BASE_URL+"/checkDuplicatedRouteName/{name}", ROUTE_NAME)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isExists").value(true))
            .andDo(print());

    mockMvc.perform(
                    MockMvcRequestBuilders
                            .get(BASE_URL+"/checkDuplicatedRouteName/{name}", NOT_EXIST)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isExists").value(false))
            .andDo(print());
  }

  @Test
  @DisplayName("경로 seq 존재 여부 테스트")
  @Transactional(readOnly = true)
  void existsByIdTest() throws Exception {
    mockMvc.perform(
            MockMvcRequestBuilders
                    .get(BASE_URL + "/existsById/{id}", ROUTE_SEQ)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isExists").value(false))
            .andDo(print());

    mockMvc.perform(
            MockMvcRequestBuilders
                    .get(BASE_URL+"/existsById/{id}", runningRoute.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isExists").value("true"));
  }

  @Test
  @DisplayName("경로 등록 테스트")
  @Transactional
  void createTest() throws Exception{
    MockMultipartFile multipartFile1 = new MockMultipartFile("files", "test.jpeg", "multipart/form-data", "test file".getBytes(StandardCharsets.UTF_8) );
    MockMultipartFile multipartFile2 = new MockMultipartFile("files", "test2.jpeg", "multipart/form-data", "test file2".getBytes(StandardCharsets.UTF_8) );
    MockMultipartFile request = new MockMultipartFile("request", "request", "application/json", objectMapper.writeValueAsString(requestDto).getBytes(StandardCharsets.UTF_8));

    mockMvc.perform(
            MockMvcRequestBuilders.multipart(BASE_URL)
                    .file(multipartFile1)
                    .file(multipartFile2)
                    .file(request)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(content().string(""));
  }

  @Test
  @DisplayName("특정 경로 상세 정보 테스트")
  @Transactional(readOnly = true)
  void getMainRouteDetailTest() throws Exception{
    mockMvc.perform(
            MockMvcRequestBuilders
                    .get(BASE_URL + "/main/{id}", runningRoute.getId().toString())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.routeName").value(ROUTE_NAME));
  }

  @Test
  @DisplayName("등록한 경로 정보 수정 테스트")
  @Transactional
  void updateTest() throws Exception{
    MockMultipartFile multipartFile1 = new MockMultipartFile("files", "test.jpeg", "multipart/form-data", "test file".getBytes(StandardCharsets.UTF_8) );
    MockMultipartFile multipartFile2 = new MockMultipartFile("files", "test2.jpeg", "multipart/form-data", "test file2".getBytes(StandardCharsets.UTF_8) );
    MockMultipartFile request = new MockMultipartFile("request", "request", "application/json", objectMapper.writeValueAsString(requestDto).getBytes(StandardCharsets.UTF_8));

    mockMvc.perform(
            MockMvcRequestBuilders
                    .multipart(HttpMethod.PUT, BASE_URL + "/{id}", runningRoute.getId())
                    .file(multipartFile1)
                    .file(multipartFile2)
                    .file(request)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk());
  }

  @Test
  @DisplayName("id로 경로 삭제 테스트")
  @Transactional
  void deleteTest() throws Exception{
    mockMvc.perform(
            MockMvcRequestBuilders
                    .delete(BASE_URL + "/{id}", runningRoute.getId()))
            .andExpect(status().isNoContent());
  }
}
