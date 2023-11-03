package com.runnerpia.boot.user.service;

import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.running_route.entities.Tag;
import com.runnerpia.boot.running_route.repository.TagRepository;
import com.runnerpia.boot.user.dto.request.UserInfoReqDto;
import com.runnerpia.boot.user.dto.request.UserSignInReqDto;
import com.runnerpia.boot.user.dto.response.UserInfoCheckRespDto;
import com.runnerpia.boot.user.dto.response.UserInfoRespDto;
import com.runnerpia.boot.user.dto.response.UserSignInRespDto;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.user.entities.UserRecommendedTag;
import com.runnerpia.boot.user.entities.UserSecureTag;
import com.runnerpia.boot.user.repository.UserRecommendedTagRepository;
import com.runnerpia.boot.user.repository.UserRepository;
import com.runnerpia.boot.user.repository.UserSecureTagRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserTagService userTagService;
    private final TagRepository tagRepository;
    private final UserSecureTagRepository userSecureTagRepository;
    private final UserRecommendedTagRepository userRecommendedTagRepository;

    public UserInfoCheckRespDto isUserIdExists(String userId) {
        boolean isExists = userRepository.existsByUserId(userId);
        return new UserInfoCheckRespDto(isExists);
    }

    public UserInfoCheckRespDto isNicknameExists(String nickname) {
        boolean isExists = userRepository.existsByNickname(nickname);
        return new UserInfoCheckRespDto(isExists);
    }

    @Transactional
    public UserSignInRespDto signIn(UserSignInReqDto request) {

        User user = createUser(request);
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
    public void increaseUseRecommended(UUID userUUID) {

        Optional<User> findUser = userRepository.findById(userUUID);

        User user = findUser.get();
        user.setNumberOfUse(user.getNumberOfUse() + 1);
        userRepository.save(user);
    }

    public UserInfoRespDto getUseRecommended(UUID userUUID) {

        Optional<User> findUser = userRepository.findById(userUUID);

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
                .build();
        return userRepository.save(user);
    }

    public List<RunningRoute> findAllRunningRoutesByUser(User user) {
        return userRepository.findAllRunningRoutesByUser(user);
    }
}
