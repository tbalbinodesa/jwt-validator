package com.instrospect.jwt_validator.service;

import com.instrospect.jwt_validator.util.PrimeUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * Serviço responsável pela validação de tokens JWT.
 */
@Service
@Slf4j
public class JwtValidationService {

    // Assumindo que o segredo para validar a assinatura é fornecido via application.properties
    // Para os testes fornecidos, o segredo não é explicitamente mencionado.
    // Vamos usar um segredo padrão, mas torná-lo configurável.
    @Value("${jwt.secret}")
    private String secret;

    private static final Set<String> VALID_ROLES = Set.of("Admin", "Member", "External");
    private static final int MAX_NAME_LENGTH = 256;

    /**
     * Valida um token JWT.
     *
     * @param token o token JWT a ser validado
     * @return true se o token for válido, false caso contrário
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            // 1. Valida a estrutura e assinatura do JWT.
            // Se for inválido (estrutura, assinatura, expiração), lança uma JwtException.
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            log.info("JWT decodificado com sucesso. Claims: {}", claims);

            // 2. Valida as regras de negócio sobre as claims.
            return areClaimsValid(claims);

        } catch (JwtException e) {
            log.error("JWT inválido: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Erro inesperado durante a validação do JWT.", e);
            return false;
        }
    }

    /**
     * Extrai informações do token JWT.
     *
     * @param token o token JWT
     * @return as informações extraídas do token
     */
    public String extractClaims(String token) {
        // TODO: Implementar extração de claims do JWT
        return null;
    }

    private boolean areClaimsValid(Claims claims) {
        return hasExactlyThreeClaims(claims) &&
                isNameClaimValid(claims) &&
                isRoleClaimValid(claims) &&
                isSeedClaimValid(claims);
    }

    // Regra: Deve conter apenas 3 claims (Name, Role e Seed)
    private boolean hasExactlyThreeClaims(Claims claims) {
        boolean valid = claims.size() == 3 &&
                claims.containsKey("Name") &&
                claims.containsKey("Role") &&
                claims.containsKey("Seed");
        if (!valid) log.warn("Validação falhou: O JWT não contém exatamente as 3 claims esperadas.");
        return valid;
    }

    // Regra: A claim Name não pode ter carácter de números e tem tamanho máximo de 256.
    private boolean isNameClaimValid(Claims claims) {
        String name = claims.get("Name", String.class);
        if (name == null || name.length() > MAX_NAME_LENGTH || name.matches(".*\\d.*")) {
            log.warn("Validação falhou: Claim 'Name' é inválida. Valor: {}", name);
            return false;
        }
        return true;
    }

    // Regra: A claim Role deve conter apenas 1 dos três valores (Admin, Member e External)
    private boolean isRoleClaimValid(Claims claims) {
        String role = claims.get("Role", String.class);
        if (role == null || !VALID_ROLES.contains(role)) {
            log.warn("Validação falhou: Claim 'Role' é inválida. Valor: {}", role);
            return false;
        }
        return true;
    }

    // Regra: A claim Seed deve ser um número primo.
    private boolean isSeedClaimValid(Claims claims) {
        try {
            int seed = Integer.parseInt(claims.get("Seed", String.class));
            boolean isPrime = PrimeUtil.isPrime(seed);
            if (!isPrime) log.warn("Validação falhou: Claim 'Seed' não é um número primo. Valor: {}", seed);
            return isPrime;
        } catch (NumberFormatException e) {
            log.warn("Validação falhou: Claim 'Seed' não é um número válido.");
            return false;
        }
    }
}
