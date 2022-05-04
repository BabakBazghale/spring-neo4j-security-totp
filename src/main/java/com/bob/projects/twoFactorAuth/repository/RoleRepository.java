package com.bob.projects.twoFactorAuth.repository;

import com.bob.projects.twoFactorAuth.model.Role;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface RoleRepository extends Neo4jRepository<Role, Long> {
}
