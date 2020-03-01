public interface QueueHandle {
    void addLineToQueue(String line) throws InterruptedException;

}
