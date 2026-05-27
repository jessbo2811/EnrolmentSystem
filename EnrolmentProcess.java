public class EnrolmentProcess extends Thread {

    String processId;
    int burstTime;
    int priority;
    private int remainingTime;
    long startTime = 0;

    public EnrolmentProcess(String id, int burstTime, int priority) {
        this.processId = id;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }

    public void run() {
        int remaining = burstTime;
        while (remaining > 0) {
            try {
                Thread.sleep(20);
                remaining -= 20;
            } catch (InterruptedException e) {
            }
        }
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getProcessPriority() {
        return this.priority;
    }

}