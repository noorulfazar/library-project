// auth/AuthController.java
package com.example.library.auth;

import com.example.library.auth.AuthDtos.AuthRequest;
import com.example.library.auth.AuthDtos.AuthResponse;
import com.example.library.security.JwtService;
import com.example.library.user.Role;
import com.example.library.user.RoleRepository;
import com.example.library.user.User;
import com.example.library.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository users;
    private final RoleRepository roles;
    private final BCryptPasswordEncoder enc;
    private final JwtService jwt;

    public AuthController(UserRepository u, RoleRepository r, BCryptPasswordEncoder e, JwtService j) {
        this.users = u;
        this.roles = r;
        this.enc = e;
        this.jwt = j;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest req) {
        // ensure role exists (ROLE_MEMBER)
        Role role = roles.findByName("ROLE_MEMBER")
                .orElseGet(() -> roles.save(new Role(null, "ROLE_MEMBER")));

        // build User without Lombok builder
        User newUser = new User();
        newUser.setEmail(req.email());
        newUser.setPassword(enc.encode(req.password()));
        newUser.setRoles(Set.of(role));

        users.save(newUser);

        String token = jwt.generate(newUser.getEmail(), Map.of("roles", role.getName()));
        return new AuthResponse(token, role.getName());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        User u = users.findByEmail(req.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!enc.matches(req.password(), u.getPassword())) {
            throw new RuntimeException("Bad credentials");
        }

        String role = u.getRoles().stream()
                .findFirst().map(Role::getName)
                .orElse("ROLE_MEMBER");

        String token = jwt.generate(u.getEmail(), Map.of("roles", role));
        return new AuthResponse(token, role);
    }
}
