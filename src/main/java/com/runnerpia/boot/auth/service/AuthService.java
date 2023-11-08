package com.runnerpia.boot.auth.service;

import com.runnerpia.boot.auth.entities.Token;
import com.runnerpia.boot.auth.jwt.JwtProperties;
import com.runnerpia.boot.auth.jwt.JwtProvider;
import com.runnerpia.boot.auth.dto.TokenDto;
import com.runnerpia.boot.user.dto.request.UserLoginReqDto;
import com.runnerpia.boot.user.dto.request.UserSignInReqDto;
import com.runnerpia.boot.user.dto.response.UserSignInRespDto;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.user.repository.UserRepository;
import com.runnerpia.boot.user.service.UserTagService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final UserTagService userTagService;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    @Transactional
    public UserSignInRespDto signIn(UserSignInReqDto request) {

        User user = userRepository.save(request.toEntity());
        List<String> userSecureTags = userTagService.createUserSecureTags(request, user);
        List<String> userRecommendedTags = userTagService.createUserRecommendedTags(request, user);



        return UserSignInRespDto.builder()
                .userId(user.getUserId())
                .city(user.getCity())
                .state(user.getState())
                .secureTags(userSecureTags)
                .recommendedTags(userRecommendedTags)
                .build();
    }

    @Transactional
    public HttpHeaders login(UserLoginReqDto request) {

        Optional<User> findUser = userRepository.findByUserId(request.getUserId());

        if(findUser.isEmpty()) {
            throw new NoSuchElementException("회원가입이 필요합니다.");
        }

        String userUUID = findUser.get().getId().toString();
        TokenDto tokenDto = jwtProvider.generateTokenDto(userUUID);
        tokenService.saveAccessAndRefreshToken(tokenDto.toEntity());

        return setTokenHeaders(tokenDto.getAccessToken());
    }

    @Transactional
    public HttpHeaders refresh(String accessToken) {

        String resolveToken = resolveToken(accessToken);
        Token token = validateRefreshToken(resolveToken);
        String newAccessToken = tokenService.regenerateAccessToken(resolveToken, token.getId());

        return setTokenHeaders(newAccessToken);
    }

    @Transactional
    public void logout(String accessToken) {

        String resolveToken = resolveToken(accessToken);
        tokenService.removeRefreshToken(resolveToken);
    }



    private HttpHeaders setTokenHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
        return headers;
    }

    public Token validateRefreshToken(String accessToken){

        Token tokenEntity = tokenService.getTokenEntity(accessToken);

        if(tokenEntity == null)
            throw new NoSuchElementException("토큰 정보가 존재하지 않습니다.재로그인이 필요합니다.");

        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtProvider.getKey())
                    .build()
                    .parseClaimsJws(tokenEntity.getRefreshToken());
            return tokenEntity;
        } catch (ExpiredJwtException e) {
            throw new AccessDeniedException("토큰이 만료되었습니다. 재로그인이 필요합니다.");
        }
    }

    public String resolveToken(String accessToken) {

        if (StringUtils.hasText(accessToken) && accessToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
            return accessToken.substring(7);
        }
        throw new NoSuchElementException("토큰 정보가 존재하지 않습니다.재로그인이 필요합니다.");
    }
}
