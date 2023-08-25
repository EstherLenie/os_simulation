
import java.util.Random;
import java.util.concurrent.*;

public class EventGenerator extends Thread
{ 
    public Semaphore semaphore;
    private String interruption;
    private String threadName;
    private final int EventId;
    
   public  void run ()
    {    
        Random random = new Random() ;
       int EventId;
        interruption = new String();
        EventId = random.nextInt(6);
        switch(EventId)
        {
        case 0:
            System.out.println("Interruption ayant pour ID"+ EventId);
            simulation.put(this.EventId);
            break;
        case 1:
            System.out.println("Interruption ayant pour ID"+ EventId);
             simulation.put(this.EventId);
            break;
        case 2:
            System.out.println("Interruption ayant pour ID"+ EventId);
            simulation.put(this.EventId);
            break;
        case 3:
            System.out.println("Interruption ayant pour ID"+ EventId);
             simulation.put(this.EventId);
            break;
        case 4:
            System.out.println("Interruption ayant pour ID"+ EventId);
             simulation.put(this.EventId);
            break;
        case 5:
            System.out.println("Interruption ayant pour ID"+ EventId);
             simulation.put(this.EventId);
            break;
        }
        

    }
    
}
