package com.instrospect.jwt_validator.controller;

import com.instrospect.jwt_validator.dto.JwtRequest;
import com.instrospect.jwt_validator.dto.ValidationResponse;
import com.instrospect.jwt_validator.service.JwtValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "JWT Validation", description = "Operações para validação e extração de informações de tokens JWT")
public class JwtController {

    @Autowired
    private JwtValidationService jwtValidationService;

    /**
     * Endpoint para validar um token JWT.
     *
     * @param jwtRequest o objeto contendo o token JWT a ser validado
     * @return ResponseEntity com o resultado da validação
     */
    @Operation(
            summary = "Validar token JWT",
            description = "Valida se um token JWT é válido de acordo com as regras de negócio configuradas"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token validado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ValidationResponse.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de resposta",
                                    value = "{\"isValid\": true}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida - JWT não fornecido ou em branco",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/validate")
    public ResponseEntity<ValidationResponse> validateToken(
            @Parameter(
                    description = "Objeto contendo o token JWT a ser validado",
                    required = true,
                    schema = @Schema(implementation = JwtRequest.class)
            )
            @RequestBody JwtRequest jwtRequest) {
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
    @Operation(
            summary = "Extrair claims do token JWT",
            description = "Extrai e retorna todas as claims (informações) contidas em um token JWT"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Claims extraídas com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemplo de claims",
                                    value = "{\"sub\":\"user123\",\"iat\":1640995200,\"exp\":1641081600,\"iss\":\"jwt-validator\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida - JWT não fornecido, em branco ou malformado",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/extract-claims")
    public ResponseEntity<String> extractClaims(
            @Parameter(
                    description = "Objeto contendo o token JWT do qual extrair as claims",
                    required = true,
                    schema = @Schema(implementation = JwtRequest.class)
            )
            @RequestBody JwtRequest jwtRequest) {
        String claims = jwtValidationService.extractClaims(jwtRequest.getJwt());
        return ResponseEntity.ok(claims);
    }
}
