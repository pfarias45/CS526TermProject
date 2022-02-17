import java.io.File;
import java.io.PrintWriter; 
import java.util.*;

public class ProcessScheduling {

    // Method calculates the total wait time of all process removed from queue
    // @param waitTimeArr = arr that keeps track of wait times of each project that is removed from queue 
    // returns total wait time
    public static int totalWaitTime (ArrayList<Integer> waitTimeArr) {
        int totalWaitTime = 0;
        for(int x: waitTimeArr) {
           totalWaitTime += x;
        }
        return totalWaitTime;
    }

    // Method prints the output to text file when a process is removed from queue
    // @param p = process that's removed
    // @param startTime = time at which it is remmoved 
    // @param waitTimeArr = arr that keeps track of wait times of each project that is removed from queue 
    // returns the formatted output
    public static String printRemoval(Process p, int startTime, ArrayList<Integer> waitTimeArr) {
        // add wait time total up to the time in which the print occurs
        double totalWaitTime = totalWaitTime(waitTimeArr);
        String indentedOutput = 
            "Priority = " + p.getPR() + "\n" +
            "Arrival = " + p.getArrivalTime() + "\n" +
            "Duration = " + p.getDuration() + "\n";
        String output = 
            "Process removed from queue is: id = "  + p.getID() +
            ", at time " + startTime +
            ", wait time = " + p.getWaitTime() +
            ", total wait time = " + totalWaitTime +"\n" +
            "Process id = " + p.getID() + "\n" +
            // indent this part
            indentedOutput.indent(5);
        return output;
    }

    // Method prints the output to text file when a process finishes execution
    // @param p = process that's removed
    // @param endTime = time at which execution is finished
    // returns the formatted output
    public static String printFinish(Process p, int endTime) {
        String output = 
            "Process " + p.getID() +
            " finished at time " + endTime + "\n" + "\n" +
            "Update priority: ";
        return output;
    }

    // Method prints the output to text file when a process has their priority updated
    // @param p = process that's being updated
    // returns the formatted output
    public static String printPriority(Process p) {
        String output = 
            "PID = " + p.getID() +
            ", wait time = " + p.getWaitTime() +
            ", current priority = " + (p.getPR() + 1) + "\n" +
            "PID = " + p.getID() +
            ", new priority = " + p.getPR() + "\n" ;
        return output;
    }

    // Method updates priority of process waiting longer or equal to maxTime
    // @param Q = priority queue with processes
    // @param maxTime = maximum wait time
    // @param outputFile = file to print output to
    public static void updatePriority(PriorityQueue<Process> Q, double maxTime, int endTime, PrintWriter outputFile) {
        ArrayList<Process> temporaryArr = new ArrayList<>();
        for (Process process : Q) {
            updateWait(process, endTime);
            if(process.getWaitTime() >= maxTime) {
                process.updatePR();
                // Write to output file
                outputFile.println(printPriority(process)); 
                temporaryArr.add(process);
            }
        }

        // Use temp array of processes to remove and re-add processes that have had priority updated
        // in order to re-sort them in Q since priority queues don't re-sort automatically
        for (Process p: temporaryArr) {
            Q.remove(p);
            Q.add(p);
        }
    }

    // Method updates wait time of processes in the queue
    // @param p = process to update
    // @param time = time of the update
    // return wait time
    public static int updateWait(Process p, int time) {
        int waitTime = time - p.getArrivalTime();
        p.setWaitTime(waitTime);
        return waitTime;
    }  

