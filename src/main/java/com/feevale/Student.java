/*
**===========================================================================
**  @file    Student.java
**  @author  Eduardo Lorscheiter e Loreno Enrique Ribeiro
**  @class   Processamento Paralelo
**  @date    Outubro/2025
**  @version 1.0
**  @brief   Trabalho Prático 1 - Threads
**===========================================================================
*/

package com.feevale;

import com.feevale.utils.RandomUtils;

public class Student extends Thread {
    // Tempo mínimo e máximo de ida dos alunos até a parada de ônibus (milissegundos)
    private static final long TIME_GO_BUS_STOP_MIN = 30000;  // (30.000 ms = 30 s)
    private static final long TIME_GO_BUS_STOP_MAX = 600000; // (600.000 ms = 10 min)
    // Nome do aluno
    private String studentName;
    // Sala de aula do aluno
    private String classRoom;
    // Tempo de aula de todas as salas
    private long classTime;
    // Parada de ônibus da Universidade Feevale
    private BusStop busStop;

    public Student(String idStudent, String studentName, String classRoom, long classTime, BusStop busStop) {
        super(idStudent);
        this.studentName = studentName;
        this.classRoom = classRoom;
        this.classTime = classTime;
        this.busStop = busStop;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public String getStudentIdentification() {
        return studentName + " (" + classRoom + ")";
    }

    @Override
    public void run() {
        try {
            // Aguarda o tempo do aluno na aula
            Thread.sleep(classTime);

            // Aguarda o tempo que o aluno leva até o ponto de ônibus (aleatório)
            Thread.sleep(RandomUtils.randomMillis(TIME_GO_BUS_STOP_MIN, TIME_GO_BUS_STOP_MAX));

            synchronized (this) {
                // Indica a chegada do aluno no ponto de ônibus
                busStop.arrive(this);

                // Aguarda ser notificado (embarque no ônibus)
                this.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}