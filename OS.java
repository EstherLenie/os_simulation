import java.lang.Runnable;
import java.lang.Thread;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.Queue;
import java.util.LinkedList;

public class OS implements Runnable{
    private final ProcessManager processManager = new ProcessManager();
    public final Queue<Integer> sytemCallParametersList = new LinkedList<>(); 
    public  final ArrayBlockingQueue<Integer> systemCallSignal = new ArrayBlockingQueue<>(1);
    public final Semaphore semaphore = new Semaphore(1);

    private final MemoryManager memoryManager;
    private Machine machine;

    private int osSize = 10000;

    public OS(Machine machine){
        this.machine = machine;
        this.memoryManager =  new MemoryManager(machine.memorySize, machine.frameSize, osSize);
    }

    private int numProcessCreated = 0;

    private Thread processManagerThread = new Thread(processManager, "processManager");

    public void run(){
        processManagerThread.start();

        while (true){
            try{
                int signal = systemCallSignal.take();

            }catch(InterruptedException e){

            }
        }
    }

    public ArrayBlockingQueue getSistemCallSignal(){
        return systemCallSignal;
    }

    public Queue getSystemCallParametersList(){
        return sytemCallParametersList;
    }

    public Semaphore getSemaphore(){
        return semaphore;
    }

    private void handleSystemCall(int sistemCallId){
        semaphore.acquire();
        
        switch(sistemCallId){
            case 0:
                createNewProcess();
        }

        semaphore.release();
    }

    private void createNewProcess(){
        int PID = numProcessCreated++;
        Process process = new Process(PID);

        int numFramesAllocated = memoryManager.allocateMemory(PID, process.getSize());
        if (numFramesAllocated == 0){

        }

        processManager.putOnNewQueue(process);
    }


}