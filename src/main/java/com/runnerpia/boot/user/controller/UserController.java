package com.runnerpia.boot.user.controller;

import com.runnerpia.boot.user.dto.UserInfoDto;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/checkId/{id}")
    public ResponseEntity<?> checkId(@PathVariable String id) {
        userService.isUserIdExists(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/checkNickname/{nickname}")
    public ResponseEntity<?> checkNickname(@PathVariable String nickname) {
        userService.isNicknameExists(nickname);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/increaseUseRecommended")
    public ResponseEntity<?> increaseUseRecommended() {

        //TODO: 토큰에서 id값을 꺼내서 update 하는 방식으로 변경해야함(현재는 임시 값으로)
        User user = userService.createUser(UserInfoDto.Request.builder().userId("userId").build());

        userService.increaseUseRecommended(user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getUseRecommended")
    public ResponseEntity<?> getUseRecommended() {

        //TODO: 토큰에서 id값을 꺼내서 update 하는 방식으로 변경해야함(현재는 임시 값으로)
        User user = userService.createUser(UserInfoDto.Request.builder().userId("userId").build());

        UserInfoDto.Response response = userService.getUseRecommended(user.getId());
        return ResponseEntity.ok().body(response);
    }


}
