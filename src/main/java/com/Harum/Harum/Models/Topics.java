package com.Harum.Harum.Models;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
@Data
@Document(collection = "topics")
public class Topics {
    @Id
    private String id;
    private String name;
    private String description;

    public Topics() {}

    public Topics(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
