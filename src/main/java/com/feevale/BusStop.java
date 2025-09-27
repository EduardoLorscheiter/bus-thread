/*
**===========================================================================
**  @file    BusStop.java
**  @author  Eduardo Lorscheiter e Loreno Enrique Ribeiro
**  @class   Processamento Paralelo
**  @date    Setembro/2025
**  @version 1.0
**  @brief   Trabalho Prático 1 - Threads
**===========================================================================
*/

package com.feevale;

import java.util.LinkedList;
import java.util.Queue;

public class BusStop {
    private final Queue<Student> studentsQueue = new LinkedList<>();

    // Verifica se ainda há alunos na fila
    public boolean isEmpty() {
        return studentsQueue.isEmpty();
    }

    // Chegada do aluno na fila para pegar o ônibus
    public synchronized void arrive(Student student) {
        studentsQueue.add(student);
        System.out.println(student.getStudentName() + " chegou no ponto. Total esperando = " + studentsQueue.size());
    }

    // Chegada do ônibus na parada e embarque de alunos até a capacidade máxima
    public synchronized void board(Bus bus) {
        int capacity = bus.getCapacity();
        int boarded = 0;

        // Enquanto possuir alunos na fila e não lotou o ônibus
        while (!studentsQueue.isEmpty() && boarded < capacity) {
            Student aluno = studentsQueue.poll();
            System.out.println("Ônibus " + bus.getIdBus() + ": " + aluno.getStudentName()
                    + " embarcou. Total esperando = " + studentsQueue.size());
            boarded++;
        }

        // Se nenhum aluno embarcou
        if (boarded == 0) {
            System.out.println("Ônibus " + bus.getIdBus() + " partiu vazio.");
        } else {
            System.out.println("Ônibus " + bus.getIdBus() + " partiu com " + boarded + " alunos.");
        }
    }
}