package com.runnerpia.boot.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserDataForRouteDto {
  private String nickname;
  private String userId;
}
