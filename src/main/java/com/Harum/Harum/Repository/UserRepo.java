package com.Harum.Harum.Repository;

import com.Harum.Harum.Models.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<Users, String> {

}
