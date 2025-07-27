package com.example.authservice.provider;

import com.example.authservice.config.jwt.JwtConfigProperties;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jose.util.Base64URL;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JweTokenProvider {

    private final JwtConfigProperties props;

    private SecretKey getKey() {
        byte[] keyBytes = Base64URL.from(props.secret()).decode();
        if (keyBytes.length != 64) {
            throw new IllegalStateException("JWT secret must be 64 bytes when Base64URL-decoded for A256CBC-HS512");
        }
        return new SecretKeySpec(keyBytes, "AES");
    }


    public String generateEncryptedToken(String subject) throws JOSEException {
        JWEHeader header = new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A256CBC_HS512)
                .contentType("JWT")
                .build();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(subject)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + props.expiration().toMillis()))
                .build();

        JWEObject jwe = new JWEObject(header, new Payload(claims.toJSONObject()));
        jwe.encrypt(new DirectEncrypter(getKey()));
        return jwe.serialize();
    }
}