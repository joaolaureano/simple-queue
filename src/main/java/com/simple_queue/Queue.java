public class Queue {
    public int currentSize = 0;
    public int maxSize;//K
    public int[] arrivalInterval;//may be null
    public int[] departureInterval;
    public int serverNumber;//C

    Queue(int maxSize, int[] arrivalInterval, int[] departureInterval, int serverNumber) {
        //only the first queue will have arrival time
        this.maxSize = maxSize;
        this.arrivalInterval = arrivalInterval;
        this.departureInterval = departureInterval;
        this.serverNumber = serverNumber;
    }

    Queue(int maxSize, int[] departureInterval, int serverNumber) {
        //every other queue will have only departureInterval
        this.maxSize = maxSize;
        this.departureInterval = departureInterval;
        this.serverNumber = serverNumber;
    }
}