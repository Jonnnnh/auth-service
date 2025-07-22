package com.example.authspring.service.impl;

import com.example.authspring.event.UserVerifiedEvent;
import com.example.authspring.exception.ResourceNotFoundException;
import com.example.authspring.model.User;
import com.example.authspring.model.UserProfile;
import com.example.authspring.repository.UserProfileRepository;
import com.example.authspring.repository.UserRepository;
import com.example.authspring.service.UserProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Override
    @EventListener
    @Transactional
    public void handleUserVerified(UserVerifiedEvent event) {
        User user = userRepository.findById(event.user().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь" + event.user().getId()));
        if (userProfileRepository.findByUser(user).isEmpty()) {
            UserProfile profile = UserProfile.builder()
                    .user(user)
                    .firstName(null)
                    .lastName(null)
                    .birthday(null)
                    .build();
            userProfileRepository.save(profile);
            log.info("Создан профиль для пользователя {}", user.getEmail());
        } else {
            log.debug("Профиль для пользователя {} уже существует, пропускаем", user.getEmail());
        }
    }
}
