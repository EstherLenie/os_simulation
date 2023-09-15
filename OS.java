import java.lang.Thread;
import java.util.concurrent.Semaphore;

import javax.swing.SwingUtilities;

import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

public class OS {
    public final String version = "1.0.0";
    public static final Queue<Integer> sytemCallParametersList = new LinkedList<>(); 
    public static final Semaphore semaphoreCon = new Semaphore(0);
    public static final Semaphore semaphoreProd = new Semaphore(1);

    public static final ProcessManager processManager = new ProcessManager();
    public static final EventHandler eventHandler = new EventHandler(5);
    public static final GUI window = new GUI();

    public static CountDownLatch latch = new CountDownLatch(1);
    public static MemoryManager memoryManager;
    public static FileSystem fileSystem;

    public final  Machine machine;

    private int osSize = 10000;

    private Thread processManagerThread = new Thread(processManager, "processManager");
    private Thread eventHandlerThread = new Thread( eventHandler, "eventHandler");

    public OS(Machine machine){
        this.machine = machine;
        memoryManager =  new MemoryManager(this.machine.memorySize, this.machine.frameSize, osSize);
        fileSystem = new FileSystem(new MassStorage(machine.memoryCapacity));

        
        setSysCallTable();

        SwingUtilities.invokeLater(() -> {
            SwingUtilities.invokeLater(() -> {
                window.createAndShowGUI();
                latch.countDown();
            });
        });

        processManagerThread.start();
        eventHandlerThread.start();
    }

    public static Queue<Integer> getSystemCallParametersList(){
        return sytemCallParametersList;
    }

    private void createNewProcess(){
        int PID = processManager.getNumProcess() + 1;
        int processSize = sytemCallParametersList.poll();
        int processTime = sytemCallParametersList.poll();
        Process process = new Process(PID, processSize, processTime);

        int numFramesAllocated = memoryManager.allocateMemory(PID, process.getSize());
        if (numFramesAllocated == 0){
            System.out.println("not enough space to create process");
        }

        processManager.putOnNewQueue(process);
        window.newProcess(PID, numFramesAllocated * machine.frameSize, "new");
        System.out.println("process created. PID "+ PID);
    }

    public static void stopProcessScheduler(){
        processManager.stopScheduler();
    }

    public static void terminateProcess(int processId){
        memoryManager.deallocateMemory(processId);
    }

    private static void writeToConsole(){
        Integer param;
        StringBuilder builder = new StringBuilder();
        while(true){
            param = sytemCallParametersList.poll();
            if (param == null){
                break;
            }
            builder.append(Character.toString(param));
        }
        System.out.println(builder.toString());
    }

    private void createFile(){
        int[] content = new int[sytemCallParametersList.size()];
        Integer param;
        int i=0;

        while(true){
            param = sytemCallParametersList.poll();
            if (param == null){
                break;
            }
            content[i]=param.intValue();
        }
        fileSystem.createFile(content);
    }

    public static void log(String log){
        window.addLog(log);
    }

    private void setSysCallTable(){
        eventHandler.setTask(0, new Task() {
            public void execute(){
                createNewProcess();
            }
            public void log(){
                window.addLog("create new Process");
            }
        });

        eventHandler.setTask(1, new Task() {
            public void execute(){
                writeToConsole();
            }
            public void log(){
                window.addLog("write to console");
            }
        });

         eventHandler.setTask(2, new Task() {
            public void execute(){
                createFile();
            }
            public void log(){
                window.addLog("create new file");
            }
        });

        eventHandler.setTask(3, new Task() {
            public void execute(){
                processManager.killProcess();
            }
            public void log(){
                window.addLog("kill process");
            }
        });

        eventHandler.setTask(4, new Task() {
            public void execute(){
                System.out.println("running process :"+processManager.getRunningProcess());
            }
            public void log(){
                window.addLog("get running process");
            }
        });
    }
}