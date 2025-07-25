package com.example.authspring.repository;

import com.example.authspring.model.User;
import com.example.authspring.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser(User user);

    Optional<UserProfile> findByUserEmail(String email);

    void deleteByUser(User user);
}
