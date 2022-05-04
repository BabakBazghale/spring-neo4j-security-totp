package com.bob.projects.twoFactorAuth.repository;

import com.bob.projects.twoFactorAuth.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;


public interface UserRepository extends Neo4jRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
