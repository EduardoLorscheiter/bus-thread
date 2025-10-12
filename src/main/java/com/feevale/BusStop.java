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
    // Indica se o ônibus está realizando o embarque dos alunos no momento
    private boolean busBoarding = false;

    // Retorna o número total de alunos que embarcaram em algum ônibus
    public int studentsBoarded() {
        return studentsBoarded;
    }

    // Chegada do aluno na fila para pegar o ônibus
    public void arrive(Student student) {
        synchronized (studentsQueue) {
            studentsQueue.add(student);

            if (busBoarding) {
                // Mensagem exibida quando o aluno chega durante o embarque
                System.out.println(student.getStudentIdentification()
                        + " chegou no ponto durante o embarque, o aluno vai esperar o próximo ônibus. " + status());
            } else {
                System.out.println(student.getStudentIdentification() + " chegou no ponto. " + status());
            }
        }
    }

    // Chegada do ônibus na parada e embarque de alunos até a capacidade máxima
    public void board(Bus bus) {
        System.out.println("Ônibus " + bus.getIdBus() + " chegou no ponto.");

        // Capacidade máxima do ônibus
        int capacity = bus.getCapacity();
        // Número total de alunos que embarcaram no ônibus atual
        int boarded = 0;
        // Quantidade de alunos na parada no momento da chegada do ônibus
        int studentsAtBusStop;

        synchronized (studentsQueue) {
            studentsAtBusStop = Math.min(studentsQueue.size(), capacity);
            busBoarding = true;
        }

        // Realiza o embarque dos alunos que já estavam na parada quando o ônibus chegou
        for (int i = 0; i < studentsAtBusStop; i++) {
            Student student;

            synchronized (studentsQueue) {
                student = studentsQueue.poll();
                studentsBoarded++;
                boarded++;

                System.out.println("Ônibus " + bus.getIdBus() + ": " + student.getStudentIdentification()
                        + " embarcou. " + status());
            }

            try {
                // Simula o tempo de embarque do aluno
                Thread.sleep(TIME_BOARDING_STUDENT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Notifica que o aluno embarcou
            synchronized (student) {
                student.notify();
            }
        }

        busBoarding = false;

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
        return String.format("Total esperando = %02d | Total embarcado = %02d",
                studentsQueue.size(), studentsBoarded);
    }
}