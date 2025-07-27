package com.example.authservice.service;

import com.example.authservice.model.User;

public interface SecurityService {
    String getCurrentUserEmail();

    User getCurrentUser();
}
