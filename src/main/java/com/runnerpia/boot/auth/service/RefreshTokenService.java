package com.runnerpia.boot.auth.service;

import com.runnerpia.boot.auth.entities.Token;
import com.runnerpia.boot.auth.jwt.JwtProvider;
import com.runnerpia.boot.auth.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public void saveAccessAndRefreshToken(String userUUID, String accessToken, String refreshToken) {
        System.out.println("저장 토큰 : " + accessToken);
        refreshTokenRepository.save(Token
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
    }

    @Transactional
    public Token getRefreshTokenEntity(String accessToken) {

        Optional<Token> findRefreshTokenEntity =
                refreshTokenRepository.findByAccessToken(accessToken);
        if(findRefreshTokenEntity.isEmpty()) return null;
        return findRefreshTokenEntity.get();
    }

    @Transactional
    public void removeRefreshToken(String accessToken) {
        refreshTokenRepository.findByAccessToken(accessToken)
                .ifPresent(token -> refreshTokenRepository.delete(token));
    }



    @Transactional
    public String regenerateAccessToken(String accessToken, String userUUID) {
        String newAccessToken = jwtProvider.generateAccessToken(userUUID);
        refreshTokenRepository.findByAccessToken(accessToken)
                .ifPresent(token -> {
                    token.setAccessToken(newAccessToken);
                    refreshTokenRepository.save(token);
                });
        return newAccessToken;
    }
}
