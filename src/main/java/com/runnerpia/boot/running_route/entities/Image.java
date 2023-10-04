package com.runnerpia.boot.running_route.entities;

import com.runnerpia.boot.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "images")
@Getter
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "image_seq", columnDefinition = "BINARY(16) DEFAULT UUID()")
    private UUID id;

    @Column
    private String url;

    @Column(name = "`key`")
    private String key;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private RunningRoute runningRoute;

}
