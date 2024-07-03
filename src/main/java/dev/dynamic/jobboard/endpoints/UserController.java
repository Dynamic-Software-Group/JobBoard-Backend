package dev.dynamic.jobboard.endpoints;

import dev.dynamic.jobboard.endpoints.requests.users.BookmarkPostRequest;
import dev.dynamic.jobboard.endpoints.requests.users.UpdateUserRequest;
import dev.dynamic.jobboard.model.Business;
import dev.dynamic.jobboard.model.JobPost;
import dev.dynamic.jobboard.model.enums.Tags;
import dev.dynamic.jobboard.model.User;
import dev.dynamic.jobboard.repositories.JobPostRepository;
import dev.dynamic.jobboard.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserRepository userRepository;
    private final JobPostRepository jobPostRepository;

    public UserController(UserRepository userRepository, JobPostRepository jobPostRepository) {
        this.userRepository = userRepository;
        this.jobPostRepository = jobPostRepository;
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

    @GetMapping(value = "/tags")
    public ResponseEntity<?> getTags() {
        String email = EndpointUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(user.getTags());
    }

    @GetMapping(value = "/get")
    public ResponseEntity<?> getUser() {
        String email = EndpointUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/bookmark")
    public ResponseEntity<?> bookmarkPost(@RequestBody BookmarkPostRequest request) {
        String email = EndpointUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        JobPost post = jobPostRepository.findById(request.getPostId()).orElse(null);

        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        user.getBookmarkedPosts().add(post);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/bookmarks")
    public ResponseEntity<?> getBookmarks() {
        String email = EndpointUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user.getBookmarkedPosts());
    }

    @PatchMapping(value = "/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest request) {
        String email = EndpointUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }

        if (request.getAvatar() != null) {
            user.setPassword(request.getAvatar());
        }

        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/businesses")
    public ResponseEntity<?> getBusinesses() {
        String email = EndpointUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        List<Business> businesses = user.getBusinesses();

        businesses.forEach(business -> {
            business.getEmployees().forEach(employee -> {
                employee.setBusinesses(null);
                employee.setBookmarkedPosts(null);
                employee.setPassword(null);
            });
        });

        return ResponseEntity.ok(businesses);
    }
}
