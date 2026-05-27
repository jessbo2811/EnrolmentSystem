import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class PriorityScheduler extends Thread {

    private static final int QUANTUM = 500;
    private static final String COMMA_DELIMITER = ",";

    private PriorityQueue<EnrolmentProcess> queue;

    public PriorityScheduler() {
        Comparator<EnrolmentProcess> c = (pr1, pr2) -> {
            if (pr1.getProcessPriority() < pr2.getProcessPriority()) return -1;
            if (pr1.getProcessPriority() > pr2.getProcessPriority()) return 1;
            return -1;
        };
        queue = new PriorityQueue<>(c);
    }

    public static void main(String[] args) {
        try {
            PriorityScheduler scheduler = new PriorityScheduler();

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
        queue.add(process);
    }

    public EnrolmentProcess dequeue() {
        return queue.poll();
    }

    public LinkedList<EnrolmentProcess> startEnrolment() {
        LinkedList<EnrolmentProcess> completed = new LinkedList<>();

        while (!queue.isEmpty()) {
            EnrolmentProcess current = queue.poll();

            Thread.State state = current.getState();

            if (state == Thread.State.NEW) {
                current.start();
                current.setStartTime(System.currentTimeMillis());
            } else if (state == Thread.State.TERMINATED) {
                long endTime = System.currentTimeMillis();
                System.out.println("ID: " + current.processId);
                System.out.println("Status: COMPLETE");
                System.out.println("Burst Time: " + current.burstTime);
                System.out.println("Time Taken: " + (endTime - current.startTime));
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

            queue.add(current);
        }

        System.out.println("All processes completed.");
        return completed;
    }
}