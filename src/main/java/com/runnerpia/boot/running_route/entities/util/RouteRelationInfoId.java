package com.runnerpia.boot.running_route.entities.util;

import com.runnerpia.boot.running_route.entities.RunningRoute;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class RouteRelationInfoId implements Serializable {
  @OneToOne
  @JoinColumn(name = "main_route_seq")
  private RunningRoute mainRoute;
  @OneToOne
  @JoinColumn(name = "sub_route_seq")
  private RunningRoute subRoute;
}