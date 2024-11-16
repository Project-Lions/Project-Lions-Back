package com.project_lions.back.repository;

import com.project_lions.back.domain.Chat;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {

  @Query("{ $or: [ { shopId: ?0, customerId: ?1 }, { customerId: ?0, shopId: ?1 } ] }")
  Flux<Chat> findByShopIdAndCustomerId(Long shopId, Long customerId);
}
