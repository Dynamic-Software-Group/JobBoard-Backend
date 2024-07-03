package dev.dynamic.jobboard.endpoints;

import dev.dynamic.jobboard.endpoints.requests.posts.CreatePostRequest;
import dev.dynamic.jobboard.model.Business;
import dev.dynamic.jobboard.model.JobPost;
import dev.dynamic.jobboard.model.User;
import dev.dynamic.jobboard.repositories.BusinessRepository;
import dev.dynamic.jobboard.repositories.JobPostRepository;
import dev.dynamic.jobboard.repositories.UserRepository;
import lombok.Data;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final JobPostRepository jobPostRepository;

    public PostController(UserRepository userRepository, BusinessRepository businessRepository, JobPostRepository jobPostRepository) {
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
        this.jobPostRepository = jobPostRepository;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createPost(@RequestBody CreatePostRequest request) {
        String email = EndpointUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Business business = businessRepository.findByOwner(user);

        if (business == null) {
            return ResponseEntity.notFound().build();
        }

        JobPost post = getJobPost(request, business);

        if (business.getPosts() == null) {
            business.setPosts(List.of(post));
        } else {
            business.getPosts().add(post);
        }

        businessRepository.save(business);
        jobPostRepository.save(post);

        post.setBusiness(null);

        return ResponseEntity.ok(post);
    }

    private JobPost getJobPost(CreatePostRequest request, Business business) {
        JobPost post = new JobPost();

        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setEmploymentType(request.getEmploymentType());
        post.setLocationType(request.getLocationType());
        post.setLocation(request.getLocation());
        post.setMaxSalary(request.getMaxSalary());
        post.setMinSalary(request.getMinSalary());
        post.setTags(request.getTags());
        post.setCurrency(request.getCurrency());
        post.setBusiness(business);
        return post;
    }

    @GetMapping(value = "/get")
    public ResponseEntity<?> getPosts() {
        String email = EndpointUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Business business = businessRepository.findByOwner(user);

        if (business == null) {
            return ResponseEntity.notFound().build();
        }

        List<JobPost> posts = business.getPosts();

        if (posts == null || posts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        posts.forEach(post -> post.setBusiness(null));

        return ResponseEntity.ok(posts);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        Optional<JobPost> post = jobPostRepository.findById(id);

        if (post.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        post.get().setBusiness(null);

        return ResponseEntity.ok(post);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        Optional<JobPost> post = jobPostRepository.findById(id);

        if (post.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Business business = post.get().getBusiness();

        business.getPosts().remove(post.get());

        businessRepository.save(business);
        jobPostRepository.delete(post.get());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-all-active")
    public ResponseEntity<?> getAllPosts() {
        List<JobPost> posts = jobPostRepository.findAll();

        posts = posts.stream().filter(JobPost::isActive).toList();

        posts.forEach(post -> {
            post.getBusiness().getOwner().setBusiness(null);
        });

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/get-all-archive")
    public ResponseEntity<?> getAllArchivePosts() {
        List<JobPost> posts = jobPostRepository.findAll();

        posts = posts.stream().filter(post -> !post.isActive()).toList();

        posts.forEach(post -> {
            post.getBusiness().getOwner().setBusiness(null);
        });

        return ResponseEntity.ok(posts);
    }
}
