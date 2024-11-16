package com.project_lions.back.service;

import com.project_lions.back.apiPayload.code.status.ErrorStatus;
import com.project_lions.back.apiPayload.exception.handler.MemberHandler;
import com.project_lions.back.domain.Chat;
import com.project_lions.back.domain.Member;
import com.project_lions.back.repository.ChatRepository;
import com.project_lions.back.repository.MemberRepository;
import com.project_lions.back.util.security.SecurityUtil;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final ChatRepository chatRepository;
  private final MemberRepository memberRepository;

  public Flux<Chat> getChats(Long targetId) {

    Member user = memberRepository.findByEmail(
        SecurityUtil.getLoginUsername()).orElseThrow(()
        -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

    if(memberRepository.existsById(targetId)){
      throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
    }

    return chatRepository.findBySenderIdAndReceiverId(targetId, user.getId());
  }

  public Mono<Chat> sendMessage(Long receiverId, String message) {

    Chat chat = Chat.builder()
          .receiverId(receiverId)
          .senderId(memberRepository.findByEmail(
              SecurityUtil.getLoginUsername()).orElseThrow(()
              -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND)).getId())
          .message(message)
          .createdAt(LocalDateTime.now())
          .build();

    return chatRepository.save(chat);
  }
}
