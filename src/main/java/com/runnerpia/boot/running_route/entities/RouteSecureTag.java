package com.runnerpia.boot.running_route.entities;

import com.runnerpia.boot.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "route_secure_tags")
@Getter
public class RouteSecureTag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "route_secure_tag_seq", columnDefinition = "BINARY(16) DEFAULT UUID()")
    private UUID id;

    @Column(name = "`index`")
    private int index;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private RunningRoute runningRoute;

}
