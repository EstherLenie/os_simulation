import java.lang.Runnable;
import java.lang.Thread;

public class OS implements Runnable{
    private final ProcessManager processManager = new ProcessManager();
    private final MemoryManager memoryManager = new MemoryManager();

    private Thread processManagerThread = new Thread(processManager, "processManager");

    public void run(){
        processManagerThread.start();
    }
}