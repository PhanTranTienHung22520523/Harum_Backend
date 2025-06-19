package com.Harum.Harum.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Harum.Harum.Models.Conversations;
import com.Harum.Harum.Models.Messages;
import com.Harum.Harum.Repository.ConversationRepo;
import com.Harum.Harum.Repository.MessageRepo;

import java.util.*;

@Service
public class MessageService {
    @Autowired
    private ConversationRepo conversationRepo;

    @Autowired
    private MessageRepo messageRepo;

    public Conversations getOrCreateConversation(String senderId, String receiverId) {
        return conversationRepo.findByUser1IdAndUser2Id(senderId, receiverId)
                .or(() -> conversationRepo.findByUser2IdAndUser1Id(senderId, receiverId))
                .orElseGet(() -> {
                    Conversations conv = new Conversations(senderId, receiverId);
                    return conversationRepo.save(conv);
                });
    }

    public Messages sendMessage(String senderId, String receiverId, String messageText) {
        Conversations conv = getOrCreateConversation(senderId, receiverId);
        Messages msg = new Messages(conv.getId(), new Date());
        msg.setConversationId(conv.getId());
        msg.setSenderId(senderId);
        msg.setReceiverId(receiverId);
        msg.setSendAt(new Date().toInstant().toString());
        msg.setReceivedAt(new Date());
        msg.setContent(messageText);
        return messageRepo.save(msg);
    }

    public List<Messages> getMessages(String conversationId) {
        return messageRepo.findByConversationId(conversationId);
    }

    public Optional<Conversations> getConversation(String senderId, String receiverId) {
        return conversationRepo.findByUser1IdAndUser2Id(senderId, receiverId)
                .or(() -> conversationRepo.findByUser2IdAndUser1Id(senderId, receiverId));
    }
    public Messages updateMessage(String messageId, String newContent) {
        Optional<Messages> optional = messageRepo.findById(messageId);
        if (optional.isPresent()) {
            Messages message = optional.get();
            if (!message.isDeleted()) {
                message.setContent(newContent);
                return messageRepo.save(message);
            }
        }
        return null;
    }
    public boolean recallMessage(String messageId) {
        Optional<Messages> optional = messageRepo.findById(messageId);
        if (optional.isPresent()) {
            Messages message = optional.get();
            message.setDeleted(true);
            message.setContent("[Tin nhắn đã thu hồi]");
            messageRepo.save(message);
            return true;
        }
        return false;
    }
}
