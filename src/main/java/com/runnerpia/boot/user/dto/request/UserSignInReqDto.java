package com.runnerpia.boot.user.dto.request;

import com.runnerpia.boot.auth.jwt.Authority;
import com.runnerpia.boot.user.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSignInReqDto {

    private String userId;
    private String city;
    private String state;
    private List<String> recommendedTags;
    private List<String> secureTags;

    public User toEntity() {
        return User.builder()
                .userId(this.getUserId())
                .state(this.getState())
                .city(this.getCity())
                .role(Authority.ROLE_USER)
                .build();
    }

}
