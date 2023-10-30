package com.runnerpia.boot.user.entities;

import com.runnerpia.boot.running_route.entities.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserTag {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_seq")
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;
}
