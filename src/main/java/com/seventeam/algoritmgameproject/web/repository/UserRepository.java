package com.seventeam.algoritmgameproject.web.repository;

import com.seventeam.algoritmgameproject.domain.model.login.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByUserId(String username);

    Optional<User> findByUserId(String username);
}
