package com.basic.myspringboot.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                      

    @Column(nullable = false, unique = true)
    private String email;                        // 로그인 Key

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    /* ---------- UserDetails 구현 ---------- */
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return null; }
    @Override public String getPassword()   { return password; }
    @Override public String getUsername()   { return email; }   // email = username
    @Override public boolean isAccountNonExpired()  { return true; }
    @Override public boolean isAccountNonLocked()   { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()            { return true; }
}