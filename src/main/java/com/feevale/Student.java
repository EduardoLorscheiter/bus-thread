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

import java.time.LocalTime;

public class Student extends Thread {
    private String studentName;
    private LocalTime arrivalTimeBusStop;
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

    public LocalTime getArrivalTimeBusStop() {
        return arrivalTimeBusStop;
    }

    @Override
    public void run() {
        try {
            // Tempo do aluno na aula
            Thread.sleep(classTime);

            // Tempo que o aluno leva até o ponto de ónibus (aleatório)
            Thread.sleep((int) (Math.random() * 100));

            // Chegada do aluno no ponto de ônibus
            this.arrivalTimeBusStop = LocalTime.now();
            busStop.arrive(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}