package com.bob.projects.twoFactorAuth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(max = 15)
    @Index(unique = true)
    private String username;

    @NotBlank
    @Size(max = 100)
    @JsonIgnore
    private String password;

    @NotBlank
    @Size(max = 40)
    @Email
    @Index(unique = true)
    private String email;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private boolean isActive;

    @Relationship(type = "have", direction = Relationship.INCOMING)
    private Set<Role> roles;

    private boolean isTwoFactorEnabled;
    private String secret;

    public User(User user) {
        this.username = user.username;
        this.password = user.password;
        this.email = user.email;
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.isActive = user.isActive;
    }
}
