package com.joebrooks.showmethecoin.infra.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.UUID;

public class TokenGenerator {

    private static final String secretKey = System.getenv("upBit_secretKey");
    private static final String accessKey = System.getenv("upBit_accessKey");

    public static String getAuthToken(){
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);

        return "Bearer " + jwtToken;
    }

    public static String getAuthToken(String queryHash){
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
                .sign(algorithm);

        return "Bearer " + jwtToken;
    }

}
