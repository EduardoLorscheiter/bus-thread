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

import java.util.ArrayList;
import java.util.List;

import com.feevale.utils.RandomUtils;

public class University {
    // Número mínimo e máximo de salas da Universidade Feevale
    private static final int NUM_CLASSROOMS_MIN = 5;
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
    // Número total de alunos na Universidade Feevale
    private static int studentsAtUniversity = 0;

    public static void main(String[] args) throws InterruptedException {
        simulateDayClass();
    }

    /**
     * Simula um dia de aulas, alunos indo ao ponto e ônibus circulando
     */
    private static void simulateDayClass() throws InterruptedException {
        printBanner("Simulação de um dia de aulas na Universidade Feevale - Iniciada");
        System.out.println();

        // Parada de ônibus da Universidade Feevale
        BusStop busStop = new BusStop();

        // Gera um número aleatório de salas de aula
        int numberRooms = RandomUtils.randomInt(NUM_CLASSROOMS_MIN, NUM_CLASSROOMS_MAX);
        // Gera um tempo (em milissegundos) aleatório para as aulas
        long classTime = RandomUtils.randomMillis(TIME_CLASSES_MIN, TIME_CLASSES_MAX);
        // Converte o tempo da aula de milissegundos para minutos e segundos
        long totalSeconds = classTime / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        System.out.printf("Universidade Feevale criada com %d sala(s) e aulas de %02d:%02d minutos (%d ms).\n",
                numberRooms, minutes, seconds, classTime);

        // Gera todas as salas de aula da Universidade Feevale e seus alunos
        generateStudents(numberRooms, classTime, busStop);
        // Aguarda o final das aulas
        waitForClassEnd(classTime);
        // Gera todos os ônibus por um tempo determinado
        runBusCirculation(busStop);

        System.out.println();
        printBanner("Simulação de um dia de aulas na Universidade Feevale - Encerrada");
    }

    /**
     * Exibe uma mensagem em um banner formatado
     * 
     * @param message
     */
    private static void printBanner(String message) {
        // Número de sinais > ou < ao redor da mensagem
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
     * Gera todas as salas de aula da Universidade Feevale e seus alunos
     *
     * @param numberRooms Número de salas de aula
     * @param classTime   Tempo para as aulas
     * @param busStop     Parada de ônibus da Universidade Feevale
     */
    private static void generateStudents(int numberRooms, long classTime, BusStop busStop) {
        // Gera, para cada sala, um número aleatório de alunos
        for (int room = 1; room <= numberRooms; room++) {
            int studentsInRoom = RandomUtils.randomInt(NUM_STUDENTS_MIN, NUM_STUDENTS_MAX);
            System.out.println("Sala " + room + " terá " + studentsInRoom + " alunos.");

            // Cria e dispara a thread para cada um dos alunos
            for (int studentInRoom = 1; studentInRoom <= studentsInRoom; studentInRoom++) {
                String idStudent = "S" + room + "-A" + studentInRoom;
                String studentName;
                if (studentInRoom < 10) {
                    studentName = "Aluno-0" + studentInRoom;
                } else {
                    studentName = "Aluno-" + studentInRoom;
                }
                String classRoom;
                if (numberRooms < 10) {
                    classRoom = "Sala 0" + room;
                } else {
                    classRoom = "Sala " + room;
                }
                Student student = new Student(idStudent,
                        studentName,
                        classRoom,
                        classTime,
                        busStop);
                student.start();
                studentsAtUniversity++;
            }
        }

        System.out.println("Total de alunos na universidade Feevale: " + studentsAtUniversity);
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
        System.out.println(">>> Circulação de ônibus iniciada...");

        List<Bus> busList = new ArrayList<>();
        int idBus = 1;

        while (true) {
            // Aguarda o tempo até o próximo ônibus
            Thread.sleep(RandomUtils.randomMillis(TIME_BUS_ARRIVE_MIN, TIME_BUS_ARRIVE_MAX));

            // Se todos os alunos na Universidade Feevale já embarcaram
            if (busStop.studentsBoarded() >= studentsAtUniversity) {
                break;
            }

            // Gera um novo ônibus
            Bus bus = new Bus(idBus++, busStop);
            bus.start();
            busList.add(bus);
        }

        // Espera todos os ônibus terminarem
        for (Bus bus : busList) {
            bus.join();
        }

        System.out.println(">>> Circulação de ônibus encerrada...");
    }
}