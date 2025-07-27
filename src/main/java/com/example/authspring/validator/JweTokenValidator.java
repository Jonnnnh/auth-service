package com.example.authspring.validator;

import com.example.authspring.config.jwt.JwtConfigProperties;
import com.example.authspring.exception.TokenValidationException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JweTokenValidator implements TokenValidator{

    private final JwtConfigProperties props;

    private SecretKey getKey() {
        byte[] keyBytes = Base64URL.from(props.secret()).decode();
        return new SecretKeySpec(keyBytes, "AES");
    }

    public String extractSubject(String token) {
        try {
            JWEObject jweObject = JWEObject.parse(token);
            jweObject.decrypt(new DirectDecrypter(getKey()));
            JWTClaimsSet claims = JWTClaimsSet.parse(jweObject.getPayload().toJSONObject());
            var exp = claims.getExpirationTime();
            if (exp == null || exp.before(new Date())) {
                throw new TokenValidationException("Токен истёк");
            }
            return claims.getSubject();
        } catch (JOSEException | java.text.ParseException e) {
            throw new TokenValidationException("Неверный или просроченный токен");
        }
    }
}