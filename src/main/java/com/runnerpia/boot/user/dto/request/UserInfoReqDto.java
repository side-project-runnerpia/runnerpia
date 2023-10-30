package com.runnerpia.boot.user.dto.request;


import com.runnerpia.boot.user.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoReqDto {

    private String userId;
    private String nickname;

    /* Dto -> Entity */
    public User toEntity() {
        return User.builder()
                .userId(userId)
                .nickname(nickname)
                .build();
    }
}
