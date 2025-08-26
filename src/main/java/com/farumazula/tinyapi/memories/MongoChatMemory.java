package com.farumazula.tinyapi.memories;

import com.farumazula.tinyapi.entity.ChatMessage;
import com.farumazula.tinyapi.repository.ChatMessageRepository;
import lombok.Builder;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.lang.NonNull;

import java.util.Comparator;
import java.util.List;

/**
 * @author Ma1iket
 **/

@Builder
public class MongoChatMemory implements ChatMemory {

    private ChatMessageRepository chatMemoryRepository;
    private int maxMessages;

    @Override
    public void add(@NonNull final String conversationId, final List<Message> messages) {
        chatMemoryRepository.saveAll(messages
                .stream()
                .map(message -> ChatMessage.fromMessage(conversationId, message))
                .filter(chatEntry -> !chatEntry.getContent().isEmpty())
                .toList()).subscribe();
    }

    @NonNull
    @Override
    public List<Message> get(@NonNull final String conversationId) {
        var messages = chatMemoryRepository.findByChatId(conversationId).toStream().toList();
        if (messages.isEmpty()) {
            return List.of();
        }
        var messagesToSkip = Math.max(0, messages.size() - maxMessages);

        return messages.stream()
                .skip(messagesToSkip)
                .sorted(Comparator.comparing(ChatMessage::getCreatedAt))
                .map(ChatMessage::toMessage)
                .toList();
    }

    @Override
    public void clear(@NonNull final String conversationId) {
        //not implemented
    }
}
