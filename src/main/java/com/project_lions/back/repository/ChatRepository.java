package com.project_lions.back.repository;

import com.project_lions.back.domain.Chat;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {

  @Query("{ $or: [ { senderId: ?0, receiverId: ?1 }, { receiverId: ?0, senderId: ?1 } ] }")
  Flux<Chat> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
