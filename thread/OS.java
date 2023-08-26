import java.util.concurrent.Semaphore;
import java.util.concurrent.ArrayBlockingQueue;


class OS extends Thread
{
    public Semaphore semaphore ;
    private int s;
    public ArrayBlockingQueue<Integer> systemCallSignal;
    public OS (Semaphore semaphore, ArrayBlockingQueue<Integer> sistemCallSignal )
    {
        
        this.semaphore = semaphore;
        this.systemCallSignal = sistemCallSignal;
    }
   public void run ()
    {
        try 
        {
        semaphore.acquire();
        }
        catch (InterruptedException e) {
            System.out.println("InterruptedException caught");
        }
        try
        {
       System.out.println("Le parametre est :" +systemCallSignal.poll() );
        } catch (InterruptedException  e) {
           System.out.println("InterruptedException caught");
        }
    }
}

