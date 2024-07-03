package dev.dynamic.jobboard.repositories;

import dev.dynamic.jobboard.model.Business;
import dev.dynamic.jobboard.model.User;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    @Cacheable(value = "business", key = "#owner.id")
    Business findByOwner(User owner);

    @Cacheable(value = "business", key = "#id")
    Optional<Business> findById(Long id);

    @CachePut(value = "business", key = "#business.id")
    Business save(Business business);
}
