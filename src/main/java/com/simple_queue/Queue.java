package com.simple_queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.simple_queue.NumberGenerator.NumberGenerator;

public class Queue {
  private static int _index = 0;
  public int index = _index++;

  public Escalonador escalonador = Escalonador.getInstance();

  public double[] arrivalInterval;
  public double[] departureInterval;

  public int serverNumber;
  public int maxSize;
  public int lossNumber = 0;
  public int currentSize = 0;

  public Map<Integer, Double> probabilities = new HashMap<Integer, Double>();

  public ArrayList<QueueProbability> destines = new ArrayList<>();

  public Queue(double[] arrivalInterval, double[] departureInterval, int serverNumber, int maxSize) {
    this.arrivalInterval = arrivalInterval;
    this.departureInterval = departureInterval;
    this.serverNumber = serverNumber;
    this.maxSize = maxSize;
    this.index = _index++;
  }

  public void chegada(boolean newEvent) throws Exception {
    Clock.getInstance().calculateTime();
    if (this.maxSize < 0 || this.currentSize < this.maxSize) {
      this.currentSize++;
      if (this.currentSize <= this.serverNumber) {
        Escalonador r = this.escalonador;
        r.agendaSaida(this, getDestiny(), NumberGenerator.getInstance().getNextSeed());
      }
    } else {
      this.lossNumber++;
    }
    if (newEvent) {
      Escalonador r = this.escalonador;
      r.agendaChegada(this, NumberGenerator.getInstance().getNextSeed());
    }
  }

  public void saida() throws Exception {
    Clock.getInstance().calculateTime();
    this.currentSize--;
    if (this.currentSize >= this.serverNumber) {
      Escalonador r = this.escalonador;
      r.agendaSaida(this, getDestiny(), NumberGenerator.getInstance().getNextSeed());
    }
  }

  public void addDestiny(Queue destiny, double probability) {
    this.destines.add(new QueueProbability(destiny, probability));
    Collections.sort(this.destines);
  }

  public Queue getDestiny() {
    if (this.destines.isEmpty())
      return null;
    if (this.destines.size() == 1 && this.destines.get(0).probability >= 1)
      return this.destines.get(0).queue;
    double indice_tmp = NumberGenerator.getInstance().getNextSeed();
    for (QueueProbability queueProbability : this.destines) {
      if (indice_tmp <= queueProbability.probability)
        return queueProbability.queue;
      indice_tmp -= queueProbability.probability;
    }

    return null;
  }

  public static class QueueProbability implements Comparable<QueueProbability> {
    public Queue queue;

    public double probability;

    public QueueProbability(Queue queue, double probability) {
      this.queue = queue;
      this.probability = probability;
    }

    public int compareTo(QueueProbability queue) {
      return Double.compare(this.probability, queue.probability);
    }

    public boolean equals(Object o) {
      if (o == this)
        return true;
      if (!(o instanceof QueueProbability))
        return false;
      QueueProbability other = (QueueProbability) o;
      return this.queue.index == other.queue.index && this.probability == other.probability;
    }

  }
}