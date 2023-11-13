package com.runnerpia.boot.running_route.entities;

import com.runnerpia.boot.util.BaseTimeEntity;
import com.runnerpia.boot.util.StringToUuidConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "images")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "image_seq", columnDefinition = "BINARY(16) DEFAULT (UNHEX(REPLACE(UUID(), \"-\", \"\")))")
    @Convert(converter = StringToUuidConverter.class)
    private UUID id;

    @Column
    @NotBlank
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_seq")
    @NotNull
    private RunningRoute runningRoute;
}
