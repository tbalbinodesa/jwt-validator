package com.instrospect.jwt_validator.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JwtControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        String secret = "3Vq#mP9$kL2@nR5*jF8&hX4^wC7!tY6zB1";
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private String createJsonPayload(String jwt) {
        return String.format("{\"jwt\": \"%s\"}", jwt);
    }

    @Test
    void caso1_shouldReturnTrueForValidJwt() throws Exception {
        // Create a valid JWT with all required claims
        String jwt = Jwts.builder()
                .claim("Role", "Admin")
                .claim("Seed", "7841") // Prime number
                .claim("Name", "Toninho Araujo")
                .signWith(secretKey)
                .compact();

        mockMvc.perform(post("/api/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJsonPayload(jwt)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"valid\": true}"));
    }

    @Test
    void caso2_shouldReturnFalseForInvalidJwtStructure() throws Exception {
        String invalidJwt = "eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOaJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        mockMvc.perform(post("/api/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJsonPayload(invalidJwt)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"valid\": false}"));
    }

    @Test
    void caso3_shouldReturnFalseForNameWithNumber() throws Exception {
        // Create JWT with name containing numbers
        String jwt = Jwts.builder()
                .claim("Role", "External")
                .claim("Seed", "72341") // Prime number
                .claim("Name", "M4ria Olivia") // Contains number
                .signWith(secretKey)
                .compact();

        mockMvc.perform(post("/api/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJsonPayload(jwt)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"valid\": false}"));
    }

    @Test
    void caso4_shouldReturnFalseForJwtWithMoreThanThreeClaims() throws Exception {
        // Create JWT with 4 claims (more than allowed 3)
        String jwt = Jwts.builder()
                .claim("Role", "Member")
                .claim("Org", "BR") // Extra claim
                .claim("Seed", "14627") // Prime number
                .claim("Name", "Valdir Aranha")
                .signWith(secretKey)
                .compact();

        mockMvc.perform(post("/api/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJsonPayload(jwt)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"valid\": false}"));
    }

    @Test
    void shouldReturnFalseForJwtWithInvalidRole() throws Exception {
        // Create JWT with invalid role
        String jwt = Jwts.builder()
                .claim("Role", "User") // Invalid role
                .claim("Seed", "7") // Prime number
                .claim("Name", "Test")
                .signWith(secretKey)
                .compact();

        mockMvc.perform(post("/api/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJsonPayload(jwt)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"valid\": false}"));
    }

    @Test
    void shouldReturnFalseForJwtWithNonPrimeSeed() throws Exception {
        // Create JWT with non-prime seed
        String jwt = Jwts.builder()
                .claim("Role", "Admin")
                .claim("Seed", "4") // Non-prime number
                .claim("Name", "Test")
                .signWith(secretKey)
                .compact();

        mockMvc.perform(post("/api/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJsonPayload(jwt)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"valid\": false}"));
    }

    @Test
    void shouldReturnFalseForJwtWithMissingClaims() throws Exception {
        // Create JWT with missing claims (only 2 claims instead of 3)
        String jwt = Jwts.builder()
                .claim("Role", "Admin")
                .claim("Name", "Test")
                // Missing Seed claim
                .signWith(secretKey)
                .compact();

        mockMvc.perform(post("/api/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJsonPayload(jwt)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"valid\": false}"));
    }

    @Test
    void shouldReturnFalseForJwtWithInvalidSignature() throws Exception {
        // Create JWT with different secret
        SecretKey wrongKey = Keys.hmacShaKeyFor("wrongsecretwrongsecretwrongsecret".getBytes(StandardCharsets.UTF_8));
        String jwt = Jwts.builder()
                .claim("Role", "Admin")
                .claim("Seed", "7")
                .claim("Name", "Test")
                .signWith(wrongKey)
                .compact();

        mockMvc.perform(post("/api/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJsonPayload(jwt)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"valid\": false}"));
    }

    @Test
    void shouldReturnFalseForEmptyJwt() throws Exception {
        mockMvc.perform(post("/api/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJsonPayload("")))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"valid\": false}"));
    }

    @Test
    void shouldReturnFalseForNameTooLong() throws Exception {
        // Create a name longer than 256 characters
        String longName = "A".repeat(257);
        String jwt = Jwts.builder()
                .claim("Role", "Admin")
                .claim("Seed", "7")
                .claim("Name", longName)
                .signWith(secretKey)
                .compact();

        mockMvc.perform(post("/api/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJsonPayload(jwt)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"valid\": false}"));
    }

    @Test
    void shouldReturnTrueForValidExternalRole() throws Exception {
        String jwt = Jwts.builder()
                .claim("Role", "External")
                .claim("Seed", "11") // Prime number
                .claim("Name", "External User")
                .signWith(secretKey)
                .compact();

        mockMvc.perform(post("/api/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJsonPayload(jwt)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"valid\": true}"));
    }

    @Test
    void shouldReturnTrueForValidMemberRole() throws Exception {
        String jwt = Jwts.builder()
                .claim("Role", "Member")
                .claim("Seed", "13") // Prime number
                .claim("Name", "Member User")
                .signWith(secretKey)
                .compact();

        mockMvc.perform(post("/api/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJsonPayload(jwt)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"valid\": true}"));
    }
}
