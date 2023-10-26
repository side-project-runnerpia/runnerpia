package com.runnerpia.boot.user.service;

import com.runnerpia.boot.user.dto.UserInfoDto;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.user.repository.UserRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean isUserIdExists(String userId) {
        boolean isExists = userRepository.existsByUserId(userId);
        if(isExists) throw new DataIntegrityViolationException("이미 존재하는 아이디입니다.");
        return false;
    }

    public boolean isNicknameExists(String nickname) {
        boolean isExists = userRepository.existsByNickname(nickname);
        if(isExists) throw new DataIntegrityViolationException("이미 존재하는 닉네임입니다.");
        return false;
    }

    @Transactional
    public void increaseUseRecommended(String userId) {

        Optional<User> findUser = userRepository.findByUserId(userId);
        if(!findUser.isPresent()) throw new NoResultException("사용자를 찾을 수 없습니다.");

        User user = findUser.get();
        user.setNumberOfUse(user.getNumberOfUse() + 1);
        userRepository.save(user);
    }

    public UserInfoDto.Response getUseRecommended(String userId) {

        Optional<User> findUser = userRepository.findByUserId(userId);
        if(!findUser.isPresent()) throw new NoResultException("사용자를 찾을 수 없습니다.");

        User user = findUser.get();
        UserInfoDto.Response response = new UserInfoDto.Response(user);

        return response;
    }

    //임시
    @Transactional
    public void createUser(UserInfoDto.Request request) {
        User user = request.toEntity();
        userRepository.save(user);
    }
}
