package com.runnerpia.boot.auth.service;

import com.runnerpia.boot.auth.entities.Token;
import com.runnerpia.boot.auth.jwt.JwtProvider;
import com.runnerpia.boot.auth.repository.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public Token saveAccessAndRefreshToken(Token token) {
        return tokenRepository.save(token);
    }

    @Transactional
    public Token getTokenEntity(String accessToken) {

        Optional<Token> findRefreshTokenEntity =
                tokenRepository.findByAccessToken(accessToken);
        return findRefreshTokenEntity.orElse(null);
    }

    @Transactional
    public void removeRefreshToken(String accessToken) {
        tokenRepository.findByAccessToken(accessToken)
                .ifPresent(tokenRepository::delete);
    }



    @Transactional
    public String regenerateAccessToken(String accessToken, String userUUID) {
        String newAccessToken = jwtProvider.generateAccessToken(userUUID);
        tokenRepository.findByAccessToken(accessToken)
                .ifPresent(token -> {
                    token.setAccessToken(newAccessToken);
                    tokenRepository.save(token);
                });
        return newAccessToken;
    }
}
