import java.lang.Thread;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

public class Simulation{
    public static void main(String[] args){
        OS os = new OS (new Machine());
        Thread osThread = new Thread(os);
        osThread.start();

        Semaphore semaphoreCon = os.getSemaphoreCon();
        Semaphore semaphoreProd = os.getSemaphoreProd();
        Queue sistemCallParametersList = os.getSystemCallParametersList();
        ArrayBlockingQueue sistemCallSignal = os.getSistemCallSignal();

        Thread eventGenerator = new Thread(new EventGenerator(semaphoreCon, semaphoreProd, sistemCallParametersList, sistemCallSignal));
        eventGenerator.start();
    }
}