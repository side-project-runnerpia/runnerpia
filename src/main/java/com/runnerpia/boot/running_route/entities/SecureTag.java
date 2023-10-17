package com.runnerpia.boot.running_route.entities;

import com.runnerpia.boot.util.StringToUuidConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "secure_tags")
@Data
@EqualsAndHashCode(callSuper = true)
public class SecureTag extends RouteTag {
  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name="uuid2", strategy = "uuid2")
  @Column(name = "secure_seq", columnDefinition = "BINARY(16) DEFAULT (UNHEX(REPLACE(UUID(), \"-\", \"\")))")
  @Convert(converter = StringToUuidConverter.class)
  private UUID id;
}
