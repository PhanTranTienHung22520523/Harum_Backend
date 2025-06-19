package com.Harum.Harum.Services;// package com.Harum.Harum.Services;

import com.Harum.Harum.DTO.PostRecommendation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class RecommendationService {

    private final RestTemplate restTemplate;
    private final String recommendationApiUrl;

    // Sửa ở đây: @Value("${recommendation.api.base-url}") sẽ đọc key từ file properties
    public RecommendationService(RestTemplate restTemplate, @Value("${recommendation.api.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        // Xây dựng URL hoàn chỉnh để gọi
        this.recommendationApiUrl = baseUrl + "/recommend/{userId}";
    }

    public List<PostRecommendation> getRecommendations(String userId) {
        try {
            // ParameterizedTypeReference dùng để RestTemplate biết cách chuyển đổi JSON thành một List<PostRecommendation>
            ResponseEntity<List<PostRecommendation>> response = restTemplate.exchange(
                    recommendationApiUrl,
                    HttpMethod.GET,
                    null, // Không có request body
                    new ParameterizedTypeReference<List<PostRecommendation>>() {},
                    userId // Giá trị này sẽ được đưa vào placeholder {userId}
            );
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            // Nếu API trả về 404 Not Found, ta coi như không có gợi ý và trả về danh sách rỗng
            System.err.println("Không tìm thấy gợi ý cho user: " + userId);
            return Collections.emptyList();
        } catch (Exception e) {
            // Xử lý các lỗi khác (ví dụ: service FastAPI bị sập)
            System.err.println("Lỗi khi gọi đến Recommendation Service: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}