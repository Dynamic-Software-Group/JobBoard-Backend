package dev.dynamic.jobboard.repositories;

import dev.dynamic.jobboard.model.Business;
import dev.dynamic.jobboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    Business findByOwner(User owner);
    Optional<Business> findById(Long id);
}
