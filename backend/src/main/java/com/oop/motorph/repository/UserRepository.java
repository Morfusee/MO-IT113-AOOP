package com.oop.motorph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oop.motorph.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Retrieves a user by matching username and password.
     * 
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The matching User entity, or null if not found.
     */
    User findByUsernameAndPassword(String username, String password);

    /**
     * Checks if a user with the given username exists.
     * 
     * @param username The username to check.
     * @return True if a user with the username exists, false otherwise.
     */
    Boolean existsByUsername(String username);
}
