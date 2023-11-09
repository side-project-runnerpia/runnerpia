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
public class UserLoginReqDto {

    private String userId;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .build();
    }
}
