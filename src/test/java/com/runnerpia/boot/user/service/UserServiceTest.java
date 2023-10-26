package com.runnerpia.boot.user.service;

import com.runnerpia.boot.user.dto.UserInfoDto;
import com.runnerpia.boot.user.entities.User;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

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

    private User saveUser;

    @BeforeEach
    void initData() {
        UserInfoDto.Request request = UserInfoDto.Request.builder()
                .userId(USER_ID)
                .nickname(USER_NICKNAME)
                .build();

        saveUser = userService.createUser(request);
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
    void increaseAndGetUseRecommendedTest() throws Exception{

        UUID userUUID = saveUser.getId();

        Integer getInitUserNumberOfUse = userService.getUseRecommended(userUUID).getUser_numberOfUse();
        userService.increaseUseRecommended(userUUID);
        Integer getIncreaseUserNumberOfUse = userService.getUseRecommended(userUUID).getUser_numberOfUse();


        assertThat(getInitUserNumberOfUse).isEqualTo(INIT_NUMBER_OF_USE);
        assertThat(getIncreaseUserNumberOfUse).isEqualTo(INIT_NUMBER_OF_USE+1);

        assertThrows(NoResultException.class, () -> {
            userService.increaseUseRecommended(UUID.randomUUID());
        });
        assertThrows(NoResultException.class, () -> {
            userService.getUseRecommended(UUID.randomUUID());
        });
    }

}