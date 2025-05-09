package com.Harum.Harum.Controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Harum.Harum.Models.Messages;
import com.Harum.Harum.Models.Conversations;
import com.Harum.Harum.Services.MessageService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<Messages> sendMessage(
            @RequestParam String senderId,
            @RequestParam String receiverId,
            @RequestParam String message
    ) {
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
            @RequestParam String newContent
    ) {
        Messages updated = messageService.updateMessage(id, newContent);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/recall/{id}")
    public ResponseEntity<String> recallMessage(@PathVariable String id) {
        boolean success = messageService.recallMessage(id);
        return success ? ResponseEntity.ok("Tin nhắn đã được thu hồi.")
                : ResponseEntity.notFound().build();
    }
}
