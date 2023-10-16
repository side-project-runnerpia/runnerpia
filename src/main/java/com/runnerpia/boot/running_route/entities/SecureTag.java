package com.runnerpia.boot.running_route.entities;

import com.runnerpia.boot.running_route.entities.util.RouteTag;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "secure_tags")
@Data
public class SecureTag extends RouteTag {
  @EmbeddedId
  private RouteTag routeTag;
}
