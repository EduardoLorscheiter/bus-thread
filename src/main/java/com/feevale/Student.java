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
    private String classRoom;
    private Integer classTime;
    private BusStop busStop;

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

    public String getStudentIdentification(){
        return studentName + " (" + classRoom + ")";
    }

    @Override
    public void run() {
        try {
            // Aguarda o tempo do aluno na aula
            Thread.sleep(classTime);

            // Aguarda o tempo que o aluno leva até o ponto de ônibus (aleatório)
            Thread.sleep((int) (Math.random() * TIME_GO_BUS_STOP));

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