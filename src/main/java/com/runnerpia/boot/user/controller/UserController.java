package com.runnerpia.boot.user.controller;

import com.runnerpia.boot.user.dto.response.UserInfoCheckRespDto;
import com.runnerpia.boot.user.dto.response.UserInfoRespDto;
import com.runnerpia.boot.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 ID 중복  체크")
    @GetMapping("/checkId/{id}")
    public ResponseEntity<UserInfoCheckRespDto> checkId(@PathVariable String id) {
        UserInfoCheckRespDto response = userService.isUserIdExists(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "유저 닉네임 중복  체크")
    @GetMapping("/checkNickname/{nickname}")
    public ResponseEntity<UserInfoCheckRespDto> checkNickname(@PathVariable String nickname) {
        UserInfoCheckRespDto response = userService.isNicknameExists(nickname);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "추천경로 사용횟수 증가")
    @PostMapping("/increaseUseRecommended")
    public ResponseEntity<?> increaseUseRecommended(Authentication authentication) {

        userService.increaseUseRecommended(authentication.getName());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "추천경로 사용횟수 가져오기")
    @GetMapping("/getUseRecommended")
    public ResponseEntity<UserInfoRespDto> getUseRecommended(Authentication authentication) {

        UserInfoRespDto response = userService.getUseRecommended(authentication.getName());
        return ResponseEntity.ok(response);
    }
}
