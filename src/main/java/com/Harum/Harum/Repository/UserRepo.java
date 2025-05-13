package com.Harum.Harum.Repository;

import com.Harum.Harum.Models.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<Users, String> {
    Optional<Users> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<Users> findByEmail(String email); // Thêm phương thức này
    boolean existsByEmail(String email); // Kiểm tra email đã tồn tại chưa
    Optional<Users> findById(String id);
    List<Users> findAllByIdIn(List<String> ids);
    @Query("{ $or: [ { 'username': { $regex: ?0, $options: 'i' } }, { 'email': { $regex: ?0, $options: 'i' } } ] }")
    Page<Users> searchUsers(String keyword, Pageable pageable);


}
