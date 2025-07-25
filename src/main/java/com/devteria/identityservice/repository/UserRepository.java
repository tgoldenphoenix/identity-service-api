package com.devteria.identityservice.repository;

import com.devteria.identityservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//repository == model in nodeJS
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // spring hibernate auto generate a sql query, we don't need to write it
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
