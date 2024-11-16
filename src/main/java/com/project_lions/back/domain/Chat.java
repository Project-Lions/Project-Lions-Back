package com.project_lions.back.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection = "chat")
public class Chat {

  @Id
  private String id;
  private Long shopId;
  private Long customerId;
  private Long senderId;
  private String message;
  private LocalDateTime createdAt;
}
