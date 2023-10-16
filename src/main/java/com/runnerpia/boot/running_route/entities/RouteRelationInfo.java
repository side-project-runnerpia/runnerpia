package com.runnerpia.boot.running_route.entities;

import com.runnerpia.boot.running_route.entities.util.RouteRelationInfoId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "route_relation_infos")
@Data
public class RouteRelationInfo {
  @EmbeddedId
  private RouteRelationInfoId routeRelationId;
}
