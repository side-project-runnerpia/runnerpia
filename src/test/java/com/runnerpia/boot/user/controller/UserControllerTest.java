package com.runnerpia.boot.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runnerpia.boot.running_route.entities.Tag;
import com.runnerpia.boot.running_route.entities.enums.TagStatus;
import com.runnerpia.boot.running_route.repository.TagRepository;
import com.runnerpia.boot.user.dto.request.UserInfoReqDto;
import com.runnerpia.boot.user.dto.request.UserSignInReqDto;
import com.runnerpia.boot.user.entities.User;
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

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    TagRepository tagRepository;


    private static final String BASE_URL = "/user";
    private static final String USER_ID = "userId";
    private static final String USER_NICKNAME = "nickname";

    private static final String USER_CITY = "userCity";
    private static final String USER_STATE = "userState";
    private static final String SECURE_TAG = "secureTag";
    private static final String RECOMMENDED_TAG = "recommendedTag";
    private static final String NOT_EXIST = "NOT_EXIST";

    private User saveUser;
    private User savedSignInUser;
    private Tag savedSecureTag;
    private Tag savedRecommendedTag;

    private UserSignInReqDto userSignInRequest;

    @BeforeEach
    void initData() {
        UserInfoReqDto userInfoRequest = UserInfoReqDto.builder()
                .userId(USER_ID)
                .nickname(USER_NICKNAME)
                .build();

        saveUser = userService.createUser(userInfoRequest);

        Tag secureTag = new Tag(SECURE_TAG, TagStatus.SECURE);
        savedSecureTag = tagRepository.save(secureTag);

        Tag recommendedTag = new Tag(RECOMMENDED_TAG, TagStatus.SECURE);
        savedRecommendedTag = tagRepository.save(recommendedTag);

        List<String> secureTags = new ArrayList<>();
        secureTags.add(savedSecureTag.getId().toString());

        List<String> recommendedTags = new ArrayList<>();
        recommendedTags.add(savedRecommendedTag.getId().toString());

        userSignInRequest = UserSignInReqDto.builder()
                .userId(USER_ID+2)
                .city(USER_CITY)
                .state(USER_STATE)
                .secureTags(secureTags)
                .recommendedTags(recommendedTags)
                .build();

        savedSignInUser = userService.createUser(userSignInRequest);

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

    @Test
    @DisplayName("회원 가입")
    void signInTest() throws Exception{

        String requestJson = mapper.writeValueAsString(userSignInRequest);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL+"/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }



}