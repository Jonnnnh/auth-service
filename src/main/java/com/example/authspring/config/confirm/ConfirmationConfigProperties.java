package com.example.authspring.config.confirm;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ConfirmationProperties.class)
@RequiredArgsConstructor
public class ConfirmationConfigProperties {
}
