package com.runnerpia.boot.user.dto;

import com.runnerpia.boot.user.entities.User;
import lombok.*;

public class UserInfoDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
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

    @Getter
    public static class Response {
        private Integer user_numberOfUse;

        /* Entity -> Dto*/
        public Response(User user) {
            this.user_numberOfUse = user.getNumberOfUse();
        }

    }
}
