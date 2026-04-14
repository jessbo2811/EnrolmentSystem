import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class RoundRobinScheduler extends Thread { 

Queue<EnrolmentProcess> queue = new LinkedList<EnrolmentProcess>(); // creating the queue linked list of enrolment processes

public enqueue(EnrolmentProcess x) {

    queue.addFirst(x);

}

public dequeue(EnrolmentProcess x, LinkedList y) {

    y.addLast(x);
    y.remove()

}

    public startEnrolment() { // function to start the enrolment processes

        LinkedList<EnrolmentProcess> completed = new LinkedList<EnrolmentProcess>(); // creating the linked list of completed processes called 'completed'

        Thread.state currentState = getState(queue[0]); // making the current state a variable to shorten the code I have to write within the if statements

        if (currentState == 'NEW') {

            queue[0].run();
            
            dequeue(queue[0], queue);

        }

        else if (currentState == 'TERMINATED') {

            dequeue(queue[0], completed);
            
        }

        else {

            interrupt(queue[0]);

            queue[0].run();

            dequeue(queue[0], queue);

        }

        if (queue.isEmpty()) {

           // completed.printList(); implement later


    }
}
}