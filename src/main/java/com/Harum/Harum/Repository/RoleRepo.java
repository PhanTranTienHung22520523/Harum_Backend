package com.Harum.Harum.Repository;

import com.Harum.Harum.Enums.RoleTypes;
import com.Harum.Harum.Models.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepo extends MongoRepository<Roles, String> {
    Optional<Roles> findByRoleName(RoleTypes roleName);
}