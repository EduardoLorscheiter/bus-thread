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

public class Student extends Thread {
    private static final int TIME_GO_BUS_STOP = 100;
    private String studentName;
    private Integer classTime;
    private BusStop busStop;

    public Student(String idStudent, String studentName, Integer classTime, BusStop busStop) {
        super(idStudent);
        this.studentName = studentName;
        this.classTime = classTime;
        this.busStop = busStop;
    }

    public String getStudentName() {
        return studentName;
    }

    @Override
    public void run() {
        try {
            // Aguarda o tempo do aluno na aula
            Thread.sleep(classTime);

            // Aguarda o tempo que o aluno leva até o ponto de ônibus (aleatório)
            Thread.sleep((int) (Math.random() * TIME_GO_BUS_STOP));

            // Indica a chegada do aluno no ponto de ônibus
            busStop.arrive(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}