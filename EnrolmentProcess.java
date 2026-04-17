
public class EnrolmentProcess extends Thread {

    // Declaring the data types


    // Declaring the fields
    String id;
    int burstTime;
    int priority;
    Thread.State state; // you need both the definition and the variable that uses it

    // constructor (constructs a EnrolmentProcess and populates its fields) - needed to use a StudentEnrol
    public EnrolmentProcess(String id, int burstTime, int priority, Thread.State state)  { // parameters

        // I have to do this because when the constructor is called you need to assign the parameters to the fields
        // For example, the id parameter must be assigned to the id field
        this.id = id;
        this.burstTime = burstTime;
        this.priority = priority;
        this.state = state;

    
    }

    public void run() {
        // to simulate the work of the student enrolment process, I have been asked to simply put the Thread to sleep for the specified burst time
        try {
            Thread.sleep(burstTime);            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

}


