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
    public final Semaphore semaphoreCon = new Semaphore(0);
    public final Semaphore semaphoreProd = new Semaphore(1);

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
                semaphoreCon.acquire();
                stopProcessScheduler();
                int systemCallId = sytemCallParametersList.poll();
                handleSystemCall(systemCallId);
                processManagerThread = new Thread(processManager, "processmanager");
                processManagerThread.start();
                semaphoreProd.release();

            }catch(InterruptedException e){

            }
        }
    }

    public ArrayBlockingQueue<Integer> getSistemCallSignal(){
        return systemCallSignal;
    }

    public Queue<Integer> getSystemCallParametersList(){
        return sytemCallParametersList;
    }

    public Semaphore getSemaphoreCon(){
        return semaphoreCon;
    }

    public Semaphore getSemaphoreProd(){
        return semaphoreProd;
    }

    private void handleSystemCall(int sistemCallId){
            switch(sistemCallId){
                case 0:
                    createNewProcess();
                case 1:
                    getHour();
            }

    }

    private void createNewProcess(){
        int PID = ++numProcessCreated;
        Process process = new Process(PID);

        int numFramesAllocated = memoryManager.allocateMemory(PID, process.getSize());
        if (numFramesAllocated == 0){
            System.out.println("not enough space to create process");
        }

        processManager.putOnNewQueue(process);
        System.out.println("process created. PID "+ PID);
    }

    private void getHour(){
        System.out.println("hello world");
    }

    private void stopProcessScheduler(){
        try{
            processManagerThread.interrupt();
        }catch(SecurityException e){
            System.exit(-1);
        }
    }
}