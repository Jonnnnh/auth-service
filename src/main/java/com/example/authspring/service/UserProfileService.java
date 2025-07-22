package com.example.authspring.service;

import com.example.authspring.event.UserVerifiedEvent;

public interface UserProfileService {
    void handleUserVerified(UserVerifiedEvent event);
}
