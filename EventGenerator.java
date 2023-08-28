import java.lang.Thread;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.Queue;
import java.util.LinkedList;

public class EventGenerator extends Thread
{ 
    public Semaphore semCon;
    public Semaphore semProd;
    private String interruption;
    private String threadName;
    private int EventId;
    private Queue<Integer> systemCallParameters; 
    private Queue<Integer> listOfParams = new LinkedList<>();

    public EventGenerator(Semaphore semCon, Semaphore semProd, Queue<Integer> parameters){
        this.systemCallParameters = parameters;
        this.semCon = semCon;
        this.semProd = semProd;
    }
    
    public  void run ()
    {    
        Random random = new Random() ;
        while(true){
            try {
                semProd.acquire();
                int EventId = random.nextInt(1); 
                listOfParams.offer(EventId);

                switch(EventId){
                    case 0:
                        listOfParams.offer(random.nextInt(100));
                        System.out.println("Interruption ayant pour ID"+ EventId);
                        break;
                    case 1:
                        listOfParams.offer(random.nextInt(400));
                        System.out.println("Interruption ayant pour ID"+ EventId);
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

                while(listOfParams.size()>0){           
                    systemCallParameters.offer(listOfParams.poll()); 

                Thread.sleep(1000);
                }         
            }catch(InterruptedException e){
                System.out.println("semaphore acquire failed");
            }finally{
                semCon.release();
            }
                             
        }
    }
}
