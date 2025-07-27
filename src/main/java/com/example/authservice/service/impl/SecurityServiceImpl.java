package com.example.authservice.service.impl;

import com.example.authservice.model.User;
import com.example.authservice.provider.CurrentUserProvider;
import com.example.authservice.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final CurrentUserProvider userProv;

    @Override
    public String getCurrentUserEmail() {
        return userProv.getEmail();
    }

    @Override
    public User getCurrentUser() {
        return userProv.getUser();
    }

}
