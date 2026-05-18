public class EnrolmentProcess extends Thread {

    String processId;
    int burstTime;
    int priority;
    private int remainingTime;

    public EnrolmentProcess(String id, int burstTime, int priority) {
        this.processId = id;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }

    public void run() {
        try {
            Thread.sleep(burstTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

}