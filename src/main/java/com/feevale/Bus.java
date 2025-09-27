/*
**===========================================================================
**  @file    Bus.java
**  @author  Eduardo Lorscheiter e Loreno Enrique Ribeiro
**  @class   Processamento Paralelo
**  @date    Setembro/2025
**  @version 1.0
**  @brief   Trabalho Prático 1 - Threads
**===========================================================================
*/

package com.feevale;

public class Bus extends Thread {
    private static final int CAPACITY = 50;
    private final int idBus;
    private final BusStop busStop;

    public Bus(int idBus, BusStop busStop) {
        this.idBus = idBus;
        this.busStop = busStop;
    }

    public int getIdBus() {
        return idBus;
    }

    public int getCapacity() {
        return CAPACITY;
    }

    @Override
    public void run() {
        System.out.println("Ônibus " + idBus + " chegou no ponto.");
        busStop.board(this);
    }
}
