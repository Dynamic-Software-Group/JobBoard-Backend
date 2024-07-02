package dev.dynamic.jobboard.endpoints;

import dev.dynamic.jobboard.model.Tags;
import dev.dynamic.jobboard.model.User;
import dev.dynamic.jobboard.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(EndpointUtils.getCurrentUserEmail());
    }

    @PatchMapping(value = "/update-tags")
    public ResponseEntity<?> updateTags(@RequestBody List<String> tags) {
        String email = EndpointUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.getTags()
                .addAll(tags.stream().map(tag -> Tags.valueOf(tag.toUpperCase())).toList());

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/get")
    public ResponseEntity<?> getUser() {
        String email = EndpointUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }
}
