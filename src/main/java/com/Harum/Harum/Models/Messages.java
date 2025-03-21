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
    private String id;
    private String conversationId;
    private String sendAt;
    private String receivedAt;

    public Messages() {}
    public Messages(String conversationId, Date receivedAt) {
        this.conversationId = conversationId;
        this.sendAt = Instant.now().toString();
        this.receivedAt = receivedAt.toString();
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }
    public String getSendAt() { return sendAt; }
    public void setSendAt(String sendAt) { this.sendAt = sendAt; }
    public String getReceivedAt() { return receivedAt; }
    public void setReceivedAt(Date receivedAt) { this.receivedAt = receivedAt.toString(); }

}
