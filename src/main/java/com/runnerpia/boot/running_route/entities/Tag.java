package com.runnerpia.boot.running_route.entities;

import com.runnerpia.boot.running_route.entities.enums.TagStatus;
import com.runnerpia.boot.util.StringToUuidConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "tags")
@Getter
@ToString
public class Tag {
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name="uuid2", strategy = "uuid2")
  @Column(name = "tag_seq", columnDefinition = "BINARY(16) DEFAULT (UNHEX(REPLACE(UUID(), \"-\", \"\")))")
  @Convert(converter = StringToUuidConverter.class)
  private UUID id;

  @Column(unique = true)
  @NotBlank
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "ENUM('SECURE', 'RECOMMEND') DEFAULT 'RECOMMEND'")
  @NotBlank
  private TagStatus status;

  public Tag(String description, TagStatus status) {
    this.description = description;
    this.status = status;
  }

  public Tag() {

  }
}
