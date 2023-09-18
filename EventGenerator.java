import java.lang.Thread;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.Queue;
import java.util.LinkedList;

public class EventGenerator extends Thread
{ 
    public Semaphore semCon;
    public Semaphore semProd;
    private volatile Queue<Integer> systemCallParameters; 
    private Queue<Integer> listOfParams = new LinkedList<>();
    private static Random random = new Random();
    private static final int MIN_PROCESS_SIZE = 50000000;
    private static final int MAX_PROCESS_SIZE = 200000000;
    private static final int MIN_PROCESS_CPU_TIME = 400;
    private static final int MAX_PROCESS_CPU_TIME = 40000;


    public EventGenerator(Semaphore semCon, Semaphore semProd, Queue<Integer> parameters){
        this.systemCallParameters = parameters;
        this.semCon = semCon;
        this.semProd = semProd;
    }
    
    public void run (){    
        while(true){
            try {
                semProd.acquire();
                int EventId = random.nextInt(6); 
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
                        // request system infos
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
                Thread.sleep(4000);  
            }catch(InterruptedException e){
                
            }       
        }
    }

    public void generateRandomText(int maxLength) {
        int textLength = random.nextInt(maxLength) + 1; 

        for (int i = 0; i < textLength; i++) {
            int randomAscii = random.nextInt(94) + 33;
            listOfParams.offer(randomAscii);
        }
    }

    public void generateRandomText(int minLength, int maxLength) {
        int textLength = random.nextInt(maxLength) + minLength; 

        for (int i = 0; i < textLength; i++) {
            int randomAscii = random.nextInt(94) + 33;
            listOfParams.offer(randomAscii);
        }
    }

    private void requestProcessCreation(){
        listOfParams.offer(random.nextInt(MAX_PROCESS_SIZE - MIN_PROCESS_SIZE) + MIN_PROCESS_SIZE);//process size
        listOfParams.offer(random.nextInt(MAX_PROCESS_CPU_TIME - MIN_PROCESS_CPU_TIME) + MIN_PROCESS_CPU_TIME);//process size
    }

    private void requestStringPrinting(){
        int stringLength = random.nextInt(500);
        generateRandomText(stringLength, 500);
    }

    private void requestFileCreation(){
        int fileContent = random.nextInt(500);

        generateRandomFileName();
        generateRandomText(fileContent);
    }

    public void generateRandomFileName() {
        int textLength = random.nextInt(20); 

        for (int i = 0; i < textLength; i++) {
            int randomAscii = random.nextInt(26) + 97;
            listOfParams.offer(randomAscii);
        }

        for (int i = 0; i < 20 - textLength; i++ ){
            listOfParams.offer(32);
        }
    }
}
