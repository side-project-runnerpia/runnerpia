package com.runnerpia.boot.running_route.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TagStatus implements BaseEnumCode<String> {

  SECURE("안심태그"),
  RECOMMEND("추천태그");

  private final String status;
  public static final TagStatus DEFAULT = SECURE;

  @Override
  public String getValue() {
    return this.status;
  }
}

