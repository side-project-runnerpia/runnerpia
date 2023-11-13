package com.runnerpia.boot.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserSignInRespDto {

    private String userId;
    private String city;
    private String state;
    private List<String> recommendedTags;
    private List<String> secureTags;

}
