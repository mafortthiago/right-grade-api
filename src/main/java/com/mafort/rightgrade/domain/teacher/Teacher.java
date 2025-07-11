package com.mafort.rightgrade.domain.teacher;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "Teacher")
@Table(name = "teachers")
public class Teacher implements UserDetails {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Setter
    private String name;
    private String email;
    @Setter
    private String password;
    @Setter
    private Boolean isActive;
    private boolean acceptPolicy;
    @Setter
    private LocalDateTime createdAt;

    public Teacher(RegisterDTO registerDTO) {
        this.email = registerDTO.email();
        this.name = registerDTO.name();
        this.password = registerDTO.password();
        this.acceptPolicy = registerDTO.acceptPolicy();
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
      return this.email;
    }

}
