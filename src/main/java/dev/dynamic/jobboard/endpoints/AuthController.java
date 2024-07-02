package dev.dynamic.jobboard.endpoints;

import dev.dynamic.jobboard.endpoints.requests.LoginRequest;
import dev.dynamic.jobboard.endpoints.requests.RegisterRequest;
import dev.dynamic.jobboard.model.User;
import dev.dynamic.jobboard.repositories.UserRepository;
import dev.dynamic.jobboard.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final Argon2PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, Argon2PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

            String userPassword = user.getPassword();
            String requestPassword = request.getPassword();

            if (!passwordEncoder.matches(requestPassword, userPassword)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String jwt = jwtUtil.createToken(user);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, null);
            SecurityContextHolder.getContext().setAuthentication(auth);

            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());

            String encodedPassword = passwordEncoder.encode(request.getPassword());
            user.setPassword(encodedPassword);

            userRepository.save(user);

            String jwt = jwtUtil.createToken(user);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, null);
            SecurityContextHolder.getContext().setAuthentication(auth);

            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
