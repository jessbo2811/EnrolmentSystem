import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;

public class MLFQScheduler extends Thread {

    private static final int QUANTUM = 500;
    private static final String COMMA_DELIMITER = ",";

    private LinkedList<EnrolmentProcess> youngList = new LinkedList<EnrolmentProcess>();
    private LinkedList<EnrolmentProcess> oldList = new LinkedList<EnrolmentProcess>();

    public MLFQScheduler() {
        // empty
    }

    public static void main(String[] args) {
        try {
            MLFQScheduler scheduler = new MLFQScheduler();

            BufferedReader br = new BufferedReader(new FileReader("enrol.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("process_id")) continue;

                String[] values = line.split(COMMA_DELIMITER);
                String processId = values[0].trim();
                int burstTime = Integer.parseInt(values[1].trim());
                int priority = Integer.parseInt(values[2].trim());

                EnrolmentProcess process = new EnrolmentProcess(processId, burstTime, priority);
                scheduler.enqueueYoung(process);
            }
            br.close();

            scheduler.startEnrolment();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enqueueYoung(EnrolmentProcess process) {
        youngList.addLast(process);
    }    
    public void enqueueOld(EnrolmentProcess process) {
        oldList.addLast(process);
    }

    public EnrolmentProcess dequeueYoung() {
        return youngList.removeFirst();
    }
    public EnrolmentProcess dequeueOld() {
        return oldList.removeFirst();
    }

    public void startEnrolment() {

        LinkedList<EnrolmentProcess> completed = new LinkedList<EnrolmentProcess>();

        while (!youngList.isEmpty() && !oldList.isEmpty()) {
            if (!youngList.isEmpty()){
                EnrolmentProcess current = dequeueYoung();

                if (current.getRemainingTime() <= 0) {
                    completed.addLast(current);
                    continue;
                }

                if (current.getState() == Thread.State.NEW) {
                    current.start();
                    current.setStartTime(System.currentTimeMillis());
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
                    long endTime = System.currentTimeMillis();
                    System.out.println("ID: " + current.processId);
                    System.out.println("Burst Time: " + current.burstTime);
                    System.out.println("Time Taken: " + (endTime - current.startTime));
                    System.out.println("Process Completed!");
                } else {
                    enqueueOld(current);
                }
            }
            else{
                EnrolmentProcess currentOld = dequeueOld();

                if (currentOld.getRemainingTime() <= 0) {
                    completed.addLast(currentOld);
                    continue;
                }

                if (currentOld.getState() == Thread.State.NEW) {
                    currentOld.start();
                    currentOld.setStartTime(System.currentTimeMillis());
                } else if (current.getState() == Thread.State.TERMINATED) {
                } else {
                    currentOld.interrupt();
                }

                try {
                    Thread.sleep(QUANTUM);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                int newRemainingTime = currentOld.getRemainingTime() - QUANTUM;
                currentOld.setRemainingTime(newRemainingTime);

                if (currentOld.getRemainingTime() <= 0) {
                    completed.addLast(currentOld);
                    long endTime = System.currentTimeMillis();
                    System.out.println("ID: " + currentOld.processId);
                    System.out.println("Burst Time: " + currentOld.burstTime);
                    System.out.println("Time Taken: " + (endTime - currentOld.startTime));
                    System.out.println("Process Completed!");
                } else {
                    enqueueYoung(currentOld);
                }
            }
        }


        System.out.println("All processes completed.");
    }
}