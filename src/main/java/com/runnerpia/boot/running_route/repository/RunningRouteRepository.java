package com.runnerpia.boot.running_route.repository;

import com.runnerpia.boot.running_route.dto.CoordinateDto;
import com.runnerpia.boot.running_route.dto.response.SearchNearbyRouteResponseDto;
import com.runnerpia.boot.running_route.entities.RunningRoute;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.runnerpia.boot.util.GeometryConverter.parseLineString;

@Repository
public interface RunningRouteRepository extends JpaRepository<RunningRoute, UUID> {
  RunningRoute findTop1ByOrderByCreatedDateDesc();
  Optional<RunningRoute> findByRouteName(String routeName);
  List<RunningRoute> findAllByIdOrMainRoute(UUID id, RunningRoute mainRoute);
  Boolean existsByRouteName(String routeName);
  boolean existsById(UUID id);

  @Query(value = "SELECT r.id AS seq, r.routeName AS routeName, r.location AS location" +
          ", ST_X(ST_StartPoint(r.arrayOfPos)) AS longitude" +
          ", ST_Y(ST_StartPoint(r.arrayOfPos)) AS latitude" +
          ", ST_AsText(r.arrayOfPos) AS course" +
          ", ST_Length(r.arrayOfPos) AS distance" +
          ", ST_Distance_Sphere(ST_STARTPOINT(r.arrayOfPos), :point) AS distanceFromMyPosition " +
          "FROM RunningRoute r " +
          "WHERE ST_CONTAINS(ST_BUFFER(ST_STARTPOINT(r.arrayOfPos), :radius), :point) " +
          "AND r.mainRoute IS NULL")
  List<Map<String, Object>> findRoutesWithinRadius(@Param("point") Point point, @Param("radius") int radius);

  default List<SearchNearbyRouteResponseDto> findNearbyRouteList(Point point, int radius) {
    List<Map<String, Object>> result = findRoutesWithinRadius(point, radius);
    return result.stream().map(row -> SearchNearbyRouteResponseDto.builder()
            .seq((UUID) row.get("seq"))
            .routeName(row.get("routeName").toString())
            .location(row.get("location").toString())
            .startPoint(new CoordinateDto((Double) row.get("latitude"), (Double) row.get("longitude")))
            .course(parseLineString(row.get("course").toString()))
            .distance((Double) row.get("distance"))
            .distanceFromMyPosition((Double) row.get("distanceFromMyPosition"))
            .build()).collect(Collectors.toList());
  }
}
