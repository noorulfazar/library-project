// src/main/java/com/example/library/LibraryApplication.java
package com.example.library;

import com.example.library.user.Role;
import com.example.library.user.RoleRepository;
import com.example.library.user.User;
import com.example.library.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class LibraryApplication {
  public static void main(String[] args) {
    SpringApplication.run(LibraryApplication.class, args);
  }

  @Bean
  CommandLineRunner seed(RoleRepository roles, UserRepository users, BCryptPasswordEncoder enc) {
    return args -> {
      // Ensure roles exist (create if missing)
      Role adminRole = roles.findByName("ROLE_ADMIN")
          .orElseGet(() -> roles.save(new Role(null, "ROLE_ADMIN")));
      Role memberRole = roles.findByName("ROLE_MEMBER")
          .orElseGet(() -> roles.save(new Role(null, "ROLE_MEMBER")));

      // Seed admin
      if (users.findByEmail("admin@lib.com").isEmpty()) {
        User admin = new User();
        admin.setEmail("admin@lib.com");
        admin.setPassword(enc.encode("admin123"));
        admin.setRoles(Set.of(adminRole));
        users.save(admin);
      }

      // Seed member
      if (users.findByEmail("member@lib.com").isEmpty()) {
        User mem = new User();
        mem.setEmail("member@lib.com");
        mem.setPassword(enc.encode("member123"));
        mem.setRoles(Set.of(memberRole));
        users.save(mem);
      }
    };
  }
}
