package com.lab6Project.userManagement.repository;

import com.lab6Project.userManagement.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    // Optimized by Jpa to support a boolean value type.
    //boolean userExistsByEmail(String email);

    // Custom version of the previous Query Derivation Mechanism
    //@Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM user u WHERE u.email = ?1")
    //@Query("SELECT COUNT(*) > 0 FROM user WHERE user.email = ?1")
    boolean existsByEmail(String email);
}
