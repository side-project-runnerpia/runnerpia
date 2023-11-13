package com.runnerpia.boot.auth.repository;

import com.runnerpia.boot.auth.entities.Token;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@Slf4j
@SpringBootTest
@Transactional
class TokenRepositoryTest {

    @Autowired
    TokenRepository tokenRepository;

    private Token token;
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";



    @BeforeEach
    void setUp() {
        token = Token.builder()
                .accessToken(ACCESS_TOKEN)
                .refreshToken(REFRESH_TOKEN)
                .build();
        tokenRepository.save(token);
    }

    @AfterEach
    void teardown() {
        tokenRepository.deleteAll();
    }

    @Test
    @DisplayName("엑세스 토큰을 찾는 로직 테스트")
    void findByAccessToken() {

        Optional<Token> findToken = tokenRepository.findByAccessToken(ACCESS_TOKEN);
        assertThat(findToken).isPresent();
        assertThat(findToken.get().getAccessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(findToken.get().getRefreshToken()).isEqualTo(REFRESH_TOKEN);
    }





}