package com.runnerpia.boot.user.dto.response;

import com.runnerpia.boot.user.entities.User;
import lombok.Getter;

@Getter
public class UserInfoRespDto {

    private Integer user_numberOfUse;

    /* Entity -> Dto*/
    public UserInfoRespDto(User user) {
        this.user_numberOfUse = user.getNumberOfUse();
    }
}
