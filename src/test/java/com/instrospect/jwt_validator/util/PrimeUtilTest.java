package com.instrospect.jwt_validator.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class PrimeUtilTest {

    @Test
    @DisplayName("Should return false for negative numbers")
    void testIsPrime_NegativeNumbers_ShouldReturnFalse() {
        assertFalse(PrimeUtil.isPrime(-1));
        assertFalse(PrimeUtil.isPrime(-2));
        assertFalse(PrimeUtil.isPrime(-10));
        assertFalse(PrimeUtil.isPrime(-100));
    }

    @Test
    @DisplayName("Should return false for 0 and 1")
    void testIsPrime_ZeroAndOne_ShouldReturnFalse() {
        assertFalse(PrimeUtil.isPrime(0));
        assertFalse(PrimeUtil.isPrime(1));
    }

    @Test
    @DisplayName("Should return true for small prime numbers")
    void testIsPrime_SmallPrimes_ShouldReturnTrue() {
        assertTrue(PrimeUtil.isPrime(2));
        assertTrue(PrimeUtil.isPrime(3));
        assertTrue(PrimeUtil.isPrime(5));
        assertTrue(PrimeUtil.isPrime(7));
        assertTrue(PrimeUtil.isPrime(11));
        assertTrue(PrimeUtil.isPrime(13));
        assertTrue(PrimeUtil.isPrime(17));
        assertTrue(PrimeUtil.isPrime(19));
        assertTrue(PrimeUtil.isPrime(23));
        assertTrue(PrimeUtil.isPrime(29));
    }

    @Test
    @DisplayName("Should return false for small non-prime numbers")
    void testIsPrime_SmallNonPrimes_ShouldReturnFalse() {
        assertFalse(PrimeUtil.isPrime(4));
        assertFalse(PrimeUtil.isPrime(6));
        assertFalse(PrimeUtil.isPrime(8));
        assertFalse(PrimeUtil.isPrime(9));
        assertFalse(PrimeUtil.isPrime(10));
        assertFalse(PrimeUtil.isPrime(12));
        assertFalse(PrimeUtil.isPrime(14));
        assertFalse(PrimeUtil.isPrime(15));
        assertFalse(PrimeUtil.isPrime(16));
        assertFalse(PrimeUtil.isPrime(18));
        assertFalse(PrimeUtil.isPrime(20));
    }

    @Test
    @DisplayName("Should return true for medium-sized prime numbers")
    void testIsPrime_MediumPrimes_ShouldReturnTrue() {
        assertTrue(PrimeUtil.isPrime(31));
        assertTrue(PrimeUtil.isPrime(37));
        assertTrue(PrimeUtil.isPrime(41));
        assertTrue(PrimeUtil.isPrime(43));
        assertTrue(PrimeUtil.isPrime(47));
        assertTrue(PrimeUtil.isPrime(53));
        assertTrue(PrimeUtil.isPrime(59));
        assertTrue(PrimeUtil.isPrime(61));
        assertTrue(PrimeUtil.isPrime(67));
        assertTrue(PrimeUtil.isPrime(71));
        assertTrue(PrimeUtil.isPrime(73));
        assertTrue(PrimeUtil.isPrime(79));
        assertTrue(PrimeUtil.isPrime(83));
        assertTrue(PrimeUtil.isPrime(89));
        assertTrue(PrimeUtil.isPrime(97));
    }

    @Test
    @DisplayName("Should return false for medium-sized non-prime numbers")
    void testIsPrime_MediumNonPrimes_ShouldReturnFalse() {
        assertFalse(PrimeUtil.isPrime(21)); // 3 * 7
        assertFalse(PrimeUtil.isPrime(25)); // 5 * 5
        assertFalse(PrimeUtil.isPrime(27)); // 3 * 9
        assertFalse(PrimeUtil.isPrime(33)); // 3 * 11
        assertFalse(PrimeUtil.isPrime(35)); // 5 * 7
        assertFalse(PrimeUtil.isPrime(39)); // 3 * 13
        assertFalse(PrimeUtil.isPrime(49)); // 7 * 7
        assertFalse(PrimeUtil.isPrime(51)); // 3 * 17
        assertFalse(PrimeUtil.isPrime(55)); // 5 * 11
        assertFalse(PrimeUtil.isPrime(57)); // 3 * 19
        assertFalse(PrimeUtil.isPrime(63)); // 7 * 9
        assertFalse(PrimeUtil.isPrime(65)); // 5 * 13
        assertFalse(PrimeUtil.isPrime(69)); // 3 * 23
        assertFalse(PrimeUtil.isPrime(75)); // 3 * 25
        assertFalse(PrimeUtil.isPrime(77)); // 7 * 11
        assertFalse(PrimeUtil.isPrime(81)); // 9 * 9
        assertFalse(PrimeUtil.isPrime(85)); // 5 * 17
        assertFalse(PrimeUtil.isPrime(87)); // 3 * 29
        assertFalse(PrimeUtil.isPrime(91)); // 7 * 13
        assertFalse(PrimeUtil.isPrime(93)); // 3 * 31
        assertFalse(PrimeUtil.isPrime(95)); // 5 * 19
        assertFalse(PrimeUtil.isPrime(99)); // 9 * 11
    }

    @Test
    @DisplayName("Should return true for larger prime numbers")
    void testIsPrime_LargePrimes_ShouldReturnTrue() {
        assertTrue(PrimeUtil.isPrime(101));
        assertTrue(PrimeUtil.isPrime(103));
        assertTrue(PrimeUtil.isPrime(107));
        assertTrue(PrimeUtil.isPrime(109));
        assertTrue(PrimeUtil.isPrime(113));
        assertTrue(PrimeUtil.isPrime(127));
        assertTrue(PrimeUtil.isPrime(131));
        assertTrue(PrimeUtil.isPrime(137));
        assertTrue(PrimeUtil.isPrime(139));
        assertTrue(PrimeUtil.isPrime(149));
        assertTrue(PrimeUtil.isPrime(151));
        assertTrue(PrimeUtil.isPrime(157));
        assertTrue(PrimeUtil.isPrime(163));
        assertTrue(PrimeUtil.isPrime(167));
        assertTrue(PrimeUtil.isPrime(173));
        assertTrue(PrimeUtil.isPrime(179));
        assertTrue(PrimeUtil.isPrime(181));
        assertTrue(PrimeUtil.isPrime(191));
        assertTrue(PrimeUtil.isPrime(193));
        assertTrue(PrimeUtil.isPrime(197));
        assertTrue(PrimeUtil.isPrime(199));
    }

    @Test
    @DisplayName("Should return false for larger non-prime numbers")
    void testIsPrime_LargeNonPrimes_ShouldReturnFalse() {
        assertFalse(PrimeUtil.isPrime(100)); // 10 * 10
        assertFalse(PrimeUtil.isPrime(102)); // 2 * 51
        assertFalse(PrimeUtil.isPrime(104)); // 8 * 13
        assertFalse(PrimeUtil.isPrime(105)); // 3 * 35
        assertFalse(PrimeUtil.isPrime(106)); // 2 * 53
        assertFalse(PrimeUtil.isPrime(108)); // 4 * 27
        assertFalse(PrimeUtil.isPrime(110)); // 10 * 11
        assertFalse(PrimeUtil.isPrime(111)); // 3 * 37
        assertFalse(PrimeUtil.isPrime(112)); // 16 * 7
        assertFalse(PrimeUtil.isPrime(114)); // 6 * 19
        assertFalse(PrimeUtil.isPrime(115)); // 5 * 23
        assertFalse(PrimeUtil.isPrime(116)); // 4 * 29
        assertFalse(PrimeUtil.isPrime(117)); // 9 * 13
        assertFalse(PrimeUtil.isPrime(118)); // 2 * 59
        assertFalse(PrimeUtil.isPrime(119)); // 7 * 17
        assertFalse(PrimeUtil.isPrime(120)); // 8 * 15
        assertFalse(PrimeUtil.isPrime(121)); // 11 * 11
        assertFalse(PrimeUtil.isPrime(122)); // 2 * 61
        assertFalse(PrimeUtil.isPrime(123)); // 3 * 41
        assertFalse(PrimeUtil.isPrime(124)); // 4 * 31
        assertFalse(PrimeUtil.isPrime(125)); // 5 * 25
        assertFalse(PrimeUtil.isPrime(126)); // 6 * 21
    }

    @Test
    @DisplayName("Should return true for very large prime numbers")
    void testIsPrime_VeryLargePrimes_ShouldReturnTrue() {
        assertTrue(PrimeUtil.isPrime(211));
        assertTrue(PrimeUtil.isPrime(223));
        assertTrue(PrimeUtil.isPrime(227));
        assertTrue(PrimeUtil.isPrime(229));
        assertTrue(PrimeUtil.isPrime(233));
        assertTrue(PrimeUtil.isPrime(239));
        assertTrue(PrimeUtil.isPrime(241));
        assertTrue(PrimeUtil.isPrime(251));
        assertTrue(PrimeUtil.isPrime(257));
        assertTrue(PrimeUtil.isPrime(263));
        assertTrue(PrimeUtil.isPrime(269));
        assertTrue(PrimeUtil.isPrime(271));
        assertTrue(PrimeUtil.isPrime(277));
        assertTrue(PrimeUtil.isPrime(281));
        assertTrue(PrimeUtil.isPrime(283));
        assertTrue(PrimeUtil.isPrime(293));
        assertTrue(PrimeUtil.isPrime(307));
        assertTrue(PrimeUtil.isPrime(311));
        assertTrue(PrimeUtil.isPrime(313));
        assertTrue(PrimeUtil.isPrime(317));
        assertTrue(PrimeUtil.isPrime(331));
        assertTrue(PrimeUtil.isPrime(337));
        assertTrue(PrimeUtil.isPrime(347));
        assertTrue(PrimeUtil.isPrime(349));
        assertTrue(PrimeUtil.isPrime(353));
        assertTrue(PrimeUtil.isPrime(359));
        assertTrue(PrimeUtil.isPrime(367));
        assertTrue(PrimeUtil.isPrime(373));
        assertTrue(PrimeUtil.isPrime(379));
        assertTrue(PrimeUtil.isPrime(383));
        assertTrue(PrimeUtil.isPrime(389));
        assertTrue(PrimeUtil.isPrime(397));
    }

    @Test
    @DisplayName("Should return false for very large non-prime numbers")
    void testIsPrime_VeryLargeNonPrimes_ShouldReturnFalse() {
        assertFalse(PrimeUtil.isPrime(200)); // 8 * 25
        assertFalse(PrimeUtil.isPrime(201)); // 3 * 67
        assertFalse(PrimeUtil.isPrime(202)); // 2 * 101
        assertFalse(PrimeUtil.isPrime(203)); // 7 * 29
        assertFalse(PrimeUtil.isPrime(204)); // 12 * 17
        assertFalse(PrimeUtil.isPrime(205)); // 5 * 41
        assertFalse(PrimeUtil.isPrime(206)); // 2 * 103
        assertFalse(PrimeUtil.isPrime(207)); // 9 * 23
        assertFalse(PrimeUtil.isPrime(208)); // 16 * 13
        assertFalse(PrimeUtil.isPrime(209)); // 11 * 19
        assertFalse(PrimeUtil.isPrime(210)); // 14 * 15
        assertFalse(PrimeUtil.isPrime(212)); // 4 * 53
        assertFalse(PrimeUtil.isPrime(213)); // 3 * 71
        assertFalse(PrimeUtil.isPrime(214)); // 2 * 107
        assertFalse(PrimeUtil.isPrime(215)); // 5 * 43
        assertFalse(PrimeUtil.isPrime(216)); // 8 * 27
        assertFalse(PrimeUtil.isPrime(217)); // 7 * 31
        assertFalse(PrimeUtil.isPrime(218)); // 2 * 109
        assertFalse(PrimeUtil.isPrime(219)); // 3 * 73
        assertFalse(PrimeUtil.isPrime(220)); // 20 * 11
        assertFalse(PrimeUtil.isPrime(221)); // 13 * 17
        assertFalse(PrimeUtil.isPrime(222)); // 6 * 37
    }

    @Test
    @DisplayName("Should handle edge cases with numbers divisible by 2 and 3")
    void testIsPrime_EdgeCasesDivisibleBy2And3_ShouldReturnFalse() {
        // Numbers divisible by 2
        assertFalse(PrimeUtil.isPrime(22));
        assertFalse(PrimeUtil.isPrime(24));
        assertFalse(PrimeUtil.isPrime(26));
        assertFalse(PrimeUtil.isPrime(28));
        assertFalse(PrimeUtil.isPrime(30));

        // Numbers divisible by 3
        assertFalse(PrimeUtil.isPrime(21));
        assertFalse(PrimeUtil.isPrime(24));
        assertFalse(PrimeUtil.isPrime(27));
        assertFalse(PrimeUtil.isPrime(30));
        assertFalse(PrimeUtil.isPrime(33));
    }

    @Test
    @DisplayName("Should test numbers that follow the 6k±1 pattern but are not prime")
    void testIsPrime_SixKPlusMinusOnePattern_NonPrimes_ShouldReturnFalse() {
        // These numbers follow the 6k±1 pattern but are composite
        assertFalse(PrimeUtil.isPrime(25)); // 6*4+1 = 25 = 5*5
        assertFalse(PrimeUtil.isPrime(35)); // 6*6-1 = 35 = 5*7
        assertFalse(PrimeUtil.isPrime(49)); // 6*8+1 = 49 = 7*7
        assertFalse(PrimeUtil.isPrime(65)); // 6*11-1 = 65 = 5*13
        assertFalse(PrimeUtil.isPrime(77)); // 6*13-1 = 77 = 7*11
        assertFalse(PrimeUtil.isPrime(91)); // 6*15+1 = 91 = 7*13
        assertFalse(PrimeUtil.isPrime(115)); // 6*19+1 = 115 = 5*23
        assertFalse(PrimeUtil.isPrime(119)); // 6*20-1 = 119 = 7*17
        assertFalse(PrimeUtil.isPrime(121)); // 6*20+1 = 121 = 11*11
        assertFalse(PrimeUtil.isPrime(143)); // 6*24-1 = 143 = 11*13
        assertFalse(PrimeUtil.isPrime(169)); // 6*28+1 = 169 = 13*13
        assertFalse(PrimeUtil.isPrime(187)); // 6*31+1 = 187 = 11*17
    }

    @Test
    @DisplayName("Should throw UnsupportedOperationException when trying to instantiate PrimeUtil")
    void testPrivateConstructor_ShouldThrowUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> {
            Constructor<PrimeUtil> constructor = PrimeUtil.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            try {
                constructor.newInstance();
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    @DisplayName("Should verify constructor exception message")
    void testPrivateConstructor_ShouldHaveCorrectExceptionMessage() {
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            Constructor<PrimeUtil> constructor = PrimeUtil.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            try {
                constructor.newInstance();
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });

        assertEquals("Esta é uma classe utilitária e não deve ser instanciada", exception.getMessage());
    }
}
