package com.example.authspring.service;

import com.example.authspring.dto.request.RegistrationRequest;
import com.example.authspring.dto.response.RegistrationResponse;

public interface RegistrationService {
    RegistrationResponse register(RegistrationRequest request);
}
