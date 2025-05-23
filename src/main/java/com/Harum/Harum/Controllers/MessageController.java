package com.Harum.Harum.Controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import com.Harum.Harum.Models.Messages;
import com.Harum.Harum.Models.Conversations;
import com.Harum.Harum.Services.MessageService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/send")
    public ResponseEntity<Messages> sendMessage(@RequestBody Map<String, String> payload) {
        String senderId = payload.get("senderId");
        String receiverId = payload.get("receiverId");
        String message = payload.get("message");
        Messages saved = messageService.sendMessage(senderId, receiverId, message);
        return ResponseEntity.ok(saved);
    }


    @GetMapping("/conversation")
    public ResponseEntity<List<Messages>> getConversation(
            @RequestParam String user1,
            @RequestParam String user2
    ) {
        Optional<Conversations> conv = messageService.getConversation(user1, user2);
        return conv.map(c -> ResponseEntity.ok(messageService.getMessages(c.getId())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Messages> updateMessage(
            @PathVariable String id,
            @RequestBody Map<String, String> payload
    ) {
        String newContent = payload.get("newContent");
        Messages updated = messageService.updateMessage(id, newContent);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/recall/{id}")
    public ResponseEntity<String> recallMessage(@PathVariable String id) {
        boolean success = messageService.recallMessage(id);
        return success ? ResponseEntity.ok("Tin nhắn đã được thu hồi.")
                : ResponseEntity.notFound().build();
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Messages chatMessage) {
        // Lưu message vào DB
        Messages savedMessage = messageService.sendMessage(
                chatMessage.getSenderId(),
                chatMessage.getReceiverId(),
                chatMessage.getContent());

        // Lấy conversationId của message vừa lưu
        String conversationId = savedMessage.getConversationId();

        // Gửi message tới topic riêng của conversationId
        messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, savedMessage);
    }
}
