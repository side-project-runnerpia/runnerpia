package com.runnerpia.boot.running_route.entities;

import com.runnerpia.boot.user.entities.Bookmark;
import com.runnerpia.boot.user.entities.Like;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.locationtech.jts.geom.Geometry;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "running_routes")
@Getter
@NoArgsConstructor
public class RunningRoute extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "running_route_seq", columnDefinition = "BINARY(16) DEFAULT UUID()")
    private UUID id;

    @Column(length = 50, unique = true)
    private String routeName;

    @Column(columnDefinition = "POINT")
    private Geometry startPoint;

    @Column(columnDefinition = "LINESTRING")
    private Geometry arrayOfPos;

    @Column
    @Temporal(TemporalType.TIME)
    private String runningTime;

    @Column(length = 100)
    private String review;

    @Column
    private float distance;

    @Column
    private String routeImage;

    @Column(name = "`key`")
    private String key;

    @Column
    private String firstLocation;

    @Column
    private String secondLocation;

    @Column
    private String thirdLocation;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private User user;

    @OneToMany(mappedBy = "runningRoute")
    private List<Bookmark> bookmarks;

    @OneToMany(mappedBy = "runningRoute")
    private List<Like> likes;

    @OneToMany(mappedBy = "runningRoute")
    private List<RouteRecommendedTag> routeRecommendedTags;

    @OneToMany(mappedBy = "runningRoute")
    private List<RouteSecureTag> routeSecureTags;

    @OneToMany(mappedBy = "runningRoute")
    private List<Image> images;

    @ManyToOne
    @JoinColumn(name = "main_route_seq")
    private RunningRoute mainRoute;

    @OneToMany(mappedBy = "mainRoute")
    private List<RunningRoute> subRoute;

}