package com.runnerpia.boot.user.service;

import com.runnerpia.boot.running_route.entities.Tag;
import com.runnerpia.boot.running_route.entities.enums.TagStatus;
import com.runnerpia.boot.running_route.repository.TagRepository;
import com.runnerpia.boot.user.dto.request.UserInfoReqDto;
import com.runnerpia.boot.user.dto.request.UserSignInReqDto;
import com.runnerpia.boot.user.dto.response.UserInfoCheckRespDto;
import com.runnerpia.boot.user.dto.response.UserSignInRespDto;
import com.runnerpia.boot.user.entities.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {


    @Autowired
    UserService userService;

    @Autowired
    TagRepository tagRepository;

    private static final String USER_ID = "userId";
    private static final String USER_NICKNAME = "nickname";
    private static final String USER_CITY = "userCity";
    private static final String USER_STATE = "userState";
    private static final String SECURE_TAG = "secureTag";
    private static final String RECOMMENDED_TAG = "recommendedTag";
    private static final String NOT_EXIST = "NOT_EXIST";
    private static final Integer INIT_NUMBER_OF_USE = 0;

    private User saveUser;
    private User savedSignInUser;
    private Tag savedSecureTag;
    private Tag savedRecommendedTag;

    private UserSignInReqDto userSignInRequest;

    @BeforeEach
    void initData() {
        UserInfoReqDto userInfoRequest = UserInfoReqDto.builder()
                .userId(USER_ID+1)
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
    @DisplayName("userId, nickname 존재 여부 확인")
    void userIdANdNicknameExistCheck() {


        UserInfoCheckRespDto userIdExists = userService.isUserIdExists(NOT_EXIST);
        UserInfoCheckRespDto nicknameExists = userService.isNicknameExists(NOT_EXIST);

        assertThat(userIdExists.getIsExists()).isFalse();
        assertThat(nicknameExists.getIsExists()).isFalse();

        userIdExists = userService.isUserIdExists(USER_ID);
        nicknameExists =  userService.isNicknameExists(USER_NICKNAME);

        assertThat(userIdExists.getIsExists()).isTrue();
        assertThat(nicknameExists.getIsExists()).isTrue();
    }

    @Test
    @DisplayName("추천 경로 사용횟수 증가, 사용횟수 가져오는 로직")
    void increaseAndGetUseRecommendedTest() throws Exception{

        UUID userUUID = saveUser.getId();

        Integer getInitUserNumberOfUse = userService.getUseRecommended(userUUID).getUser_numberOfUse();
        userService.increaseUseRecommended(userUUID);
        Integer getIncreaseUserNumberOfUse = userService.getUseRecommended(userUUID).getUser_numberOfUse();


        assertThat(getInitUserNumberOfUse).isEqualTo(INIT_NUMBER_OF_USE);
        assertThat(getIncreaseUserNumberOfUse).isEqualTo(INIT_NUMBER_OF_USE+1);

        assertThrows(NoSuchElementException.class, () -> {
            userService.increaseUseRecommended(UUID.randomUUID());
        });
        assertThrows(NoSuchElementException.class, () -> {
            userService.getUseRecommended(UUID.randomUUID());
        });
    }

    @Test
    @DisplayName("회원가입")
    void signInTest() {

        UserSignInRespDto userSignInRespDto = userService.signIn(userSignInRequest);

        assertThat(userSignInRespDto.getUserId()).isEqualTo(savedSignInUser.getUserId());
        assertThat(userSignInRespDto.getCity()).isEqualTo(savedSignInUser.getCity());
        assertThat(userSignInRespDto.getState()).isEqualTo(savedSignInUser.getState());
        assertThat(userSignInRespDto.getSecureTags().get(0)).contains(savedSecureTag.getId().toString());
        assertThat(userSignInRespDto.getRecommendedTags().get(0)).isEqualTo(savedRecommendedTag.getId().toString());
    }


}