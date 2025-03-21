package com.Harum.Harum.Models;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.time.Instant;

@Document (collection = "conversations")
@Data
public class Conversations {
    @Id
    private String id;
    private String senderId;
    private String receiverId;

    public Conversations() {}

    public Conversations(String senderId, String receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
}
