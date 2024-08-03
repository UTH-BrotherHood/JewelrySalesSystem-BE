package com.example.JewelrySalesSystem.util;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.JWTClaimsSet;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final String SECRET = "your-256-bit-secret"; // Replace with your own secret key
    private static final long EXPIRATION_TIME = 864_000_00; // 1 day in milliseconds

    public static String createToken(Map<String, Object> claims) throws Exception {
        JWSSigner signer = new MACSigner(SECRET);
        
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
        builder.expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
        
        claims.forEach(builder::claim);
        
        JWTClaimsSet claimsSet = builder.build();
        
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);
        
        return signedJWT.serialize();
    }

    public static boolean validateToken(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SECRET);
        
        return signedJWT.verify(verifier) && !isTokenExpired(signedJWT);
    }

    public static String getUsernameFromToken(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getStringClaim("username");
    }

    private static boolean isTokenExpired(SignedJWT signedJWT) throws Exception {
        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        return expirationDate.before(new Date());
    }
}