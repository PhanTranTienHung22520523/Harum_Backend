package com.Harum.Harum.Repository;

import com.Harum.Harum.Models.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<Users, String> {
    Optional<Users> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<Users> findByEmail(String email); // Thêm phương thức này
    boolean existsByEmail(String email); // Kiểm tra email đã tồn tại chưa

}
