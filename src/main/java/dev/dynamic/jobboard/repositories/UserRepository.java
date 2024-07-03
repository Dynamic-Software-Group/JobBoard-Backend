package dev.dynamic.jobboard.repositories;

import dev.dynamic.jobboard.model.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Cacheable(value = "user", key = "#email")
    Optional<User> findByEmail(String email);

    @Cacheable(value = "user", key = "#username")
    Optional<User> findByUsername(String username);

    @CacheEvict(value = "user", key = "#email")
    void deleteByEmail(String email);

    @CacheEvict(value = "user", key = "#username")
    void deleteByUsername(String username);

    @Override
    @CachePut(value = "user", key = "#user.email")
    User save(User user);
}
