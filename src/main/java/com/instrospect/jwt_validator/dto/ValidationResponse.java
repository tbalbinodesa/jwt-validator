package com.instrospect.jwt_validator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Classe de resposta que representa o resultado da validação de um JWT.
 */
@Data
@AllArgsConstructor
@Schema(description = "Resposta da validação do token JWT")
public class ValidationResponse {

    /**
     * Indica se o JWT é válido (true) ou inválido (false).
     */
    @Schema(
            description = "Indica se o token JWT é válido ou não",
            example = "true"
    )
    private boolean isValid;
}
