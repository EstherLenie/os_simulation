import java.lang.Thread;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.Queue;
import java.util.LinkedList;

public class EventGenerator extends Thread
{ 
    public Semaphore semCon;
    public Semaphore semProd;
    private Queue<Integer> systemCallParameters; 
    private Queue<Integer> listOfParams = new LinkedList<>();
    Random random = new Random() ;

    public EventGenerator(Semaphore semCon, Semaphore semProd, Queue<Integer> parameters){
        this.systemCallParameters = parameters;
        this.semCon = semCon;
        this.semProd = semProd;
    }
    
    public void run (){    
        while(true){
            try {
                semProd.acquire();
                int EventId = random.nextInt(5); 
                listOfParams.offer(EventId);

                switch(EventId){
                    case 0:
                        requestProcessCreation();
                        break;
                    case 1:
                        requestStringPrinting();
                    break;
                    case 2:
                        requestFileCreation();
                        break;
                    case 3:
                      // request file creation
                        break;
                    case 4:
                        // request process id
                        break;
                    case 5:
                        System.out.println("Interruption ayant pour ID"+ EventId);
                        // simulation.put(this.EventId);
                        break;
                }

                while(listOfParams.size()>0){           
                    systemCallParameters.offer(listOfParams.poll()); 
                }         
            }catch(InterruptedException e){
                System.out.println("semaphore acquire failed");
            }finally{
                semCon.release();
            }
            try{
                Thread.sleep(1000);  
            }catch(InterruptedException e){
                
            }       
        }
    }

    private void requestProcessCreation(){
        generateParams(2);
    }

    private void requestStringPrinting(){
        int stringLength = random.nextInt(500);
        generateParams(stringLength);
    }

    private void requestFileCreation(){
        int fileContent = random.nextInt(500);
        generateParams(fileContent);
    }

    private void generateParams(int length){
        for (int i= 0; i<length; i++){
            listOfParams.offer(random.nextInt(256));
        }
    }
}
