package com.runnerpia.boot.auth.jwt;

public interface JwtProperties {
    String SECRET = "Q4NSl604sgyHJj1qwEkR3ycUeR4uUAt7WJraD7EN3O9DVM4yyYuHxMEbSF4XXyYJkal13eqgB0F7Bq4HdfjieBUIEWBGNKdfkdlf"; // 우리 서버만 알고 있는 비밀값
    long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60; //1시간
    long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; //7일

    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";

    String ROLE_KEY = "role";
}