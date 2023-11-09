package com.runnerpia.boot.auth.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Authority {

    ROLE_ADMIN("ROLE_ADMIN", "관리자"),
    ROLE_USER("ROLE_USER", "일반사용자");

    private final String key;
    private final String title;
}