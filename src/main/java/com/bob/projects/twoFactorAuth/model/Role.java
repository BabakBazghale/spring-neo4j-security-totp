package com.bob.projects.twoFactorAuth.model;

import lombok.*;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class Role {
    @Id
    @GeneratedValue
    private Long id;
    @Index(unique = true)
    private String roleName;

    public Role(String roleName) {
        this.roleName = roleName;
    }
}

