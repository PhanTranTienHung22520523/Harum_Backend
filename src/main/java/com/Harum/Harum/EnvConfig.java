package com.Harum.Harum;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {
    private static final Dotenv dotenv = Dotenv.configure()
            .directory("src/main/resources") // Chỉ định thư mục chứa .env
            .ignoreIfMissing() // Không báo lỗi nếu thiếu file
            .load();

    public static String get(String key) {
        return dotenv.get(key);
    }
}
