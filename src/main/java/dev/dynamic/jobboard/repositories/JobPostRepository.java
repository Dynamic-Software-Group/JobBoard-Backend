package dev.dynamic.jobboard.repositories;

import dev.dynamic.jobboard.model.JobPost;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    @Cacheable(value = "jobPosts", key = "#id")
    Optional<JobPost> findById(Long id);

    @CachePut(value = "jobPosts", key = "#jobPost.id")
    JobPost save(JobPost jobPost);

    @Cacheable(value = "jobPosts")
    List<JobPost> findAll();

    @Cacheable(value = "jobPosts")
    List<JobPost> findAllByActiveFalse();

    @Cacheable(value = "jobPosts")
    List<JobPost> findAllByActiveTrue();
}