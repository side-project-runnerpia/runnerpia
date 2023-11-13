package com.runnerpia.boot.user.repository;

import com.runnerpia.boot.user.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    private static final String USER_ID = "userId";
    private static final String USER_NICKNAME = "nickname";

    private User user;

    @BeforeEach
    void initData() {
        user = User.builder()
                .userId(USER_ID)
                .nickname(USER_NICKNAME)
                .build();
        em.persist(user);
    }
    
    @Test
    @DisplayName("userId nickname 존재여부 확인")
    void userIdANdNicknameExistCheck() {

        Boolean isExistUserId = userRepository.existsByUserId(user.getUserId());
        Boolean isExistNickname = userRepository.existsByNickname(user.getNickname());

        assertThat(isExistUserId).isTrue();
        assertThat(isExistNickname).isTrue();
    }

    @Test
    @DisplayName("userId로 User 찾는 로직")
    void findByUserId() {

        Optional<User> findOptionalUser = userRepository.findByUserId(user.getUserId());

        assertThat(findOptionalUser.isPresent()).isTrue();
        assertThat(findOptionalUser.get().getUserId()).isEqualTo(user.getUserId());
    }

}