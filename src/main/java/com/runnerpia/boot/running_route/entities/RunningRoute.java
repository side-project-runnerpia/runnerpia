package com.runnerpia.boot.running_route.entities;

import com.runnerpia.boot.running_route.dto.CoordinateDto;
import com.runnerpia.boot.user.entities.Bookmark;
import com.runnerpia.boot.user.entities.Like;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.util.BaseTimeEntity;
import com.runnerpia.boot.util.CoordinateConverter;
import com.runnerpia.boot.util.StringToUuidConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "routes")
@Getter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@Builder
@ToString
public class RunningRoute extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "route_seq", columnDefinition = "BINARY(16) DEFAULT (UNHEX(REPLACE(UUID(), \"-\", \"\")))")
    @Convert(converter = StringToUuidConverter.class)
    private UUID id; // 식별자 id

    @Column(length = 50, unique = true)
    private String routeName;

    @Column(columnDefinition = "JSON")
    @Convert(converter = CoordinateConverter.class)
    private List<CoordinateDto> arrayOfPos;

    @Column
    @Temporal(TemporalType.TIME)
    private LocalTime runningTime;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime runningDate;

    @Column(length = 100)
    private String review;

    @Column
    private float distance;

    @Column
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @OneToMany(mappedBy = "runningRoute", fetch = FetchType.LAZY)
    private List<Bookmark> bookmarks;

    @OneToMany(mappedBy = "runningRoute", fetch = FetchType.LAZY)
    private List<Like> likes;

    @OneToMany(mappedBy = "runningRoute", fetch = FetchType.LAZY)
    private List<Image> images;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<RouteRelationInfo> relationInfo = new HashSet<>();

}