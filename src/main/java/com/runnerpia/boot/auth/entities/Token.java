package com.runnerpia.boot.auth.entities;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Data
@Builder
@AllArgsConstructor
@RedisHash(value = "jwtToken", timeToLive = 60*60*24*7)
public class Token {

    @Id
    private String id;

    @Indexed
    private String accessToken;

    private String refreshToken;

}
