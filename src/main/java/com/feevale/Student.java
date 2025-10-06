/*
**===========================================================================
**  @file    Student.java
**  @author  Eduardo Lorscheiter e Loreno Enrique Ribeiro
**  @class   Processamento Paralelo
**  @date    Setembro/2025
**  @version 1.0
**  @brief   Trabalho Prático 1 - Threads
**===========================================================================
*/

package com.feevale;

import java.util.Random;

public class Student extends Thread {
    // Tempo mínimo e máximo da chegada dos alunos até a parada de ônibus (milissegundo)
    private static final int TIME_GO_BUS_STOP_MIN = 30000;
    private static final int TIME_GO_BUS_STOP_MAX = 600000;
    // Nome do estudante
    private String studentName;
    // Sala de aula do aluno
    private String classRoom;
    // Tempo de aula de todas as salas
    private Integer classTime;
    // Indica se o aluno está na parada de ônibus
    private BusStop busStop;
    // Gerador de números aleatórios
    private static final Random random = new Random();

    public Student(String idStudent, String studentName, String classRoom, Integer classTime, BusStop busStop) {
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
            Thread.sleep(random.nextInt(TIME_GO_BUS_STOP_MAX - TIME_GO_BUS_STOP_MIN + 1) + TIME_GO_BUS_STOP_MIN);

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