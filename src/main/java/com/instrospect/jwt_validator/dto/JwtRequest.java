package com.instrospect.jwt_validator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Classe que representa a requisição para validação de um JWT.
 */
@Data
@Schema(description = "Objeto de requisição contendo o token JWT a ser processado")
public class JwtRequest {

    /**
     * Token JWT que será validado.
     * Não pode estar em branco.
     */
    @Schema(
            description = "Token JWT a ser validado ou processado",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMTIzIiwiaWF0IjoxNjQwOTk1MjAwLCJleHAiOjE2NDEwODE2MDB9.signature",
            required = true
    )
    @NotBlank(message = "O JWT não pode estar em branco")
    private String jwt;
}
