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
    // Número de dias da simulação
    private static final int NUM_DAYS = 3;
    // Número mínimo e máximo de salas da Universidade Feevale
    private static final int NUM_CLASSROOMS_MIN = 5;
    private static final int NUM_CLASSROOMS_MAX = 10;
    // Número mínimo e máximo de alunos por sala da Universidade Feevale
    private static final int NUM_STUDENTS_MIN = 10;
    private static final int NUM_STUDENTS_MAX = 30;
    // Tempo mínimo e máximo de aula de todas as salas da Universidade Feevale (milissegundo)
    private static final int TIME_CLASSES_MIN = 120000;
    private static final int TIME_CLASSES_MAX = 600000;
    // Tempo mínimo e máximo do intervalo para enviar o próximo ônibus na Universidade Feevale (milissegundo)
    private static final int TIME_BUS_MIN = 120000;
    private static final int TIME_BUS_MAX = 180000;
    // Gerador de números aleatórios
    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        BusStop busStop = new BusStop();

        // Realiza a simulação por dias
        for (int day = 1; day <= NUM_DAYS; day++) {
            simulateDay(day, busStop);
        }

        System.out.println(">>> Fim da simulação de " + NUM_DAYS + " dias <<<");
    }

    // Simula um dia de aulas, alunos indo ao ponto e ônibus circulando
    private static void simulateDay(Integer day, BusStop busStop) throws InterruptedException {
        System.out.println("\n===== Início do dia " + day + " =====");

        // Gera um número aleatório de salas de aulas
        int numberRooms = random.nextInt(NUM_CLASSROOMS_MAX - NUM_CLASSROOMS_MIN + 1) + NUM_CLASSROOMS_MIN;
        // Gera um tempo (em milissegundos) aleatório para as aulas
        int classTime = random.nextInt(TIME_CLASSES_MAX - TIME_CLASSES_MIN + 1) + TIME_CLASSES_MIN;
        // Converte o tempo em milissegundos para minutos (com casas decimais)
        double classTimeMinutes = classTime / 60000.0;

        System.out.printf("Universidade Feevale criada com %d salas e aulas de %.2f minutos (%d ms).%n",
                numberRooms, classTimeMinutes, classTime);

        int totalNumberStudents = 0;

        // Gera, para cada sala, um número aleatório de alunos
        for (int room = 1; room <= numberRooms; room++) {
            int numberStudentsClassroom = random.nextInt(NUM_STUDENTS_MAX - NUM_STUDENTS_MIN + 1) + NUM_STUDENTS_MIN;
            System.out.println("Sala " + room + " terá " + numberStudentsClassroom + " alunos.");

            // Cria e dispara a thread para cada um dos alunos
            for (int i = 0; i < numberStudentsClassroom; i++) {
                Student aluno = new Student("S" + room + "-M" + i, "Aluno-" + i, "Sala " + room, classTime, busStop);
                aluno.start();
                totalNumberStudents++;
            }
        }

        System.out.println("Total de alunos na universidade Feevale: " + totalNumberStudents);

        // Gera ônibus até o fim das aulas
        int idBus = 1;
        long endClasses = System.currentTimeMillis() + classTime;
        while (System.currentTimeMillis() < endClasses) {
            idBus = generateBus(idBus, busStop);
        }

        // Após o fim das aulas, gera ônibus até que não haja mais alunos na parada
        while (!busStop.isEmpty()) {
            idBus = generateBus(idBus, busStop);
        }

        System.out.println("===== Fim do dia " + day + " =====\n");
    }

    // Gera um novo ônibus
    private static int generateBus(int idBus, BusStop busStop) throws InterruptedException {
        Thread.sleep(random.nextInt(TIME_BUS_MAX - TIME_BUS_MIN + 1) + TIME_BUS_MIN);
        Bus bus = new Bus(idBus++, busStop);
        bus.start();
        return idBus;
    }
}
