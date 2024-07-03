package dev.dynamic.jobboard.repositories;

import dev.dynamic.jobboard.model.Experience;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    @Cacheable(value = "experience", key = "#id")
    Optional<Experience> findById(Long id);

    @CachePut(value = "experience", key = "#experience.id")
    Experience save(Experience experience);
}
