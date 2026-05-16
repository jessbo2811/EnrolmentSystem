import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class RoundRobinScheduler extends Thread { 

    private static final int QUANTUM = 500;

    LinkedList<EnrolmentProcess> queue = new LinkedList<EnrolmentProcess>();

    public RoundRobinScheduler() {
        // empty
    }

    public static void main(String[] args) {
        try {
            RoundRobinScheduler scheduler = new RoundRobinScheduler();
            // add processes, start the scheduler etc.

        // Reads the CSV
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enqueue(EnrolmentProcess process) {
        queue.addLast(process);
    }

    public EnrolmentProcess dequeue() {
        return queue.removeFirst();
    }


    public void startEnrolment() {

        LinkedList<EnrolmentProcess> completed = new LinkedList<EnrolmentProcess>(); // Makes a new linked list called completed

        while (!queue.isEmpty()) {

            EnrolmentProcess current = dequeue(); // removes the enrolment process from the front of the queue
            Thread.State currentState = current.getState();

            if (currentState == Thread.State.NEW) {
                current.start();
            } else if (currentState == Thread.State.TERMINATED) {
                completed.addLast(current);
                continue;
            } else {
                current.interrupt();
            }

            try {
                Thread.sleep(QUANTUM);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (current.getState() == Thread.State.TERMINATED) {
                completed.addLast(current);
            } else {
                enqueue(current); // puts it back to the end
            }
        }

    }
}