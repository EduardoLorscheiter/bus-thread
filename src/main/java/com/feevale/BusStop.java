/*
**===========================================================================
**  @file    BusStop.java
**  @author  Eduardo Lorscheiter e Loreno Enrique Ribeiro
**  @class   Processamento Paralelo
**  @date    Outubro/2025
**  @version 1.0
**  @brief   Trabalho Prático 1 - Threads
**===========================================================================
*/

package com.feevale;

import com.feevale.utils.RandomUtils;

import java.util.LinkedList;
import java.util.Queue;

public class BusStop {
    // Tempo mínimo e máximo para embarque de um aluno (em milissegundos)
    private static final long TIME_BOARDING_MIN = 1000; // (1.000 ms = 1 s)
    private static final long TIME_BOARDING_MAX = 5000; // (5.000 ms = 5 s)
    // Lista dos alunos
    private final Queue<Student> studentsQueue = new LinkedList<>();

    // Chegada do aluno na fila para pegar o ônibus
    public synchronized void arrive(Student student) {
        studentsQueue.add(student);
        System.out.println(student.getStudentIdentification() + " chegou no ponto. " + status());
    }

    // Chegada do ônibus na parada e embarque de alunos até a capacidade máxima
    public synchronized void board(Bus bus) {
        System.out.println("Ônibus " + bus.getIdBus() + " chegou no ponto.");

        int capacity = bus.getCapacity();
        int boarded = 0;

        // Percorre a lista enquanto possuir alunos na fila e não lotou o ônibus
        while (!studentsQueue.isEmpty() && boarded < capacity) {
            Student student = studentsQueue.poll();

            System.out.println("Ônibus " + bus.getIdBus() + ": " + student.getStudentIdentification()
                    + " embarcando...");
            try {
                // Simula o tempo de embarque do aluno
                long boardingTime = RandomUtils.randomMillis(TIME_BOARDING_MIN, TIME_BOARDING_MAX);
                Thread.sleep(boardingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Ônibus " + bus.getIdBus() + ": " + student.getStudentIdentification()
                    + " embarcou. " + status());
            boarded++;

            // Notifica que o aluno embarcou
            synchronized (student) {
                student.notify();
            }
        }

        // Se nenhum aluno embarcou
        if (boarded == 0) {
            System.out.println("Ônibus " + bus.getIdBus() + " partiu vazio.");
        } else {
            System.out.println("Ônibus " + bus.getIdBus() + " partiu com " + boarded + " alunos.");
        }
    }

    /**
     * Exibe o status atual
     */
    private String status() {
        return "Total esperando = " + studentsQueue.size();
    }
}