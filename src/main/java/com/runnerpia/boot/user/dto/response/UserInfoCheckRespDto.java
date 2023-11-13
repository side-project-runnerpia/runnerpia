package com.runnerpia.boot.user.dto.response;

import lombok.Getter;

@Getter
public class UserInfoCheckRespDto {

    private Boolean isExists;

    /* Entity -> Dto*/
    public UserInfoCheckRespDto(Boolean isExists) {
        this.isExists = isExists;
    }
}
