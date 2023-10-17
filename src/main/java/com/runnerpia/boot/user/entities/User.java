package com.runnerpia.boot.user.entities;

import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "user_seq", columnDefinition = "BINARY(16) DEFAULT (UNHEX(REPLACE(UUID(), \"-\", \"\")))")
    private UUID id;

    @Column(length = 20)
    private String name;

    @Column(length = 20, nullable = false)
    private String nickname;

    @Column(length = 50, nullable = false)
    private String userId;

    @Column
    private String password;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int numberOfUse;

    @Column
    private int birthDate;

    @Column
    private String gender;

    @Column
    private String city;

    @Column
    private String state;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<RunningRoute> routeList = new ArrayList<>();

}
