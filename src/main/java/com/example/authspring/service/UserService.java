package com.example.authspring.service;


import com.example.authspring.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User getCurrentUser();
}
