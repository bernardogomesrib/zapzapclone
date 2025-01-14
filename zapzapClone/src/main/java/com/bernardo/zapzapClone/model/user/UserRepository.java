package com.bernardo.zapzapClone.model.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {
    @Query(name="User.findByEmail")
    Optional<User> findByEmail(String email);
}
