package com.runnerpia.boot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runnerpia.boot.running_route.dto.CoordinateDto;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Converter
@Slf4j
public class CoordinateConverter implements AttributeConverter<CoordinateDto, String> {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(CoordinateDto coordinateDto) {
    try {
      return objectMapper.writeValueAsString(coordinateDto);
    } catch (JsonProcessingException e) {
      log.error("Error while parsing DB 데이터: {} :: {}", coordinateDto, e);
    }
    return new String();
  }

  @Override
  public CoordinateDto convertToEntityAttribute(String dbData) {
    try {
      return objectMapper.readValue(dbData, CoordinateDto.class);
    } catch (JsonProcessingException e) {
      log.error("Error while parsing JSON: {} :: {}", dbData, e);
    }
    return new CoordinateDto();
  }
}