    public static void main(String[] args) throws Exception {
  
        // Pass the path to the file that will be written to
        PrintWriter outputFile = new PrintWriter("process_scheduling_output.txt");

        // Create empty Array List (D) for process objects
        ArrayList<Process> D = new ArrayList<Process>();

        // Pass the path to the file that will be read from
        File inputFile = new File("process_scheduling_input.txt");
        Scanner scanner = new Scanner(inputFile);
    
        // While file has next line
        while (scanner.hasNextLine()) {
            // Get integers that correspond to each part of process
            int id = scanner.nextInt(); 
            int pr = scanner.nextInt();
            int duration = scanner.nextInt();
            int arrivalTime = scanner.nextInt(); 
            int waitTime = 0;
            // Create new process object
            Process process = new Process(id, pr, duration, arrivalTime, waitTime); 

            // Add process to Array List(D)
            D.add(process);
        }
        scanner.close();

        // Sort Array List(D)
        Collections.sort(D, new ProcessComparatorD());

        // Print all processes in p
        for(Process p: D) {
            outputFile.println(
                "Id = " + p.getID() +
                ", priority = " + p.getPR() +
                ", duration = " + p.getDuration() +
                ", arrival time = " + p.getArrivalTime());
        }

        Process pRunning = new Process(); // Instance of process 

        double maxTime =  30.0; // Max wait time

        // Print max time
        outputFile.println("\n" + "Maximum wait time = " + maxTime + "\n");

        // Create empty Priority Queue (Q) and sort it according to priority using a comparator
        PriorityQueue<Process> Q = new PriorityQueue<>(new ProcessComparatorQ());

        int currentTime = 0; // Current time of simulation, 
        boolean running = false; // Indicates whether the system is currently executing a process or not. 
        ArrayList<Integer> waitTimeArr = new ArrayList<>();  // Array to keep track of wait times and calculate avg wait time at end
        int startTime = 0; // Time when process is removed from queue
        int endTime = 0; // Time when process execution finishes

        // While loop runs once for every time unit until D is empty 
        while(D.size() != 0) {
            // Get a process p from D that has the earliest arrival time, this would be at index 0
            Process p = D.get(0);
            // If p is less than or equal to current time
            if(p.getArrivalTime() <= currentTime) {
                // Remove p from D and insert process with earliest arrival time into Q
                D.remove(p);
                Q.add(p);
            }

            // If Q is not empty and the flag running is false
            if(Q.size() != 0 && running == false) {
                // Remove a process with the smallest priority from Q
                // Given the comparator sorting, it is the head of the queue
                pRunning = Q.poll();
                // Set start time to current time to mark when process is removed from queue
                startTime = currentTime;
                // Calculate the wait time of the process
                int waitTime = updateWait(pRunning, startTime);
                // Set a flag running to true 
                running = true;
                // Add wait time to array
                waitTimeArr.add(waitTime);
                // Write to output file
                outputFile.println(printRemoval(pRunning, startTime, waitTimeArr)); 
            }

            // Calculate time process finishes execution
            endTime = startTime + pRunning.getDuration();
            
            // If currently running process has finished 
            if(currentTime !=0 && currentTime == endTime) {
                // Set a flag running to false
                running = false;
                // Write to output file
                outputFile.println(printFinish(pRunning, endTime));
                // Update priorities of processes that have been waiting longer than max. wait time 
                updatePriority(Q, maxTime, endTime, outputFile);
                // Set current time to time process finished
                currentTime = endTime;
            }
            else {
                // If no process has finished, increment currentTime
                currentTime++;
            }
 
        }

        // Print when D gets empty
        outputFile.println("D becomes empty at time " + (currentTime - 1) + "\n");

        // Reset for the next loop
        currentTime = endTime;
        running = false;

        updatePriority(Q, maxTime, currentTime, outputFile);
        
        // While Q is not empty
        while(Q.size() != 0) {
            // If the flag running is false
            if(running == false) {
                // Remove a process with the smallest priority from Q
                // Given the comparator sorting, it is the head of the queue
                pRunning = Q.poll();
                // Set start time to current time to mark when process is removed from queue
                startTime = currentTime;
                // Calculate the wait time of the process
                int waitTime = updateWait(pRunning, startTime);
                // Set a flag running to true 
                running = true;
                // Add wait time to array
                waitTimeArr.add(waitTime);
                // Write to output file
                outputFile.println(printRemoval(pRunning, startTime, waitTimeArr)); 
            }

            // Calculate time process finishes execution
            endTime = startTime + pRunning.getDuration();

            // If currently running process has finished 
            if(currentTime == endTime) {
                // Set a flag running to false
                running = false;
                // Write to output file
                outputFile.println(printFinish(pRunning, endTime));
                // Update priorities of processes that have been waiting longer than max. wait time 
                updatePriority(Q, maxTime, endTime, outputFile);
                // Set current time to time process finished
                currentTime = endTime;
            }
            else {
                // If no process has finished, increment currentTime
                currentTime++;
            }
  
        }

        // Caclulate total and avg waits
        double finalWaitTime = totalWaitTime(waitTimeArr);
        double avgWaitTime = finalWaitTime / waitTimeArr.size();

        // Write to output file
        outputFile.println(
            "Total wait time = " + finalWaitTime + "\n" +
            "Average wait time = " + avgWaitTime);

        outputFile.close();
    }
}   


