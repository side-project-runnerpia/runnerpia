package com.runnerpia.boot.user.entities;

import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "bookmarks")
@Getter
public class Bookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "route_seq")
    private RunningRoute runningRoute;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_seq")
    private User user;
}