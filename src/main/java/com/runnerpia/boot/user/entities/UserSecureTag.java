package com.runnerpia.boot.user.entities;

import com.runnerpia.boot.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "user_secure_tags")
@Getter
public class UserSecureTag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "user_secure_tag_seq", columnDefinition = "BINARY(16) DEFAULT UUID()")
    private UUID id;

    @Column(name = "`index`")
    private int index;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_seq")
    private User user;

}
