import java.lang.Thread;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

import org.w3c.dom.events.Event;

public class Simulation{
    public static void main(String[] args){
        OS os = new OS(new Machine());
        Thread osThread = new Thread(os, "os");
        osThread.start();

        Semaphore semaphoreCon = os.getSemaphoreCon();
        Semaphore semaphoreProd = os.getSemaphoreProd();
        Queue<Integer> sistemCallParametersList = os.getSystemCallParametersList();
        EventGenerator eventGenerator = new EventGenerator(semaphoreCon, semaphoreProd, sistemCallParametersList);

        Thread eventGeneratorThread = new Thread(eventGenerator, "eventGenerator");
        eventGeneratorThread.start();
    }
}