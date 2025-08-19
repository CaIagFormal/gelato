package com.tcc.gelato.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Serviço para lidar com JSON Web Tokens
 */
@Service
public class S_JWT {

    private final String chaveSecreta;

    public S_JWT() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        SecretKey chave = keyGen.generateKey();
        this.chaveSecreta = Base64.getEncoder().encodeToString(chave.getEncoded());
    }

    /**
     * Gera um token JWT contendo um nome de usuário
     * @return
     */
    public String gerarToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        Date agora = new Date();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(agora)
                .expiration(new Date(agora.getTime() + (1000 * 60 * 10)))
                .and()
                .signWith(getKey())
                .compact();
    }

    public Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(chaveSecreta);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
