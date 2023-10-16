package com.runnerpia.boot.running_route.dto;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateRunningRouteResponseDtoTest {

  @Test
  public void 러닝루트_생성_응답_테스트() {
    // given
    UUID id = UUID.randomUUID();

    // when
    CreateRunningRouteResponseDto dto = new CreateRunningRouteResponseDto(id);

    // then
    assertThat(dto.getId()).isEqualTo(id);
  }

}
