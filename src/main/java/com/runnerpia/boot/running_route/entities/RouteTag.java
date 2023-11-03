package com.runnerpia.boot.running_route.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RouteTag implements Serializable {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tag_seq")
  private Tag tag;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "route_seq")
  private RunningRoute runningRoute;
}
