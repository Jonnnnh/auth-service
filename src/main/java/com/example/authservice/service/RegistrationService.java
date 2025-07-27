package com.example.authservice.service;

import com.example.authservice.dto.request.RegistrationRequest;
import com.example.authservice.dto.response.RegistrationResponse;

public interface RegistrationService {
    RegistrationResponse register(RegistrationRequest request);
}
