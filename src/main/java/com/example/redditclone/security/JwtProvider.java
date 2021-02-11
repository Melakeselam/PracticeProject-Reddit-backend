package com.example.redditclone.security;

import com.example.redditclone.exception.SpringRedditException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;


@Service
public class JwtProvider {

    private KeyStore keyStore;

    @Value("${jwt-keystore-pwd}")
    private String jwtKeystorePassword;
    @Value("${jwt-keystore-alias}")
    private String jwtKeystoreAlias;
    @Value("${jwt-keystore-file}")
    private String jwtKeystoreFile;

    @PostConstruct
    public void init(){
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream(jwtKeystoreFile);
            keyStore.load(resourceAsStream, jwtKeystorePassword.toCharArray());//

        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            throw new SpringRedditException("Exception occurred while loading keystore");
        }
    }

    public String generateToken(Authentication authentication){
        org.springframework.security.core.userdetails.User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey(jwtKeystoreAlias, jwtKeystorePassword.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new SpringRedditException("Exception occurred while receiving public key from keystore");
        }
    }

    public boolean validateToken(String jwtFromRequest) {

        Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwtFromRequest);
        return true;
    }

    private PublicKey getPublicKey() {

        try {
            return keyStore.getCertificate(jwtKeystoreAlias).getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringRedditException("Exception occurred while retrieving the public key from keystore");
        }
    }

    public String getUsernameFromJwt(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(jwt)
                .getBody();
        return claims.getSubject();
    }
}
