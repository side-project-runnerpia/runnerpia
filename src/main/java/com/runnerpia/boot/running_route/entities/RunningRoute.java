package com.runnerpia.boot.running_route.entities;

import com.runnerpia.boot.running_route.dto.response.MainRouteDetailResponseDto;
import com.runnerpia.boot.user.dto.request.UserInfoReqDto;
import com.runnerpia.boot.user.entities.Bookmark;
import com.runnerpia.boot.user.entities.Like;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.util.BaseTimeEntity;
import com.runnerpia.boot.util.GeometryConverter;
import com.runnerpia.boot.util.StringToUuidConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.locationtech.jts.geom.LineString;

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

    @Column(length = 20, unique = true)
    @Unique
    @Size(max = 20, message = "경로 이름은 10자 이하로 입력해 주세요.")
    @Schema(description = "경로 이름", example = "광화문 청계천 앞 마당길")
    private String routeName;

    @Column
    private LineString arrayOfPos;

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
    private Double distance;

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
                .arrayOfPos(GeometryConverter.convertToCoordinateDto(arrayOfPos))
                .review(review)
                .location(location)
                .mainRoute(mainRoute == null ? null : mainRoute.getId())
                .runningTime(runningTime == null ? null : runningTime.toString())
                .runningDate(runningDate == null ? null : runningDate.toString())
                .user(new UserInfoReqDto(
                        user.getUserId(),
                        user.getNickname()))
                .build();
    }
}