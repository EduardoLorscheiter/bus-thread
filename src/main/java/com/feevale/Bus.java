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
    private final int idBus;
    private final BusStop busStop;
    private final int capacity = 50;

    public Bus(int idBus, BusStop busStop) {
        this.idBus = idBus;
        this.busStop = busStop;
    }

    public int getIdBus() {
        return idBus;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public void run() {
        System.out.println("Ônibus " + idBus + " chegou no ponto.");
        busStop.embarcar(this);
    }
}
