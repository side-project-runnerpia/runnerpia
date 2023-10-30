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
@Table(name = "user_secure_tags")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserSecureTag extends UserTag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "user_secure_tag_seq", columnDefinition = "BINARY(16) DEFAULT (UNHEX(REPLACE(UUID(), \"-\", \"\")))")
    @Convert(converter = StringToUuidConverter.class)
    private UUID id;

    public UserSecureTag(Tag tag, User user) {super(tag, user);}

}
