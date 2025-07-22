package com.example.authspring.service;

import com.example.authspring.model.User;

public interface SecurityService {
    String getCurrentUserEmail();

    User getCurrentUser();

    Long getUserProfileId();
}
