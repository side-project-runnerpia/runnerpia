package com.runnerpia.boot.auth.service;

import com.runnerpia.boot.auth.entities.Token;
import com.runnerpia.boot.auth.jwt.JwtProperties;
import com.runnerpia.boot.auth.jwt.JwtProvider;
import com.runnerpia.boot.auth.repository.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.NoSuchElementException;
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

    public void validateRefreshToken(String accessToken){

        Token tokenEntity = getTokenEntity(accessToken);

        if(tokenEntity == null)
            throw new NoSuchElementException("토큰 정보가 존재하지 않습니다.재로그인이 필요합니다.");

        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtProvider.getKey())
                    .build()
                    .parseClaimsJws(tokenEntity.getRefreshToken());
        } catch (ExpiredJwtException e) {
            throw new AccessDeniedException("토큰이 만료되었습니다. 재로그인이 필요합니다.");
        }
    }

    public HttpHeaders setTokenHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
        return headers;
    }



    public String resolveToken(String accessToken) {

        if (StringUtils.hasText(accessToken) && accessToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
            return accessToken.substring(7);
        }
        throw new NoSuchElementException("토큰 정보가 존재하지 않습니다.재로그인이 필요합니다.");
    }
}
