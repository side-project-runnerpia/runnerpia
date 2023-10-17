package com.runnerpia.boot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runnerpia.boot.running_route.dto.CoordinateDto;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Converter
@Slf4j
public class CoordinateConverter implements AttributeConverter<List<CoordinateDto>, String> {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(List<CoordinateDto> coordinateDtoList) {
    try {
      return objectMapper.writeValueAsString(coordinateDtoList);
    } catch (JsonProcessingException e) {
      log.error("Error while parsing DB 데이터: {} :: {}", coordinateDtoList, e);
    }
    return new String();
  }

  @Override
  public List<CoordinateDto> convertToEntityAttribute(String dbData) {
    try {
      return objectMapper.readValue(dbData, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      log.error("Error while parsing JSON: {} :: {}", dbData, e);
    }
    return Collections.singletonList(new CoordinateDto());
  }
}
