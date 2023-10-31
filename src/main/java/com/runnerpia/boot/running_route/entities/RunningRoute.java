package com.runnerpia.boot.running_route.entities;

import com.runnerpia.boot.running_route.dto.CoordinateDto;
import com.runnerpia.boot.running_route.dto.MainRouteDetailResponseDto;
import com.runnerpia.boot.user.dto.request.UserInfoReqDto;
import com.runnerpia.boot.user.entities.Bookmark;
import com.runnerpia.boot.user.entities.Like;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.util.BaseTimeEntity;
import com.runnerpia.boot.util.CoordinateConverter;
import com.runnerpia.boot.util.StringToUuidConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "routes")
@Getter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@Builder
@DynamicUpdate
public class RunningRoute extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "route_seq", columnDefinition = "BINARY(16) DEFAULT (UNHEX(REPLACE(UUID(), \"-\", \"\")))")
    @Convert(converter = StringToUuidConverter.class)
    private UUID id;

    @Column(length = 50, unique = true)
    private String routeName;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = CoordinateConverter.class)
    private List<CoordinateDto> arrayOfPos;

    @Column
    @Temporal(TemporalType.TIME)
    private LocalTime runningTime;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime runningDate;

    @Column(length = 100)
    @Setter
    private String review;

    @Column
    private Float distance;

    @Column
    private String location;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    @Setter // 임시
    private User user;

    @OneToMany(mappedBy = "runningRoute", fetch = FetchType.LAZY)
    @Setter // 임시
    private List<Bookmark> bookmarks;

    @OneToMany(mappedBy = "runningRoute", fetch = FetchType.LAZY)
    @Setter // 임시
    private List<Like> likes;

    @OneToMany(mappedBy = "runningRoute"
            , fetch = FetchType.LAZY
            , orphanRemoval = true
            , cascade = CascadeType.PERSIST
    )
    @Setter // 임시
    private List<Image> images;

    @OneToMany(mappedBy = "runningRoute"
            , fetch = FetchType.LAZY
            , orphanRemoval = true
            , cascade = CascadeType.PERSIST
    )
    @Setter
    private List<SecureTag> secureTags;

    @OneToMany(mappedBy = "runningRoute"
            , fetch = FetchType.LAZY
            , orphanRemoval = true
            , cascade = CascadeType.PERSIST
    )
    @Setter
    private List<RecommendTag> recommendTags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_route_seq", columnDefinition = "BINARY(16) DEFAULT NULL")
    @Setter // 임시
    private RunningRoute mainRoute;

    @OneToMany(mappedBy = "mainRoute"
            , fetch = FetchType.LAZY
            , orphanRemoval = true
            , cascade = CascadeType.PERSIST
    )
    private List<RunningRoute> subRoute;

    public MainRouteDetailResponseDto toResponse() {
        return MainRouteDetailResponseDto.builder()
                .routeName(routeName)
                .distance(distance)
                .arrayOfPos(arrayOfPos)
                .review(review)
                .location(location)
                .runningTime(runningTime.toString())
                .runningDate(runningDate.toString())
                .user(new UserInfoReqDto(
                        user.getUserId(),
                        user.getNickname()))
                .build();
    }

}