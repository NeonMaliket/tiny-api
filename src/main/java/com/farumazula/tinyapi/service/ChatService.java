package com.farumazula.tinyapi.service;

import com.farumazula.tinyapi.dto.*;
import com.farumazula.tinyapi.entity.Chat;
import com.farumazula.tinyapi.entity.ChatMessage;
import com.farumazula.tinyapi.events.StreamMessagesEvent;
import com.farumazula.tinyapi.repository.ChatRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
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
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatClient chatClient;
    @Autowired
    private ReactiveMongoTemplate mongo;

    public Flux<SimpleChatDto> findAllChats() {
        log.debug("findAllChats");
        return chatRepository.findAll(
                Sort.by(
                        Sort.Direction.DESC,
                        "createdAt")
        ).map(SimpleChatDto::from);
    }

    public Mono<ChatDto> findChatById(String id) {
        log.debug("findChatById");
        return chatRepository.findById(id).map(ChatDto::from);
    }

    public Mono<SimpleChatDto> createChat(NewChatDto newChatDto) {
        log.info("Creating chat {}", newChatDto);
        var saved = chatRepository.save(newChatDto.asChat());
        return saved.map(SimpleChatDto::from);
    }

    public Mono<Void> deleteChat(String chatId) {
        log.info("Deleting chat with id: {}", chatId);
        return chatRepository.deleteById(chatId);
    }

    public Flux<MessageChunk> streamMessage(NewChatMessageDto messageRequest) {
        log.info("Streaming message for: {}", messageRequest);
        var chatId = messageRequest.chatId();
        return chatClient.prompt(messageRequest.prompt())
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .chatResponse()
                .flatMap(response -> {
                    var metadata = response.getMetadata();
                    var usage = metadata.getUsage();
                    var text = response.getResult().getOutput().getText();
                    var isLast = usage.getCompletionTokens() != 0;
                    return Mono.just(new MessageChunk(text, isLast));
                });
    }

    public Flux<ServerSentEvent<ChatMessageDto>> streamMessages(String chatId) {
        log.info("Streaming chat history for: {}", chatId);
        Flux<ServerSentEvent<ChatMessageDto>> history =
                chatRepository.findById(chatId)
                        .flatMapMany(c -> Flux.fromIterable(c.getHistory()))
                        .sort(Comparator.comparing(ChatMessage::getCreatedAt))
                        .map(ChatMessageDto::from)
                        .map(StreamMessagesEvent.HISTORY::toSentEvent);
        var live = mongo
                .changeStream(Chat.class)
                .watchCollection(Chat.class)
                .listen()
                .filter(x -> x.getBody() != null && !x.getBody().getHistory().isEmpty())
                .map(x -> x.getBody().getHistory().getLast())
                .map(entry -> StreamMessagesEvent.NEW_MESSAGE.toSentEvent(ChatMessageDto.from(entry)));
        return Flux.concat(history, live);
    }


    public Flux<String> proceedInteraction(NewChatMessageDto newChatMessageDto) {
        log.info("Sending chat prompt {}", newChatMessageDto);
        var chatId = newChatMessageDto.chatId();

        return chatClient.prompt(newChatMessageDto.prompt())
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .chatResponse()
                .map(response -> {
                    if (response.getResult() == null ||
                            response.getResult().getOutput() == null ||
                            response.getResult().getOutput().getText() == null) {
                        return "";
                    }
                    return response.getResult().getOutput().getText();
                });
    }
}

