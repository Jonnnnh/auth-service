package com.example.authspring.provider;

import com.example.authspring.exception.ResourceNotFoundException;
import com.example.authspring.model.User;
import com.example.authspring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final UserRepository userRepository;

    private String email;
    private User user;

    public String getEmail() {
        if (email == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
                throw new BadCredentialsException("Пользователь не аутентифицирован");
            }
            Object principal = auth.getPrincipal();
            email = principal instanceof UserDetails
                    ? ((UserDetails) principal).getUsername()
                    : principal.toString();
        }
        return email;
    }

    public User getUser() {
        if (user == null) {
            user = userRepository.findByEmail(getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", getEmail()));
        }
        return user;
    }

}
