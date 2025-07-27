package com.example.authservice.service;

import com.example.authservice.dto.response.UserDto;
import com.example.authservice.model.User;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    List<User> getAllUsers();
    void deactivateUser(UUID userId);
    void activateUser(UUID userId);
    UserDto changeUserRole(UUID userId, String newRole);
}
