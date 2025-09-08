package com.example.library.user;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "roles")
public class Role {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String name;   // "ROLE_ADMIN", "ROLE_MEMBER"

  public Role() {}
  public Role(Long id, String name) { this.id = id; this.name = name; }

  public Long getId() { return id; }
  public String getName() { return name; }        // <-- explicit getter (fixes ro.getName())
  public void setName(String name) { this.name = name; }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Role r)) return false;
    return Objects.equals(name, r.name);
  }
  @Override public int hashCode() { return Objects.hash(name); }
}
