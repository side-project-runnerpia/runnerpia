package com.runnerpia.boot.auth.service;

import com.runnerpia.boot.auth.dto.TokenDto;
import com.runnerpia.boot.auth.jwt.JwtProvider;
import com.runnerpia.boot.user.dto.request.UserLoginReqDto;
import com.runnerpia.boot.user.dto.request.UserSignInReqDto;
import com.runnerpia.boot.user.dto.response.UserSignInRespDto;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.user.repository.UserRepository;
import com.runnerpia.boot.user.service.UserTagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserTagService userTagService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    private final String USER_ID = "userId";
    private final String CITY = "city";
    private final String STATE = "state";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String RESOLVE_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final UUID USER_UUID = UUID.randomUUID();


    @Test
    @DisplayName("회원 가입 서비스 테스트")
    public void signInTest() {
        UserSignInReqDto request = new UserSignInReqDto();
        request.setUserId(USER_ID);
        request.setCity(CITY);
        request.setState(STATE);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(UUID.randomUUID());
                    return user;
                });

        when(userTagService.createUserSecureTags(any(UserSignInReqDto.class), any(User.class)))
                .thenReturn(Arrays.asList("Tag1", "Tag2"));

        when(userTagService.createUserRecommendedTags(any(UserSignInReqDto.class), any(User.class)))
                .thenReturn(Arrays.asList("Tag3", "Tag4"));

        UserSignInRespDto response = authService.signIn(request);

        assertNotNull(response);
        assertEquals(USER_ID, response.getUserId());
        assertEquals(CITY, response.getCity());
        assertEquals(STATE, response.getState());
        assertEquals(2, response.getSecureTags().size());
        assertEquals(2, response.getRecommendedTags().size());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("로그인 테스트")
    public void loginTest() {
        UserLoginReqDto request = new UserLoginReqDto();
        request.setUserId(USER_ID);

        User user = request.toEntity();
        user.setId(USER_UUID);

        when(userRepository.findByUserId(USER_ID)).thenReturn(Optional.of(user));

        TokenDto tokenDto = new TokenDto();
        tokenDto.setAccessToken(ACCESS_TOKEN);
        tokenDto.setRefreshToken(REFRESH_TOKEN);

        when(jwtProvider.generateTokenDto(USER_UUID.toString())).thenReturn(tokenDto);

        HttpHeaders headers = authService.login(request);

        assertNotNull(headers);
        assertTrue(headers.containsKey("Authorization"));
        assertEquals("Bearer accessToken", headers.getFirst("Authorization"));
    }



    //TODO: 로그아웃, 갱신 테스트 작성해야 함



}