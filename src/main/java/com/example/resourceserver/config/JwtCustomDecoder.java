package com.example.resourceserver.config;

import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.EncryptedJWT;
import org.springframework.security.oauth2.jwt.*;

import java.text.ParseException;
import java.time.Instant;
import java.util.Map;

public class JwtCustomDecoder implements JwtDecoder {

    private final RSAKey rsaKey;

    public JwtCustomDecoder(RSAKey rsaKey) {
        this.rsaKey = rsaKey;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            EncryptedJWT encryptedJWT = EncryptedJWT.parse(token);
            encryptedJWT.decrypt(new RSADecrypter(rsaKey.toRSAPrivateKey()));

            Map<String, Object> claims = encryptedJWT.getPayload().toJSONObject();
            Long exp = (Long) claims.get("exp");
            Instant expInstant = Instant.ofEpochSecond(exp);

            if (expInstant.isBefore(Instant.now())) {
                throw new JwtException("Token expirado");
            }

            return new Jwt(
                    token,
                    Instant.ofEpochSecond((Long) claims.get("iat")),
                    expInstant,
                    Map.of("alg", "RSA-OAEP-256", "enc", "A256GCM"),
                    claims
            );
        } catch (ParseException e) {
            throw new JwtException("Token JWE invalido", e);
        } catch (Exception e) {
            throw new JwtException("Error al desencriptar JWE", e);
        }
    }
}
