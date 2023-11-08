package com.runnerpia.boot.auth.service;

import com.runnerpia.boot.auth.entities.Token;
import com.runnerpia.boot.auth.jwt.JwtProvider;
import com.runnerpia.boot.auth.repository.TokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JwtProvider jwtProvider;

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String NEW_ACCESS_TOKEN = "newAccessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    private static final String USER_UUID = "userUUID";



    @Test
    @DisplayName("액세스 토큰과 리프레시 토큰 저장 테스트")
    public void saveAccessAndRefreshTokenTest() {

        Token token = Token
                .builder()
                .accessToken(ACCESS_TOKEN)
                .refreshToken(REFRESH_TOKEN)
                .build();

        tokenService.saveAccessAndRefreshToken(token);

        verify(tokenRepository).save(token);
    }

    @Test
    @DisplayName("토큰 엔티티 get Test")
    public void getTokenEntityTest() {

        Token token = Token
                .builder()
                .accessToken(ACCESS_TOKEN)
                .refreshToken(REFRESH_TOKEN)
                .build();

        when(tokenRepository.findByAccessToken(ACCESS_TOKEN)).thenReturn(Optional.of(token));

        Token getToken = tokenService.getTokenEntity(ACCESS_TOKEN);

        assertThat(getToken).isNotNull();
        assertThat(ACCESS_TOKEN).isEqualTo(getToken.getAccessToken());
    }

    @Test
    @DisplayName("액세스 토큰 갱신 테스트")
    public void regenerateAccessTokenTest() {

        Token token = Token
                .builder()
                .accessToken(ACCESS_TOKEN)
                .refreshToken(REFRESH_TOKEN)
                .build();


        when(tokenRepository.findByAccessToken(ACCESS_TOKEN)).thenReturn(Optional.of(token));
        when(jwtProvider.generateAccessToken(USER_UUID)).thenReturn(NEW_ACCESS_TOKEN);

        String regenerateAccessToken = tokenService.regenerateAccessToken(ACCESS_TOKEN, USER_UUID);

        assertThat(regenerateAccessToken).isEqualTo(NEW_ACCESS_TOKEN);
    }




}