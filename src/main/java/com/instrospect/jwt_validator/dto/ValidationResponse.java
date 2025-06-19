package com.instrospect.jwt_validator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Classe de resposta que representa o resultado da validação de um JWT.
 */
@Data
@AllArgsConstructor
public class ValidationResponse {

    /**
     * Indica se o JWT é válido (true) ou inválido (false).
     */
    private boolean isValid;
}
