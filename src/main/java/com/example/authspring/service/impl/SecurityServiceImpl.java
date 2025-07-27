package com.example.authspring.service.impl;

import com.example.authspring.model.User;
import com.example.authspring.provider.CurrentUserProvider;
import com.example.authspring.service.SecurityService;
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
