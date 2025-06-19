package com.instrospect.jwt_validator.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JwtValidationServiceTest {

    private JwtValidationService jwtValidationService;
    private String secret = "3Vq#mP9$kL2@nR5*jF8&hX4^wC7!tY6zB1";
    private SecretKey key;

    @BeforeEach
    void setUp() {
        jwtValidationService = new JwtValidationService();
        ReflectionTestUtils.setField(jwtValidationService, "secret", secret);
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void testValidateToken_ValidJWT_ShouldReturnTrue() {
        // Create a valid JWT with required claims
        String jwt = Jwts.builder()
                .claim("Name", "JohnDoe")
                .claim("Role", "Admin")
                .claim("Seed", "7") // 7 is a prime number
                .signWith(key)
                .compact();

        boolean result = jwtValidationService.validateToken(jwt);
        assertTrue(result);
    }

    @Test
    void testValidateToken_InvalidName_ShouldReturnFalse() {
        // Create JWT with invalid name (contains numbers)
        String jwt = Jwts.builder()
                .claim("Name", "John123")
                .claim("Role", "Admin")
                .claim("Seed", "7")
                .signWith(key)
                .compact();

        boolean result = jwtValidationService.validateToken(jwt);
        assertFalse(result);
    }

    @Test
    void testValidateToken_InvalidRole_ShouldReturnFalse() {
        // Create JWT with invalid role
        String jwt = Jwts.builder()
                .claim("Name", "JohnDoe")
                .claim("Role", "InvalidRole")
                .claim("Seed", "7")
                .signWith(key)
                .compact();

        boolean result = jwtValidationService.validateToken(jwt);
        assertFalse(result);
    }

    @Test
    void testValidateToken_InvalidSeed_ShouldReturnFalse() {
        // Create JWT with non-prime seed
        String jwt = Jwts.builder()
                .claim("Name", "JohnDoe")
                .claim("Role", "Admin")
                .claim("Seed", "8") // 8 is not a prime number
                .signWith(key)
                .compact();

        boolean result = jwtValidationService.validateToken(jwt);
        assertFalse(result);
    }

    @Test
    void testValidateToken_MissingClaims_ShouldReturnFalse() {
        // Create JWT with missing claims
        String jwt = Jwts.builder()
                .claim("Name", "JohnDoe")
                .claim("Role", "Admin")
                // Missing Seed claim
                .signWith(key)
                .compact();

        boolean result = jwtValidationService.validateToken(jwt);
        assertFalse(result);
    }

    @Test
    void testValidateToken_ExtraClaims_ShouldReturnFalse() {
        // Create JWT with extra claims
        String jwt = Jwts.builder()
                .claim("Name", "JohnDoe")
                .claim("Role", "Admin")
                .claim("Seed", "7")
                .claim("Extra", "ExtraValue")
                .signWith(key)
                .compact();

        boolean result = jwtValidationService.validateToken(jwt);
        assertFalse(result);
    }

    @Test
    void testValidateToken_InvalidSignature_ShouldReturnFalse() {
        // Create JWT with different secret (must be at least 32 characters for JJWT)
        SecretKey wrongKey = Keys.hmacShaKeyFor("wrongsecretwrongsecretwrongsecret".getBytes(StandardCharsets.UTF_8));
        String jwt = Jwts.builder()
                .claim("Name", "JohnDoe")
                .claim("Role", "Admin")
                .claim("Seed", "7")
                .signWith(wrongKey)
                .compact();

        boolean result = jwtValidationService.validateToken(jwt);
        assertFalse(result);
    }
}
