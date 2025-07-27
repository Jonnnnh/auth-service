package com.example.authservice.service.impl;

import com.example.authservice.dto.response.UserDto;
import com.example.authservice.enums.Role;
import com.example.authservice.exception.BadRequestException;
import com.example.authservice.exception.ResourceNotFoundException;
import com.example.authservice.mapper.UserMapper;
import com.example.authservice.model.User;
import com.example.authservice.provider.CurrentUserProvider;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deactivateUser(UUID userId) {
        User current = currentUserProvider.getUser();
        if (current.getId().equals(userId)) {
            throw new BadRequestException("Нельзя деактивировать свою учётную запись");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + userId));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void activateUser(UUID userId) {
        User current = currentUserProvider.getUser();
        if (current.getId().equals(userId)) {
            throw new BadRequestException("Нельзя менять статус своей учётной записи");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + userId));
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public UserDto changeUserRole(UUID userId, String newRole) {
        User current = currentUserProvider.getUser();
        if (current.getId().equals(userId)) {
            throw new BadRequestException("Нельзя менять свою собственную роль");
        }
        Role role;
        try {
            role = Role.valueOf(newRole.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException(newRole);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь c id" +  userId));
        user.getRoles().clear();
        user.getRoles().add(role);

        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

}
