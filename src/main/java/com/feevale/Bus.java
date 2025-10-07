/*
**===========================================================================
**  @file    Bus.java
**  @author  Eduardo Lorscheiter e Loreno Enrique Ribeiro
**  @class   Processamento Paralelo
**  @date    Outubro/2025
**  @version 1.0
**  @brief   Trabalho Prático 1 - Threads
**===========================================================================
*/

package com.feevale;

public class Bus extends Thread {
    // Capacidade máxima do ônibus
    private static final int CAPACITY = 50;
    // Identificador único do ônibus
    private final int idBus;
    // Parada de ônibus da Universidade Feevale
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
        // Indica a chegada do ônibus no ponto de ônibus
        busStop.board(this);
    }
}
