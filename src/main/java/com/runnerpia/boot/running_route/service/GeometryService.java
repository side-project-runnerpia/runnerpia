package com.runnerpia.boot.running_route.service;

import com.runnerpia.boot.running_route.repository.RunningRouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeometryService {
  public Point createPoint(Double latitude, Double longitude) {
    GeometryFactory geometryFactory = new GeometryFactory();
    Coordinate coordinate = new Coordinate(longitude, latitude);
    return geometryFactory.createPoint(coordinate);
  }

  public Polygon createSquareAroundPoint(Double latitude, Double longitude, int radiusInKm) {
    GeometryFactory geometryFactory = new GeometryFactory();

    double halfSideLength = radiusInKm / (111.32 * Math.cos(Math.toRadians(latitude)));
    Coordinate[] coordinates = new Coordinate[5];
    coordinates[0] = new Coordinate(longitude - halfSideLength, latitude - halfSideLength);
    coordinates[1] = new Coordinate(longitude + halfSideLength, latitude - halfSideLength);
    coordinates[2] = new Coordinate(longitude + halfSideLength, latitude + halfSideLength);
    coordinates[3] = new Coordinate(longitude - halfSideLength, latitude + halfSideLength);
    coordinates[4] = coordinates[0];

    return geometryFactory.createPolygon(coordinates);
  }
}
