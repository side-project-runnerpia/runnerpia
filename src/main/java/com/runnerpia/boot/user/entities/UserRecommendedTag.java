package com.runnerpia.boot.user.entities;

import com.runnerpia.boot.running_route.entities.Tag;
import com.runnerpia.boot.util.BaseTimeEntity;
import com.runnerpia.boot.util.StringToUuidConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "user_recommended_tags")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRecommendedTag extends UserTag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "user_recommended_tag_seq", columnDefinition = "BINARY(16) DEFAULT (UNHEX(REPLACE(UUID(), \"-\", \"\")))")
    @Convert(converter = StringToUuidConverter.class)
    private UUID id;

    public UserRecommendedTag(Tag tag, User user) {super(tag, user);}

}
