package com.simple_queue;

public class Queue {
  private static int _index = 0;

  public int[] arrivalInterval;
  public int[] departureInterval;
  public int serverNumber;
  public int maxSize;
  public int index;
  public Queue destiny;
  public int lossNumber = 0;
  public int currentSize = 0;
  public Escalonador escalonador = Escalonador.getInstance();

  public Queue(int[] arrivalInterval, int[] departureInterval, int serverNumber, int maxSize, Queue destiny) {
    this.arrivalInterval = arrivalInterval;
    this.departureInterval = departureInterval;
    this.serverNumber = serverNumber;
    this.maxSize = maxSize;
    this.destiny = destiny;
    this.index = _index++;
  }

  public void chegada(boolean newEvent) throws Exception {
    this.escalonador.calculateTime(index, currentSize);
    if (this.maxSize < 0 || this.currentSize < this.maxSize) {
      this.currentSize++;
      if (this.currentSize <= this.serverNumber)
        this.escalonador.agendaSaida(this);
    } else {
      this.lossNumber++;
    }
    if (newEvent)
      this.escalonador.agendaChegada(this);
  }

  public void saida() throws Exception {
    this.escalonador.calculateTime(index, currentSize);
    this.currentSize--;
    if (this.currentSize >= this.serverNumber)
      this.escalonador.agendaSaida(this);
  }

  public void setDestiny(Queue destiny) {
    this.destiny = destiny;
  }

}