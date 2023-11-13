package com.runnerpia.boot.user.entities;

import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bookmarks", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "route_seq", "user_seq" })
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Bookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "route_seq")
    private RunningRoute runningRoute;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_seq")
    private User user;
}