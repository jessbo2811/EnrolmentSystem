import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;

public class RoundRobinScheduler extends Thread {

    private static final int QUANTUM = 500;
    private static final String COMMA_DELIMITER = ",";

    private LinkedList<EnrolmentProcess> queue = new LinkedList<EnrolmentProcess>();

    public RoundRobinScheduler() {
        // empty
    }

    public static void main(String[] args) {
        try {
            RoundRobinScheduler scheduler = new RoundRobinScheduler();

            BufferedReader br = new BufferedReader(new FileReader("enrol.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("process_id")) continue;

                String[] values = line.split(COMMA_DELIMITER);
                String processId = values[0].trim();
                int burstTime = Integer.parseInt(values[1].trim());
                int priority = Integer.parseInt(values[2].trim());

                EnrolmentProcess process = new EnrolmentProcess(processId, burstTime, priority);
                scheduler.enqueue(process);
            }
            br.close();

            scheduler.startEnrolment();

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

        LinkedList<EnrolmentProcess> completed = new LinkedList<EnrolmentProcess>();

        while (!queue.isEmpty()) {

            EnrolmentProcess current = dequeue();

            if (current.getRemainingTime() <= 0) {
                completed.addLast(current);
                continue;
            }

            if (current.getState() == Thread.State.NEW) {
                current.start();
            } else if (current.getState() == Thread.State.TERMINATED) {
            } else {
                current.interrupt();
            }

            try {
                Thread.sleep(QUANTUM);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            int newRemainingTime = current.getRemainingTime() - QUANTUM;
            current.setRemainingTime(newRemainingTime);

            if (current.getRemainingTime() <= 0) {
                completed.addLast(current);
                System.out.println("ID: " + current.processId);
                System.out.println("Time Taken: " + (current.burstTime));
                System.out.println("Process Completed!");
            } else {
                enqueue(current);
            }
        }

        System.out.println("All processes completed.");
    }
}