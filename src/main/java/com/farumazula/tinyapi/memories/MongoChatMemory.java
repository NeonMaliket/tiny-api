package com.farumazula.tinyapi.memories;

import com.farumazula.tinyapi.entity.ChatEntry;
import com.farumazula.tinyapi.repository.ChatRepository;
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

    private ChatRepository chatMemoryRepository;
    private int maxMessages;

    @Override
    public void add(@NonNull String conversationId, List<Message> messages) {
        chatMemoryRepository.addChatEntries(conversationId,
                messages
                        .stream()
                        .map(ChatEntry::fromMessage)
                        .filter(chatEntry -> !chatEntry.getContent().isEmpty())
                        .toList()).blockOptional();
    }

    @NonNull
    @Override
    public List<Message> get(@NonNull String conversationId) {
        var chat = chatMemoryRepository.findById(conversationId).blockOptional();
        if (chat.isEmpty()) {
            return List.of();
        }
        var messages = chat.get().getHistory();
        var messagesToSkip = Math.max(0, messages.size() - maxMessages);

        return messages.stream()
                .skip(messagesToSkip)
                .sorted(Comparator.comparing(ChatEntry::getCreatedAt))
                .map(ChatEntry::toMessage)
                .toList();
    }

    @Override
    public void clear(@NonNull String conversationId) {
        //not implemented
    }
}
