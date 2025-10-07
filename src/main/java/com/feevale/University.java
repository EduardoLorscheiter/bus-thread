/*
**===========================================================================
**  @file    University.java
**  @author  Eduardo Lorscheiter e Loreno Enrique Ribeiro
**  @class   Processamento Paralelo
**  @date    Outubro/2025
**  @version 1.0
**  @brief   Trabalho Prático 1 - Threads
**===========================================================================
*/

package com.feevale;

import com.feevale.utils.RandomUtils;

public class University {
    // Número de dias da simulação
    private static final int NUM_DAYS = 1;
    // Número mínimo e máximo de salas da Universidade Feevale
    private static final int NUM_CLASSROOMS_MIN = 1;
    private static final int NUM_CLASSROOMS_MAX = 10;
    // Número mínimo e máximo de alunos por sala na Universidade Feevale
    private static final int NUM_STUDENTS_MIN = 10;
    private static final int NUM_STUDENTS_MAX = 30;
    // Tempo mínimo e máximo de aula na Universidade Feevale (milissegundos)
    private static final long TIME_CLASSES_MIN = 120000; // (120.000 ms = 2 min)
    private static final long TIME_CLASSES_MAX = 600000; // (600.000 ms = 10 min)
    // Tempo mínimo e máximo de novo ônibus na Universidade Feevale (milissegundos)
    private static final long TIME_BUS_ARRIVE_MIN = 120000; // (120.000 ms = 2 min)
    private static final long TIME_BUS_ARRIVE_MAX = 180000; // (180.000 ms = 3 min)
    // Tempo máximo de circulação dos ônibus na Universidade Feevale (milissegundos)
    private static final long TIME_BUS_CIRCULATION_MAX = 900000; // (900.000 ms = 15 min)

    public static void main(String[] args) throws InterruptedException {
        printBanner("Simulação de " + NUM_DAYS + " dia(s) iniciada");

        for (int day = 1; day <= NUM_DAYS; day++) {
            simulateDay(day);
        }

        printBanner("Simulação de " + NUM_DAYS + " dia(s) encerrada");
    }

    /**
     * Exibe uma mensagem em um banner formatado
     * 
     * @param message
     */
    private static void printBanner(String message) {
        // número de sinais > ou < ao redor da mensagem
        int padding = 5;
        int totalLength = message.length() + padding * 2 + 2;

        // Linha de cima e de baixo
        String line = "=".repeat(totalLength);

        // Linha do meio com mensagem
        String middle = ">>>>> " + message + " <<<<<";

        System.out.println(line);
        System.out.println(middle);
        System.out.println(line);
    }

    /**
     * Simula um dia de aulas, alunos indo ao ponto e ônibus circulando
     *
     * @param day Dia da simulação
     */
    private static void simulateDay(Integer day) throws InterruptedException {
        System.out.println("\n===== Dia " + day + " iniciado =====");

        // Parada de ônibus da Universidade Feevale
        BusStop busStop = new BusStop();

        // Gera um número aleatório de salas de aula
        int numberRooms = RandomUtils.randomInt(NUM_CLASSROOMS_MIN, NUM_CLASSROOMS_MAX);
        // Gera um tempo (em milissegundos) aleatório para as aulas
        long classTime = RandomUtils.randomMillis(TIME_CLASSES_MIN, TIME_CLASSES_MAX);

        System.out.printf("Universidade Feevale criada com %d sala(s) e aulas de %.2f minutos (%d ms).\n",
                numberRooms, millisToMinutes(classTime), classTime);

        // Gera todas as salas de aula da Universidade Feevale e seus alunos
        generateStudents(numberRooms, classTime, busStop);
        // Aguarda o final das aulas
        waitForClassEnd(classTime);
        // Gera todos os ônibus por um tempo determinado
        runBusCirculation(busStop);

        System.out.println("===== Dia " + day + " finalizado =====\n");
    }

