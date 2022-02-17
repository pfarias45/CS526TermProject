import java.util.Comparator;

// Method implements user defined comparator for array list (D) based on arrival time
public class ProcessComparatorD implements Comparator<Process>{
    @Override
    public int compare(Process p1, Process p2) {
        if(p1.getArrivalTime() > p2.getArrivalTime()) return 1;
        else if (p1.getArrivalTime() < p2.getArrivalTime()) return -1;
        return 0;
    }
    
}
