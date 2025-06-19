package com.instrospect.jwt_validator.util;

/**
 * Classe utilitária para operações relacionadas a números primos.
 */
public class PrimeUtil {

    /**
     * Construtor privado para evitar instanciação da classe utilitária.
     */
    private PrimeUtil() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não deve ser instanciada");
    }

    /**
     * Verifica se um número é primo.
     *
     * @param number o número a ser verificado
     * @return true se o número for primo, false caso contrário
     */
    public static boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        if (number <= 3) {
            return true;
        }
        if (number % 2 == 0 || number % 3 == 0) {
            return false;
        }

        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) {
                return false;
            }
        }

        return true;
    }
}
