package com.example.library.user;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  // ---- explicit getters/setters (so Eclipse sees them) ----
  public Long getId()              { return id; }
  public String getEmail()         { return email; }       // <-- used in SecurityConfig
  public String getPassword()      { return password; }    // <-- used in SecurityConfig
  public Set<Role> getRoles()      { return roles; }       // <-- used in JwtAuthFilter
  public void setEmail(String e)   { this.email = e; }
  public void setPassword(String p){ this.password = p; }
  public void setRoles(Set<Role> r){ this.roles = r; }
}
