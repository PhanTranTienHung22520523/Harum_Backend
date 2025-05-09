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
    private String user1Id;
    private String user2Id;

    public Conversations() {}

    public Conversations(String user1Id, String user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getuser1Id() { return user1Id; }
    public void setuser1Id(String user1Id) { this.user1Id = user1Id; }
    public String getReceiverId() { return user2Id; }
    public void setuser2Id(String user2Id) { this.user2Id = user2Id; }
}
