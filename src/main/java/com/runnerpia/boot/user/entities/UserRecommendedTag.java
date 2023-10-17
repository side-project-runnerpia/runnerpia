package com.runnerpia.boot.user.entities;

import com.runnerpia.boot.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "user_recommended_tags")
@Getter
public class UserRecommendedTag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "user_recommended_tag_seq", columnDefinition = "BINARY(16) DEFAULT (UNHEX(REPLACE(UUID(), \"-\", \"\")))")
    private UUID id;

    @Column(name = "`index`")
    private int index;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_seq")
    private User user;

}
