package com.runnerpia.boot.running_route.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CheckRunningExperienceDto(@JsonProperty("check") Boolean check) {
}
