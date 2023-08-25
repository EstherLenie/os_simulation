
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.Queue;

public class EventGenerator extends Thread
{ 
    public Semaphore semaphore;
    private String interruption;
    private String threadName;
    private final int EventId;
    private Queue systemCallParameters; 
    public ArrayBlockingQueue<Integer> systemCallSignal;

    public EventGenerator(Semaphore semaphore, Queue parameters, ArrayBlockingQueue sistemCallSignal){
        this.systemCallSignal = sistemCallSignal;
        this.systemCallParameters = parameters;
        this.semaphore = semaphore;
    }
    
    public  void run ()
    {    
        Random random = new Random() ;
        int EventId = random.nextInt(1);
        interruption = new String();

        semaphore.acquire();
        systemCallParameters.offer(EventId);

        switch(EventId)
        {
            case 0:
                int param = random.nextInt();
                systemCallParameters.offer(param);
                System.out.println("Interruption ayant pour ID"+ EventId);

                try {
                    systemCallSignal.put(1); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
               // simulation.put(this.EventId);
                break;
        case 1:
            System.out.println("Interruption ayant pour ID"+ EventId);
          //  simulation.put(this.EventId);
            break;
        case 2:
            System.out.println("Interruption ayant pour ID"+ EventId);
         //   simulation.put(this.EventId);
            break;
        case 3:
            System.out.println("Interruption ayant pour ID"+ EventId);
          //   simulation.put(this.EventId);
            break;
        case 4:
            System.out.println("Interruption ayant pour ID"+ EventId);
           //  simulation.put(this.EventId);
            break;
        case 5:
            System.out.println("Interruption ayant pour ID"+ EventId);
            // simulation.put(this.EventId);
            break;
        }   
        semaphore.release();    
    }
 
    
}
