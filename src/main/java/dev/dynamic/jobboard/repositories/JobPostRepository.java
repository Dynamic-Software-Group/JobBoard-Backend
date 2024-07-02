package dev.dynamic.jobboard.repositories;

import dev.dynamic.jobboard.model.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    Optional<JobPost> findById(Long id);
}
