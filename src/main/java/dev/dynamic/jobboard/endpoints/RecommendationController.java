package dev.dynamic.jobboard.endpoints;

import dev.dynamic.jobboard.model.JobPost;
import dev.dynamic.jobboard.model.User;
import dev.dynamic.jobboard.model.enums.Tags;
import dev.dynamic.jobboard.repositories.JobPostRepository;
import dev.dynamic.jobboard.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/recommendations")
public class RecommendationController {

    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;

    public RecommendationController(JobPostRepository jobPostRepository, UserRepository userRepository) {
        this.jobPostRepository = jobPostRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/get-related/{currentPostId}")
    public ResponseEntity<?> getRecommendations(@PathVariable Long currentPostId) {
        Optional<JobPost> optionalJobPost = jobPostRepository.findById(currentPostId);

        if (optionalJobPost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        JobPost currentPost = optionalJobPost.get();

        List<Tags> tags = currentPost.getTags();
        List<JobPost> allPosts = jobPostRepository.findAll();

        allPosts.remove(currentPost);

        if (tags.isEmpty()) {
            int endIndex = Math.min(allPosts.size(), 3);

            if (endIndex == 0) {
                return ResponseEntity.ok(List.of());
            }

            return ResponseEntity.ok(allPosts.subList(0, endIndex)); // return first 3 posts
        }

        List<JobPost> relatedPosts = allPosts.stream()
                .sorted(Comparator.comparingInt((JobPost post) -> getMatchCount(post.getTags(), tags)).reversed())
                .toList();

        return ResponseEntity.ok(relatedPosts);
    }

    private int getMatchCount(List<Tags> postTags, List<Tags> searchTags) {
        int count = 0;
        for (Tags tag : searchTags) {
            if (postTags.contains(tag)) {
                count++;
            }
        }
        return count;
    }

    @GetMapping("/get-user-recommendations")
    public ResponseEntity<?> getUserRecommendations() {
        String email = EndpointUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        List<Tags> tags = user.getTags();

        if (tags.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        List<JobPost> allPosts = jobPostRepository.findAll();

        List<JobPost> relatedPosts = allPosts.stream()
                .sorted(Comparator.comparingInt((JobPost post) -> getMatchCount(post.getTags(), tags)).reversed())
                .toList();

        return ResponseEntity.ok(relatedPosts);
    }
}
