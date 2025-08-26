package com.farumazula.tinyapi.service;

import com.farumazula.tinyapi.dto.ChatMessageDto;
import com.farumazula.tinyapi.dto.MessageChunk;
import com.farumazula.tinyapi.dto.NewChatMessageDto;
import com.farumazula.tinyapi.entity.ChatMessage;
import com.farumazula.tinyapi.events.StreamMessagesEvent;
import com.farumazula.tinyapi.exceptions.ChatNotFoundException;
import com.farumazula.tinyapi.repository.ChatMessageRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

/**
 * @author Ma1iket
 **/

@Log4j2
@Service
public class ChatMessageService {

    @Autowired
    private ChatService chatService;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ChatClient chatClient;
    @Autowired
    private ReactiveMongoTemplate mongo;

    public Flux<MessageChunk> streamMessage(NewChatMessageDto messageRequest) {
        log.info("Streaming message for: {}", messageRequest);
        var chatId = messageRequest.chatId();

        return chatService.findChatById(chatId).switchIfEmpty(Mono.error(new ChatNotFoundException(chatId)))
                .thenMany(chatClient.prompt(messageRequest.prompt())
                        .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                        .stream()
                        .chatResponse()
                        .flatMap(response -> {
                            var metadata = response.getMetadata();
                            var usage = metadata.getUsage();
                            var text = response.getResult().getOutput().getText();
                            var isLast = usage.getCompletionTokens() != 0;
                            return Mono.just(new MessageChunk(text, isLast));
                        }));
    }

    public Flux<ServerSentEvent<ChatMessageDto>> streamMessages(String chatId) {
        log.info("Streaming chat history for: {}", chatId);
        Flux<ServerSentEvent<ChatMessageDto>> history =
                chatMessageRepository.findByChatId(chatId)
                        .sort(Comparator.comparing(ChatMessage::getCreatedAt))
                        .map(ChatMessageDto::from)
                        .map(StreamMessagesEvent.HISTORY::toSentEvent);
        var live = mongo
                .changeStream(ChatMessage.class)
                .watchCollection(ChatMessage.class)
                .filter(Criteria.where("chatId").is(chatId))
                .listen()
                .filter(x -> x.getBody() != null)
                .map(ChangeStreamEvent::getBody)
                .map(entry -> StreamMessagesEvent.NEW_MESSAGE.toSentEvent(ChatMessageDto.from(entry)));
        return Flux.concat(history, live);
    }
}
