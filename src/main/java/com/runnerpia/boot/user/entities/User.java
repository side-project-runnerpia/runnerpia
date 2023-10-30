package com.runnerpia.boot.user.entities;

import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.util.BaseTimeEntity;
import com.runnerpia.boot.util.StringToUuidConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "USERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "user_seq", columnDefinition = "BINARY(16) DEFAULT (UNHEX(REPLACE(UUID(), \"-\", \"\")))")
    @Convert(converter = StringToUuidConverter.class)
    private UUID id;

    @Column(length = 20)
    private String name;

    @Column(length = 20)
    private String nickname;

    @Column(length = 50, nullable = false, unique = true)
    private String userId;

    @Column(columnDefinition = "INT DEFAULT 0")
    @Setter
    private int numberOfUse;

    @Column
    private LocalDate birthDate;

    @Column
    private String gender;

    @Column
    private String city;

    @Column
    private String state;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<RunningRoute> routeList = new ArrayList<>();

    @OneToMany(mappedBy = "user"
            , fetch = FetchType.LAZY
            , orphanRemoval = true
            , cascade = CascadeType.PERSIST
    )
    private List<UserSecureTag> userSecureTags;

    @OneToMany(mappedBy = "user"
            , fetch = FetchType.LAZY
            , orphanRemoval = true
            , cascade = CascadeType.PERSIST
    )
    private List<UserRecommendedTag> userRecommendedTags;

}
