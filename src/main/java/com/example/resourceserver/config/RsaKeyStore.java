package com.example.resourceserver.config;

import com.nimbusds.jose.jwk.RSAKey;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

public class RsaKeyStore {

    private static final String KEY_FILE = "/tmp/rsa-key.json";
    private static final RSAKey RSA_KEY = loadOrGenerate();

    private static RSAKey loadOrGenerate() {
        try {
            File file = new File(KEY_FILE);
            if (file.exists()) {
                // Lee la llave existente
                String json = Files.readString(file.toPath());
                return RSAKey.parse(json);
            } else {
                // Genera y guarda
                KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
                gen.initialize(2048);
                KeyPair keyPair = gen.generateKeyPair();
                RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                        .privateKey((RSAPrivateKey) keyPair.getPrivate())
                        .keyID(UUID.randomUUID().toString())
                        .build();
                Files.writeString(file.toPath(), rsaKey.toJSONString());
                return rsaKey;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static RSAKey getRsaKey() {
        return RSA_KEY;
    }
}