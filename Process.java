public class Process {

    private int id; // Process id
    private int pr; // Priority of the process 
    private int duration; // Execution of the process takes this amount of time
    private int arrivalTime; // The time when the process arrives at the system
    private int waitTime; // Keeps track of wait time once in queue

    public Process() {} 

    // Constructor
    public Process(int id, int pr,  int duration, int arrivalTime, int waitTime) {
        this.id = id;
        this.pr = pr;
        this.duration = duration;
        this.arrivalTime = arrivalTime;
        this.waitTime = waitTime;
    } 

    // Accessor methods
    public int getPR() { return pr; }
    public int getID() {return id; }
    public int getArrivalTime() {return arrivalTime; }
    public int getDuration() { return duration; }
    public double getWaitTime() { return waitTime; }

    // Update methods
    
    // Method sets wait time according to parameter
    // @param wait = the calculated wait time of process
    public void setWaitTime(int wait) { waitTime = wait; }

    // Method updates priority by decrementing it by 1
    public void updatePR() { pr = pr - 1; }

}

