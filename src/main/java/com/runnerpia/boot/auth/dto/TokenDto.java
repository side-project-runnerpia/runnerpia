package com.runnerpia.boot.auth.dto;


import com.runnerpia.boot.auth.entities.Token;
import com.runnerpia.boot.user.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String accessToken;
    private String refreshToken;

    public Token toEntity() {
        return Token.builder()
                .accessToken(this.accessToken)
                .refreshToken(this.refreshToken)
                .build();
    }
}
