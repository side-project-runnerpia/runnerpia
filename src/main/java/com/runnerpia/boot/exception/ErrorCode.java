package com.runnerpia.boot.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INDEX_NOT_FOUND(1001, "인덱스가 존재하지 않습니다."),
    UNKNOWN_ERROR(1002, "알 수 없는 에러가 발생했습니다."),
    WRONG_TYPE_TOKEN(1003, "변조된 토큰입니다."),
    EXPIRED_TOKEN(1004, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(1005, "변조된 토큰입니다."),
    ACCESS_DENIED(1006, "권한이 없습니다.");

    private Integer code;
    private String message;


}