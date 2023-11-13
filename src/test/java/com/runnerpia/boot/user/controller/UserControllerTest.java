package com.runnerpia.boot.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runnerpia.boot.auth.jwt.JwtProperties;
import com.runnerpia.boot.auth.jwt.JwtProvider;
import com.runnerpia.boot.auth.service.AuthService;
import com.runnerpia.boot.running_route.entities.Tag;
import com.runnerpia.boot.running_route.entities.enums.TagStatus;
import com.runnerpia.boot.running_route.repository.TagRepository;
import com.runnerpia.boot.user.dto.request.UserInfoReqDto;
import com.runnerpia.boot.user.dto.request.UserLoginReqDto;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.user.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthService authService;

    private static final String BASE_URL = "/user";
    private static final String USER_ID = "userId";
    private static final String USER_NICKNAME = "nickname";

    private static final String SECURE_TAG = "secureTag";
    private static final String RECOMMENDED_TAG = "recommendedTag";
    private static final String NOT_EXIST = "NOT_EXIST";

    private User saveUser;
    private Tag savedSecureTag;
    private Tag savedRecommendedTag;

    private String accessToken;


    @BeforeEach
    void setUp() {

        saveUser = userService.createUser(new UserInfoReqDto(USER_ID, USER_NICKNAME));
        savedSecureTag = tagRepository.save(new Tag(SECURE_TAG, TagStatus.SECURE));
        savedRecommendedTag = tagRepository.save(new Tag(RECOMMENDED_TAG, TagStatus.RECOMMEND));

        List<String> secureTags = new ArrayList<>();
        secureTags.add(savedSecureTag.getId().toString());

        List<String> recommendedTags = new ArrayList<>();
        recommendedTags.add(savedRecommendedTag.getId().toString());

        HttpHeaders header = authService.login(new UserLoginReqDto(USER_ID));
        accessToken = header.getFirst(JwtProperties.HEADER_STRING);
    }

    @AfterEach
    void tearDown() {
        authService.logout(accessToken);
    }

    @Test
    @WithMockUser
    @DisplayName("아이디 존재여부 확인")
    void isExistUserIdTest() throws Exception {

        //아이디가 존재하지 않는 경우
        mockMvc.perform(MockMvcRequestBuilders
                    .get(BASE_URL+"/checkId/{id}", NOT_EXIST)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isExists").value("false"));

        //아이디가 이미 존재하는 경우
        mockMvc.perform(MockMvcRequestBuilders
                    .get(BASE_URL+"/checkId/{id}", USER_ID)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isExists").value("true"));
    }

    @Test
    @WithMockUser
    @DisplayName("닉네임 존재여부 확인")
    void isExistNicknameTest() throws Exception {

        //닉네임이 존재하지 않는 경우
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"/checkNickname/{nickname}", NOT_EXIST)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isExists").value("false"));

        //닉네임이 이미 존재하는 경우
        mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL+"/checkNickname/{nickname}", USER_NICKNAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isExists").value("true"));
    }

    @Test
    @DisplayName("추천 경로 사용횟수 증가 확인")
    void increaseUseRecommendedTest() throws Exception {

        mockMvc.perform(post(BASE_URL+"/increaseUseRecommended")
                        .header(JwtProperties.HEADER_STRING, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("추천경로 사용횟수를 가져오는 기능 확인")
    void getUseRecommendedTest() throws Exception {

        mockMvc.perform(get(BASE_URL+"/getUseRecommended")
                        .header(JwtProperties.HEADER_STRING, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_numberOfUse").value(0));
    }

}