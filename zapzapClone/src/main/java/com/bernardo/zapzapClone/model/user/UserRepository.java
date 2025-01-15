package com.bernardo.zapzapClone.model.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {
    @Query(name="User.findAll")
    List<User> findAllButMe(String publicId);

    @Query(name="User.findByEmail")
    Optional<User> findByEmail(String email);
}
