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

import java.util.LinkedList;
import java.util.Queue;

public class BusStop {
    // Tempo para embarque de um aluno (em milissegundos)
    private static final long TIME_BOARDING_STUDENT = 1000; // (1.000 ms = 1 s)
    // Lista dos alunos
    private final Queue<Student> studentsQueue = new LinkedList<>();

    // Número total de alunos que embarcaram em algum ônibus
    private int studentsBoarded = 0;

    // Retorna o número total de alunos que embarcaram em algum ônibus
    public int studentsBoarded() {
        return studentsBoarded;
    }

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
            studentsBoarded++;
            boarded++;

            try {
                // Simula o tempo de embarque do aluno
                Thread.sleep(TIME_BOARDING_STUDENT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Ônibus " + bus.getIdBus() + ": " + student.getStudentIdentification()
                    + " embarcou. " + status());

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
        return "Total esperando = " + studentsQueue.size() + " | Total embarcado = " + studentsBoarded;
    }
}