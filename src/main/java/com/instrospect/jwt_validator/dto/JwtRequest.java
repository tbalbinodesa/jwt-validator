package com.instrospect.jwt_validator.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Classe que representa a requisição para validação de um JWT.
 */
@Data
public class JwtRequest {

    /**
     * Token JWT que será validado.
     * Não pode estar em branco.
     */
    @NotBlank(message = "O JWT não pode estar em branco")
    private String jwt;
}
