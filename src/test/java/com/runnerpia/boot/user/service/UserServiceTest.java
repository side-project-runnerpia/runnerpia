package com.runnerpia.boot.user.service;

import com.runnerpia.boot.user.dto.UserInfoDto;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {


    @Autowired
    UserService userService;

    private static final String USER_ID = "userId";
    private static final String USER_NICKNAME = "nickname";
    private static final String NOT_EXIST = "NOT_EXIST";
    private static final Integer INIT_NUMBER_OF_USE = 0;

    @BeforeEach
    void initData() {
        UserInfoDto.Request request = UserInfoDto.Request.builder()
                .userId(USER_ID)
                .nickname(USER_NICKNAME)
                .build();

        userService.createUser(request);
    }

    @Test
    @DisplayName("userId, nickname 존재 여부 확인")
    void userIdANdNicknameExistCheck() {


        boolean userIdExists = userService.isUserIdExists(NOT_EXIST);
        boolean nicknameExists = userService.isNicknameExists(NOT_EXIST);

        assertThat(userIdExists).isFalse();
        assertThat(nicknameExists).isFalse();

        assertThrows(DataIntegrityViolationException.class, () -> {
            userService.isUserIdExists(USER_ID);
        });
        assertThrows(DataIntegrityViolationException.class, () -> {
            userService.isNicknameExists(USER_NICKNAME);
        });
    }

    @Test
    @DisplayName("추천 경로 사용횟수 증가, 사용횟수 가져오는 로직")
    void increaseAndGetUseRecommendedTest() {

        Integer getInitUserNumberOfUse = userService.getUseRecommended(USER_ID).getUser_numberOfUse();
        userService.increaseUseRecommended(USER_ID);
        Integer getIncreaseUserNumberOfUse = userService.getUseRecommended(USER_ID).getUser_numberOfUse();


        assertThat(getInitUserNumberOfUse).isEqualTo(INIT_NUMBER_OF_USE);
        assertThat(getIncreaseUserNumberOfUse).isEqualTo(INIT_NUMBER_OF_USE+1);

        assertThrows(NoResultException.class, () -> {
            userService.increaseUseRecommended(NOT_EXIST);
        });
        assertThrows(NoResultException.class, () -> {
            userService.getUseRecommended(NOT_EXIST);
        });
    }

}