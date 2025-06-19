package com.Harum.Harum.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;

// Lớp này đại diện cho một đối tượng trong danh sách gợi ý
public class PostRecommendation {

    // Annotation này ánh xạ từ trường JSON "_id" vào biến "id" của Java
    @JsonProperty("_id")
    private String id;

    @JsonProperty("title")
    private String title;

    // Bắt buộc phải có constructor không tham số cho việc deserialize
    public PostRecommendation() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}