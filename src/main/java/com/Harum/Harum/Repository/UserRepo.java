package com.Harum.Harum.Repository;

import com.Harum.Harum.Models.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<Users, String> {

}
