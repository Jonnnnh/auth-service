package com.example.authspring.repository;

import com.example.authspring.enums.ConfirmationAction;
import com.example.authspring.model.Confirmation;
import com.example.authspring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {
    Optional<Confirmation> findByConfirmationCodeAndAction(String code, ConfirmationAction action);

    List<Confirmation> findByUserAndActionAndUsedFalse(User user, ConfirmationAction action);
}
