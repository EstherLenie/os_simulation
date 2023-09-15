import java.lang.Thread;

//import org.w3c.dom.events.Event;

public class Simulation{
    public static void main(String[] args){
        new OS(new Machine());

        try {
            OS.latch.await();
            EventGenerator eventGenerator = new EventGenerator(OS.semaphoreCon, OS.semaphoreProd, OS.sytemCallParametersList);
            Thread eventGeneratorThread = new Thread(eventGenerator, "eventGenerator");
            eventGeneratorThread.start();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}