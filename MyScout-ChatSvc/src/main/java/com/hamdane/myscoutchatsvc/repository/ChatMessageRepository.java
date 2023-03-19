package com.hamdane.myscoutchatsvc.repository;


import com.hamdane.myscoutchatsvc.model.ChatMessage;
import com.hamdane.myscoutchatsvc.model.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    long countBySenderIdAndRecipientIdAndStatus(String senderId, String recipientId, MessageStatus status);

    List<ChatMessage> findByChatId(String chatId);
}