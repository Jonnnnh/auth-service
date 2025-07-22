package com.example.authspring.event;

import com.example.authspring.model.User;

public record UserVerifiedEvent(User user) {
}
