import java.lang.Runnable;
import java.lang.Thread;
import java.util.concurrent.Semaphore;
import java.util.Queue;
import java.util.LinkedList;

public class OS implements Runnable{
    private static final ProcessManager processManager = new ProcessManager();
    public static final Queue<Integer> sytemCallParametersList = new LinkedList<>(); 
    public static final Semaphore semaphoreCon = new Semaphore(0);
    public static final Semaphore semaphoreProd = new Semaphore(1);

    private static MemoryManager memoryManager;
    private Machine machine;

    private int osSize = 10000;

    public OS(Machine machine){
        this.machine = machine;
        memoryManager =  new MemoryManager(this.machine.memorySize, this.machine.frameSize, osSize);
    }

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

    public static Queue<Integer> getSystemCallParametersList(){
        return sytemCallParametersList;
    }

    public static Semaphore getSemaphoreCon(){
        return semaphoreCon;
    }

    public static Semaphore getSemaphoreProd(){
        return semaphoreProd;
    }

    private  void handleSystemCall(int sistemCallId){
            switch(sistemCallId){
                case 0:
                    createNewProcess();
                case 1:
                    getHour();
            }

    }

    private void createNewProcess(){
        int PID = processManager.getNumProcess() + 1;
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
        processManager.stopScheduler();
    }

    public static void terminateProcess(int processId){
        memoryManager.deallocateMemory(processId);
    }
}