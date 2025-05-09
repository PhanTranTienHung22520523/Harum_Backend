package com.Harum.Harum.Models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class PostBlock {
    private String type;  // "text" hoặc "image"
    private String value; // Nội dung văn bản hoặc URL hình ảnh

    public PostBlock() {}
    public PostBlock(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public String getValue(){
        return value;
    }
}
