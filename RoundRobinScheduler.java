import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class RoundRobinScheduler extends Thread { 

    LinkedList<EnrolmentProcess> queue = new LinkedList<EnrolmentProcess>();

    public RoundRobinScheduler() {
        // empty
    }

    public static void main(String[] args) {
        try {
            RoundRobinScheduler scheduler = new RoundRobinScheduler();
            // add processes, start the scheduler etc.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enqueue(EnrolmentProcess x) {
        queue.addFirst(x);
    }

    public void dequeue(EnrolmentProcess x, LinkedList y) {
        y.addLast(x);
        y.remove();
    }

    public void startEnrolment() {

        LinkedList<EnrolmentProcess> completed = new LinkedList<EnrolmentProcess>();

        Thread.State currentState = queue.peek().getState();

        if (currentState == Thread.State.NEW) {
            queue.peek().run();
            dequeue(queue.peek(), queue);
        }
        else if (currentState == Thread.State.TERMINATED) {
            dequeue(queue.peek(), completed);
        }
        else {
            queue.peek().interrupt();
            queue.peek().run();
            dequeue(queue.peek(), queue);
        }

        if (queue.isEmpty()) {
            // completed.printList(); implement later
        }
    }
}