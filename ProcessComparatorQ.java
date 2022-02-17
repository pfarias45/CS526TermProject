import java.util.Comparator;

// Method implements user defined comparator for queue (Q) based on priority
public class ProcessComparatorQ implements Comparator<Process>{
    @Override
    public int compare(Process p1, Process p2) {
        if(p1.getPR() > p2.getPR()) return 1;
        else if (p1.getPR() < p2.getPR()) return -1;
        return 0;
    }
    
}
