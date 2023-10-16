package com.runnerpia.boot.running_route.entities.util;

import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.running_route.entities.Tag;
import jakarta.persistence.*;

import java.io.Serializable;

public abstract class RouteTag implements Serializable {
  @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  @JoinColumn(name = "tag_seq")
  private Tag tag;

  @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  @JoinColumn(name = "route_seq")
  private RunningRoute runningRoute;
}
