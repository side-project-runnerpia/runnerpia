package com.runnerpia.boot.util;

import com.runnerpia.boot.running_route.dto.CoordinateDto;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

import java.util.ArrayList;
import java.util.List;

public class GeometryConverter {
  public static LineString convertToLineString(List<CoordinateDto> attribute) {
    if (attribute == null || attribute.size() < 2) {
      throw new IllegalArgumentException("LineString은 최소 2개의 점이 필요합니다.");
    }

    Coordinate[] coordinates = new Coordinate[attribute.size()];
    for (int i = 0; i < attribute.size(); i++) {
      CoordinateDto point = attribute.get(i);
      coordinates[i] = new Coordinate(point.getLongitude(), point.getLatitude());
    }

    GeometryFactory geometryFactory = new GeometryFactory();
    return geometryFactory.createLineString(coordinates);
  }

  public static List<CoordinateDto> convertToCoordinateDto(LineString lineString) {
    Coordinate[] coordinates = lineString.getCoordinates();
    List<CoordinateDto> coordinateDtoList = new ArrayList<>();

    for (Coordinate coordinate : coordinates) {
      CoordinateDto coordinateDto = new CoordinateDto(coordinate.getY(), coordinate.getX());
      coordinateDtoList.add(coordinateDto);
    }

    return coordinateDtoList;
  }

  public static List<CoordinateDto> parseLineString(String lineString) {
    List<CoordinateDto> coordinates = new ArrayList<>();
    String[] points = lineString.replaceAll("LINESTRING\\(|\\)", "").split(",");

    for (String point : points) {
      String[] latLon = point.trim().split(" ");
      Double longitude = Double.parseDouble(latLon[0]);
      Double latitude = Double.parseDouble(latLon[1]);
      coordinates.add(new CoordinateDto(latitude, longitude));
    }

    return coordinates;
  }
}
