package com.pasteleria.repository;

import com.pasteleria.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCorreo(String correo);

    Optional<User> findByRun(String run);

    boolean existsByCorreo(String correo);

    boolean existsByRun(String run);
}
