package com.runnerpia.boot.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runnerpia.boot.user.dto.request.UserInfoReqDto;
import com.runnerpia.boot.user.service.UserService;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    UserService userService;


    private static final String BASE_URL = "/user";
    private static final String USER_ID = "userId";
    private static final String USER_NICKNAME = "nickname";
    private static final String NOT_EXIST = "NOT_EXIST";


    @BeforeEach
    void initData() {
        UserInfoReqDto request = UserInfoReqDto.builder()
                .userId(USER_ID)
                .nickname(USER_NICKNAME)
                .build();

        userService.createUser(request);
    }

    @Test
    @DisplayName("아이디 존재여부 확인")
    void isExistUserIdTest() throws Exception {

        //아이디가 존재하지 않는 경우
        mockMvc.perform(MockMvcRequestBuilders
                    .get(BASE_URL+"/checkId/{id}", NOT_EXIST)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.isExists").value("false"));

        //아이디가 이미 존재하는 경우
        mockMvc.perform(MockMvcRequestBuilders
                    .get(BASE_URL+"/checkId/{id}", USER_ID)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.isExists").value("true"));
    }

    @Test
    @DisplayName("닉네임 존재여부 확인")
    void isExistNicknameTest() throws Exception {

        //닉네임이 존재하지 않는 경우
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"/checkNickname/{nickname}", NOT_EXIST)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.isExists").value("false"));

        //닉네임이 이미 존재하는 경우
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"/checkNickname/{nickname}", USER_NICKNAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.isExists").value("true"));
    }

    @Test
    @DisplayName("추천 경로 사용횟수 증가 확인")
    void increaseUseRecommendedTest() throws Exception {

        //TODO: 토큰을 사용했을 경우에 테스트 코드 변경 필요
        mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL+"/increaseUseRecommended")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @DisplayName("추천경롤르 사용횟수를 가져오는 기능 확인")
    void getUseRecommendedTest() throws Exception {

        //TODO: 토큰을 사용했을 경우에 테스트 코드 변경 필요
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"/getUseRecommended")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }



}