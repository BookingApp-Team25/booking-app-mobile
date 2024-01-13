package com.example.bookingapp.security;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtTokenUtil {
    private static final String SECRET_KEY = "8dae84f846e4f4b158a8d26681707f4338495bc7ab68151d7f7679cc5e56202dd3da0d356da007a7c28cb0b780418f4f3246769972d6feaa8f610c7d1e7ecf6a";

    public static String getUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    public static String getRole(String token) {
        List<Map<String, String>> roles = (List<Map<String, String>>) getAllClaims(token).get("role");
        if (roles != null && !roles.isEmpty()) {
            return roles.get(0).get("authority");
        } else {
            return null;
        }
    }

    private static  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private static Claims getAllClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes("UTF-8")).build().parseClaimsJws(token).getBody();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
