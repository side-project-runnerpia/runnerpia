package com.runnerpia.boot.auth.controller;

import com.runnerpia.boot.auth.service.AuthService;
import com.runnerpia.boot.user.dto.request.UserLoginReqDto;
import com.runnerpia.boot.user.dto.request.UserSignInReqDto;
import com.runnerpia.boot.user.dto.response.UserSignInRespDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserLoginReqDto request) {
        String userId = request.getUserId();
        HttpHeaders headers = authService.login(userId);
        if(headers != null) {
            return ResponseEntity.ok().headers(headers).build();
        }

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원가입")
    @PostMapping("/signIn")
    public ResponseEntity<UserSignInRespDto> signIn(@RequestBody UserSignInReqDto request) {

        UserSignInRespDto response = authService.signIn(request);
        HttpHeaders headers = authService.login(response.getUserId());

        return ResponseEntity.ok().headers(headers).body(response);
    }

    @Operation(summary = "엑세스 토큰 갱신")
    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(@RequestHeader("Authorization") String accessToken) {

        HttpHeaders headers = authService.refresh(accessToken);
        return ResponseEntity.ok().headers(headers).build();
    }

    @Operation(summary = "로그아웃")
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken) {

        authService.logout(accessToken);
        return ResponseEntity.ok().build();
    }



}
