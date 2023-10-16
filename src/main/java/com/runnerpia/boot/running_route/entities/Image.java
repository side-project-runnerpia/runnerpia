package com.runnerpia.boot.running_route.entities;

import com.runnerpia.boot.util.BaseTimeEntity;
import com.runnerpia.boot.util.StringToUuidConverter;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "images")
@Getter
public class Image extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "image_seq", columnDefinition = "BINARY(16) DEFAULT (UNHEX(REPLACE(UUID(), \"-\", \"\")))")
    @Convert(converter = StringToUuidConverter.class)
    private UUID id;

    @Column
    private String url;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "route_seq")
    private RunningRoute runningRoute;
}
