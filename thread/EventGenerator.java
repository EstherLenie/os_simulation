
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.Queue;

public class EventGenerator extends Thread
{ 
    public Semaphore semaphore;
    //private String interruption;
    //private String threadName;
    private  Queue<Integer> systemCallParameters ; 
    public ArrayBlockingQueue<Integer> systemCallSignal;

    public EventGenerator(Semaphore semaphore, ArrayBlockingQueue<Integer> sistemCallSignal)
    {
        this.systemCallSignal = sistemCallSignal;
        //this.systemCallParameters = parameters;
        this.semaphore = semaphore;
    }
    
    public  void run ()
    {    
        Random random = new Random() ;
        int EventId = random.nextInt(1);
       

        try
         {
        semaphore.acquire();
         }
        catch (InterruptedException e) {
        System.out.println("InterruptedException caught");}

        systemCallParameters.offer(EventId);

        switch(EventId)
        {
            case 0:
                int param = random.nextInt(3);
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
