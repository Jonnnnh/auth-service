package com.example.authspring.config.security;

import com.example.authspring.exception.TokenValidationException;
import com.example.authspring.service.UserService;
import com.example.authspring.until.SecurityConstants;
import com.example.authspring.validator.TokenValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final TokenValidator tokenValidator;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return Arrays.stream(SecurityConstants.PUBLIC_URLS).anyMatch(pattern -> new AntPathMatcher().match(pattern, path));
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {
        String jwe = getJwtFromRequest(request);
        if (jwe != null) {
            log.debug("Получен JWE-токен: {}", jwe);
            try {
                String username = tokenValidator.extractSubject(jwe);
                log.debug("Токен действителен, пользователь: {}", username);

                var userDetails = userService.loadUserByUsername(username);
                if (!userDetails.isEnabled()) {
                    log.warn("Учётная запись '{}' заблокирована", username);
                    response.sendError(HttpStatus.FORBIDDEN.value(), "Учётная запись заблокирована");
                    return;
                }

                var auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("Пользователь '{}' успешно аутентифицирован через JWE", username);

            } catch (TokenValidationException ex) {
                log.warn("Ошибка валидации JWE-токена: {}", ex.getMessage());
                response.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
                return;
            }
        } else {
            log.trace("Заголовок Authorization отсутствует или не содержит Bearer-токен");
        }

        chain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}