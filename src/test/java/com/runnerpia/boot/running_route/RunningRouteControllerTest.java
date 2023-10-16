package com.runnerpia.boot.running_route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runnerpia.boot.running_route.dto.CreateRunningRouteRequestDto;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
//@WebMvcTest(controllers = RunningRouteController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RunningRouteControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setup() {
    this.mvc = mvc;
  }

  @Test
  public void 신_경로_등록_테스트() throws Exception {
    String contentType = "image/png";

    ClassLoader classLoader = getClass().getClassLoader();
    File demo1 = new File(classLoader.getResource("images/demo.png").getFile());
    File demo2 = new File(classLoader.getResource("images/demo2.png").getFile());
    MockMultipartFile file1 = new MockMultipartFile("test", demo1.getName(), contentType, FileUtils.readFileToByteArray(demo1));
    MockMultipartFile file2 = new MockMultipartFile("test", demo2.getName(), contentType, FileUtils.readFileToByteArray(demo2));

    CreateRunningRouteRequestDto requestDto = new CreateRunningRouteRequestDto();
    requestDto.setRouteName("송정뚝방길");
    requestDto.setRunningDate(LocalDateTime.of(2023,12,1,19,30));
    requestDto.setRunningTime(LocalTime.of(0,34,21));
    requestDto.setReview("testReview");
    requestDto.setDistance(3.76f);
    requestDto.setLocation("서울특별시 성동 송정동");
    requestDto.setImages(Arrays.asList(file1, file2));

    // DTO 객체를 JSON 문자열로 변환
    String jsonRequest = objectMapper.writeValueAsString(requestDto);

    mvc.perform(MockMvcRequestBuilders.post("/running-route")
                    .content(jsonRequest)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  }

}
