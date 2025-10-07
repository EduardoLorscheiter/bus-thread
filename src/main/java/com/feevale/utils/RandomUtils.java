/*
**===========================================================================
**  @file    RandomUtils.java
**  @author  Eduardo Lorscheiter e Loreno Enrique Ribeiro
**  @class   Processamento Paralelo
**  @date    Outubro/2025
**  @version 1.0
**  @brief   Trabalho Prático 1 - Threads
**===========================================================================
*/

package com.feevale.utils;

import java.util.Random;

public class RandomUtils {
    // Gerador de números aleatórios
    private static final Random random = new Random();

    /**
     * Gera um número inteiro aleatório
     *
     * @param min Número inteiro mínimo
     * @param max Número inteiro máximo
     * @return Número inteiro
     */
    public static int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Gera um tempo em milissegundos aleatório
     *
     * @param minMinutes Tempo em milissegundos mínimo
     * @param maxMinutes Tempo em milissegundos máximo
     * @return Tempo em milissegundos
     */
    public static long randomMillis(long minMillis, long maxMillis) {
        return (long) (minMillis + Math.random() * (maxMillis - minMillis + 1));
    }
}
