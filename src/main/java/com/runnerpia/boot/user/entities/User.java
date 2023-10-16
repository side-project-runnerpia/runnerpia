package com.runnerpia.boot.user.entities;

import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "user_seq", columnDefinition = "BINARY(16) DEFAULT (UNHEX(REPLACE(UUID(), \"-\", \"\")))")
    private UUID id;

    @Column(length = 20)
    private String name;

    @Column(length = 20, nullable = false)
    private final String nickname;

    @Column(length = 50, nullable = false)
    private final String userId;

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

    @OneToMany(mappedBy = "user")
    private List<RunningRoute> routeList = new ArrayList<>();

}
