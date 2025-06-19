package com.Harum.Harum.Models;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.time.Instant;
import java.util.Date;

@Document (collection = "messages")
@Data
public class Messages {
    @Id
    private String Id;
    private String conversationId;
    private String sendAt;
    private String receivedAt;
    private String senderId;
    private String receiverId;
    private String content;
    private boolean isDeleted=false;
    public Messages() {}
    public Messages(String conversationId, Date receivedAt) {
        this.conversationId = conversationId;
        this.sendAt = Instant.now().toString();
        this.receivedAt = receivedAt.toString();
    }
    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }
    public String getSendAt() { return sendAt; }
    public void setSendAt(String sendAt) { this.sendAt = sendAt; }
    public String getReceivedAt() { return receivedAt; }
    public void setReceivedAt(Date receivedAt) { this.receivedAt = receivedAt.toString(); }

    public void setSenderId(String senderId) {
        this.senderId=senderId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId=receiverId;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }

    public String getSenderId() {
        return senderId;
    }
    public String getReceiverId(){
        return receiverId;
    }
}
