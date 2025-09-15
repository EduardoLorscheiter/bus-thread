/*
**===========================================================================
**  @file    University.java
**  @author  Eduardo Lorscheiter e Loreno Enrique Ribeiro
**  @class   Processamento Paralelo
**  @date    Setembro/2025
**  @version 1.0
**  @brief   Trabalho Prático 1 - Threads
**===========================================================================
*/

package com.feevale;

import java.util.Random;

public class University {
    // Número mínimo e máximo de salas da Universidade  
    private static final int NUM_CLASSROOMS_MIN = 1;
    private static final int NUM_CLASSROOMS_MAX = 10;
    // Número mínimo e máximo de alunos por sala da Universidade  
    private static final int NUM_STUDENTS_MIN = 10;
    private static final int NUM_STUDENTS_MAX = 30;
    // Tempo mínimo e máximo de aula por sala da Universidade (milissegundo)
    private static final int TIME_CLASSES_MIN = 2000;
    private static final int TIME_CLASSES_MAX = 10000;
    // Tempo mínimo e máximo por chegada de ônibus na Universidade (milissegundo)
    private static final int TIME_BUS_MIN = 2000;
    private static final int TIME_BUS_MAX = 3000;

    public static void main(String[] args) throws InterruptedException {
        Random rand = new Random();
        BusStop busStop = new BusStop();

        int numeroSalas = rand.nextInt(NUM_CLASSROOMS_MAX - NUM_CLASSROOMS_MIN + 1) + NUM_CLASSROOMS_MIN;
        int tempoAula = rand.nextInt(TIME_CLASSES_MAX - TIME_CLASSES_MIN + 1) + TIME_CLASSES_MIN;
        int numeroAlunos = rand.nextInt(NUM_STUDENTS_MAX - NUM_STUDENTS_MIN + 1) + NUM_STUDENTS_MIN;

        System.out.println("Universidade criada com " + numeroSalas + " salas, " +
                           numeroAlunos + " alunos e aulas de " + tempoAula + " ms.");

        // cria e inicia alunos
        for (int i = 0; i < numeroAlunos; i++) {
            Student aluno = new Student("M" + i, "Aluno-" + i, tempoAula, busStop);
            aluno.start();
        }

        // gera ônibus continuamente
        int idOnibus = 1;
        while (true) {
            Bus bus = new Bus(idOnibus++, busStop);
            bus.start();

            Thread.sleep(rand.nextInt(TIME_BUS_MAX - TIME_BUS_MIN + 1) + TIME_BUS_MIN);
        }
    }
}
