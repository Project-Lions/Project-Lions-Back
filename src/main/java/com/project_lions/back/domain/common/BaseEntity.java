package com.project_lions.back.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@MappedSuperclass
public class BaseEntity{

  @Column(updatable = false)
  private LocalDateTime created_at;

  private LocalDateTime updated_at;

  @PrePersist
  public void prePersist(){
    LocalDateTime now = LocalDateTime.now();
    created_at = now;
    updated_at = now;
  }

  @PreUpdate
  public void preUpdate() {
    updated_at = LocalDateTime.now();
  }
}
