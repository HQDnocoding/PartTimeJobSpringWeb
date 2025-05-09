/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.utils;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Date;

/**
 *
 * @author huaquangdat
 */
public class JwtUtils {

    private static final Dotenv dotenv = Dotenv.configure().load();
    private static final String SECRET = dotenv.get("SECRET_JWT");
    private static final long EXPIRATION_MS;

    static {
        String expirationMsStr = dotenv.get("EXPIRATION_MS");
        if (SECRET == null || expirationMsStr == null) {
            throw new IllegalStateException("Environment variables SECRET_JWT and EXPIRATION_MS must be set in .env file.");
        }
        try {
            EXPIRATION_MS = Long.parseLong(expirationMsStr);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("EXPIRATION_MS must be a valid long value.");
        }
    }
    public static String generateToken(String username) throws Exception {
        System.out.println(SECRET);
        JWSSigner signer = new MACSigner(SECRET);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .issueTime(new Date())
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimsSet
        );

        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    public static String validateTokenAndGetUsername(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SECRET);

        if (signedJWT.verify(verifier)) {
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expiration.after(new Date())) {
                return signedJWT.getJWTClaimsSet().getSubject();
            }
        }
        return null;
    }
}
