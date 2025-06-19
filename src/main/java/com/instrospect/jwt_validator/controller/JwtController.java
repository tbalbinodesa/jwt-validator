package com.instrospect.jwt_validator.controller;

import com.instrospect.jwt_validator.dto.JwtRequest;
import com.instrospect.jwt_validator.dto.ValidationResponse;
import com.instrospect.jwt_validator.service.JwtValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST responsável por gerenciar as operações relacionadas a JWT.
 */
@RestController
@RequestMapping("/api/jwt")
public class JwtController {

    @Autowired
    private JwtValidationService jwtValidationService;

    /**
     * Endpoint para validar um token JWT.
     *
     * @param jwtRequest o objeto contendo o token JWT a ser validado
     * @return ResponseEntity com o resultado da validação
     */
    @PostMapping("/validate")
    public ResponseEntity<ValidationResponse> validateToken(@RequestBody JwtRequest jwtRequest) {
        boolean isValid = jwtValidationService.validateToken(jwtRequest.getJwt());
        ValidationResponse response = new ValidationResponse(isValid);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para extrair claims de um token JWT.
     *
     * @param jwtRequest o objeto contendo o token JWT
     * @return ResponseEntity com as claims extraídas
     */
    @PostMapping("/extract-claims")
    public ResponseEntity<String> extractClaims(@RequestBody JwtRequest jwtRequest) {
        String claims = jwtValidationService.extractClaims(jwtRequest.getJwt());
        return ResponseEntity.ok(claims);
    }
}
