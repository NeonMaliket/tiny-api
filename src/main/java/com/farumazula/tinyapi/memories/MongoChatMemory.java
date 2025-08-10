package com.farumazula.tinyapi.memories;

import com.farumazula.tinyapi.entity.ChatEntry;
import com.farumazula.tinyapi.repository.ChatRepository;
import lombok.Builder;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

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
    public void add(String conversationId, List<Message> messages) {
        chatMemoryRepository.addChatEntries(conversationId,
                messages
                        .stream()
                        .map(ChatEntry::fromMessage)
                        .toList());
    }

    @Override
    public List<Message> get(String conversationId) {
        var chat = chatMemoryRepository.findById(conversationId);
        if (chat.isEmpty()) {
            return List.of();
        }
        var messages = chat.get().getHistory();

        return messages.stream()
                .sorted(Comparator.comparing(ChatEntry::getCreatedAt))
                .map(ChatEntry::toMessage)
                .toList();
    }

    @Override
    public void clear(String conversationId) {
        //not implemented
    }
}
