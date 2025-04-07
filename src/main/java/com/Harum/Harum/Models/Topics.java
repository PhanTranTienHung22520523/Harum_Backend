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


    public Topics() {}

    public Topics(String name) {
        this.name = name;

    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }


}
