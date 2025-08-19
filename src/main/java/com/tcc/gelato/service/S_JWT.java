package com.tcc.gelato.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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

    public SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(chaveSecreta);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extrairUsername(String token) {
        // extract the username from jwt token
        return extrairDeClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extrairDeClaim(token, Claims::getExpiration);
    }

    /**
     * @param token Token a ser validado
     * @param username Use essa função {@link S_JWT#extrairUsername(String)}
     * @param userDetails Detalhes de usuário que serão comparados
     * @return
     */
    public boolean validarToken(String token, String username, UserDetails userDetails) {
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    /**
     * Extrai um elemento de uma claim
     * @param token Token
     * @param claimResolver Referência a função que extrai a propriedade desejada
     * @return
     */
    private <T> T extrairDeClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


}
