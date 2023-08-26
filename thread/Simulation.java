import java.util.concurrent.Semaphore;
//import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;


public class Simulation {
     public static  Semaphore semaphore= new Semaphore(1);
   // private  Queue<Integer> Parameters; 
    public static ArrayBlockingQueue<Integer> systemCallSignal;
    public static void main(String[] args)
    {
       
        
        systemCallSignal = new ArrayBlockingQueue<Integer> (4);
        Thread OS = new Thread(new OS ( semaphore, systemCallSignal ));
        Thread Generator =  new Thread(new OS ( semaphore, systemCallSignal ));
        Generator.start();
        OS.start();

    }
}
