package com.project_lions.back.controller;

import com.project_lions.back.domain.Chat;
import com.project_lions.back.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

  private final ChatService chatService;

  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Chat> getChats(@RequestParam Long receiverId) {
    Flux<Chat> chatFlux = chatService.getChats(receiverId)
        .subscribeOn(Schedulers.boundedElastic());
    return chatFlux;
  }

  @PostMapping
  public Mono<Chat> sendMessage(@RequestParam Long receiverId, @RequestBody String message) {
    return chatService.sendMessage(receiverId, message);
  }
}