    /**
     * Gera todas as salas de aula da Universidade Feevale e seus alunos
     *
     * @param numberRooms Número de salas de aula
     * @param classTime   Tempo para as aulas
     * @param busStop     Parada de ônibus da Universidade Feevale
     */
    private static void generateStudents(int numberRooms, long classTime, BusStop busStop) {
        int totalStudents = 0;

        // Gera, para cada sala, um número aleatório de alunos
        for (int room = 1; room <= numberRooms; room++) {
            int studentsInRoom = RandomUtils.randomInt(NUM_STUDENTS_MIN, NUM_STUDENTS_MAX);
            System.out.println("Sala " + room + " terá " + studentsInRoom + " alunos.");

            // Cria e dispara a thread para cada um dos alunos
            for (int studentInRoom = 0; studentInRoom < studentsInRoom; studentInRoom++) {
                String idStudent = "S" + room + "-A" + studentInRoom;
                String studentName = "Aluno-" + studentInRoom;
                String classRoom = "Sala " + room;
                Student student = new Student(idStudent,
                        studentName,
                        classRoom,
                        classTime,
                        busStop);
                student.start();
                totalStudents++;
            }
        }

        System.out.println("Total de alunos na universidade Feevale: " + totalStudents);
    }

    /**
     * Aguarda o final das aulas
     *
     * @param classTime Tempo para as aulas
     */
    private static void waitForClassEnd(long classTime) throws InterruptedException {
        System.out.println(">>> Aulas iniciadas...");

        // Encontra o horário de início das aulas
        long startClasses = System.currentTimeMillis();
        // Encontra o horário de término das aulas
        long endClasses = startClasses + classTime;

        // Aguarda o término das aulas (sem gerar ônibus)
        while (System.currentTimeMillis() < endClasses) {
            // Calcula o tempo decorrido e o tempo restante
            long elapsed = System.currentTimeMillis() - startClasses;
            long remaining = endClasses - System.currentTimeMillis();

            // Converte os tempos para minutos e segundos
            long elapsedMinutes = (elapsed / 1000) / 60;
            long elapsedSeconds = (elapsed / 1000) % 60;

            long remainingMinutes = (remaining / 1000) / 60;
            long remainingSeconds = (remaining / 1000) % 60;

            System.out.printf("\r[Tempo de aula] Passaram %02d:%02d | Restam %02d:%02d",
                    elapsedMinutes, elapsedSeconds, remainingMinutes, remainingSeconds);

            // Obs.1: Aqui a thread "dorme" por 1 s, liberando a CPU para outras tarefas
            // Obs.2: É mais eficiente, não fica rodando sem parar só para verificar o tempo
            Thread.sleep(1000);
        }

        System.out.println("\n>>> Aulas encerradas...");
    }

    /**
     * Gera todos os ônibus por um tempo determinado
     *
     * @param busStop Parada de ônibus da Universidade Feevale
     */
    private static void runBusCirculation(BusStop busStop) throws InterruptedException {
        System.out.printf(">>> Circulação de ônibus por %.2f minutos iniciada...\n",
                millisToMinutes(TIME_BUS_CIRCULATION_MAX));

        int idBus = 1;
        long endTime = System.currentTimeMillis() + TIME_BUS_CIRCULATION_MAX;

        while (System.currentTimeMillis() < endTime) {
            Thread.sleep(RandomUtils.randomMillis(TIME_BUS_ARRIVE_MIN, TIME_BUS_ARRIVE_MAX));
            Bus bus = new Bus(idBus++, busStop);
            bus.start();
        }

        System.out.println(">>> Circulação de ônibus encerrada...");
    }

    /**
     * Converte milissegundos para minutos
     *
     * @param millis Tempo em milissegundos
     * @return Tempo em minutos
     */
    private static double millisToMinutes(long millis) {
        // 60.000 ms = 1 min
        return millis / 60000.0;
    }
}