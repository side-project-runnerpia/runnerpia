package com.runnerpia.boot.user.service;

import com.runnerpia.boot.user.dto.request.UserInfoReqDto;
import com.runnerpia.boot.user.dto.response.UserInfoCheckRespDto;
import com.runnerpia.boot.user.dto.response.UserInfoRespDto;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.user.repository.UserRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        User user = request.toEntity();
        return userRepository.save(user);
    }
}
