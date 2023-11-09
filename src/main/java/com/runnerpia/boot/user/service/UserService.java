package com.runnerpia.boot.user.service;

import com.runnerpia.boot.auth.jwt.Authority;
import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.user.dto.request.UserInfoReqDto;
import com.runnerpia.boot.user.dto.request.UserSignInReqDto;
import com.runnerpia.boot.user.dto.response.UserInfoCheckRespDto;
import com.runnerpia.boot.user.dto.response.UserInfoRespDto;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfoCheckRespDto isUserIdExists(String userId) {
        boolean isExists = userRepository.existsByUserId(userId);
        return new UserInfoCheckRespDto(isExists);
    }

    public UserInfoCheckRespDto isNicknameExists(String nickname) {
        boolean isExists = userRepository.existsByNickname(nickname);
        return new UserInfoCheckRespDto(isExists);
    }

    @Transactional
    public void increaseUseRecommended(String userUUID) {

        Optional<User> findUser = userRepository.findById(UUID.fromString(userUUID));
        User user = findUser.get();
        user.setNumberOfUse(user.getNumberOfUse() + 1);
        userRepository.save(user);
    }

    public UserInfoRespDto getUseRecommended(String userUUID) {
        Optional<User> findUser = userRepository.findById(UUID.fromString(userUUID));
        User user = findUser.get();
        return new UserInfoRespDto(user);
    }

    //임시
    @Transactional
    public User createUser(UserInfoReqDto request) {
        User user = User.builder()
                .userId(request.getUserId())
                .nickname(request.getNickname())
                .build();
        return userRepository.save(user);
    }

    @Transactional
    public User createUser(UserSignInReqDto request) {
        User user = User.builder()
                .userId(request.getUserId())
                .city(request.getCity())
                .state(request.getState())
                .role(Authority.ROLE_USER)
                .build();
        return userRepository.save(user);
    }

    public List<RunningRoute> findAllRunningRoutesByUser(User user) {
        return userRepository.findAllRunningRoutesByUser(user);
    }
}
