import java.lang.Thread;
import java.util.concurrent.Semaphore;
import javax.swing.SwingUtilities;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class OS {
    public final String version = "1.0.0";
    public static final Queue<Integer> sytemCallParametersList = new LinkedList<>(); 
    public static final Semaphore semaphoreCon = new Semaphore(0);
    public static final Semaphore semaphoreProd = new Semaphore(1);
    public static final Semaphore processSync = new Semaphore(1);
    public static CountDownLatch latch = new CountDownLatch(1);
    
    private static ArrayBlockingQueue<Integer> signal = new ArrayBlockingQueue<>(1);

    private static Timer timer;
    private volatile boolean newEvent = false;
    private static int quantum = 1000; 
    private TimerTask timerTask;

    public final ProcessManager processManager = new ProcessManager(processSync, quantum);
    public final EventHandler eventHandler = new EventHandler(6, newEvent);
    public final GUI window = new GUI();

    public final MemoryManager memoryManager;
    public final FileSystem fileSystem;
    public final  Machine machine;

    private final int osSize = 500000000;
    private final Instant bootTime;

    private  ExecutorService processManagerThread = Executors.newSingleThreadExecutor();

    private Thread eventHandlerThread = new Thread( eventHandler, "eventHandler");
    public static OS os;

    private OS(Machine machine){
        this.machine = machine;
        this.bootTime = Instant.now();
        memoryManager =  new MemoryManager(this.machine.memorySize, this.machine.frameSize, osSize);
        fileSystem = new FileSystem(new MassStorage(machine.memoryCapacity));
    
        setSysCallTable();

        SwingUtilities.invokeLater(() -> {
            SwingUtilities.invokeLater(() -> {
                window.createAndShowGUI();
                syslog(new Log(EventOutcome.SUCCESS, EventType.SYSTEM_BOOT, "system has started"));
                latch.countDown();
            });
        });
    }

   public static OS getInstance(Machine machine){
        if (os == null){
            os = new OS(machine);
        }

        return os;
   }
   
    public static Queue<Integer> getSystemCallParametersList(){
        return sytemCallParametersList;
    }

        private void startTimer() { 
        if (timer != null) { 
            timer.cancel(); 
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                processManager.run();
            }
        };
        timer = new Timer("processManagerTimer");
        timer.scheduleAtFixedRate(timerTask,0, quantum);
    }

    public static void restartScheduler(){
        os.processManagerThread = Executors.newSingleThreadExecutor();
    }

    private void handleTimerInterrupt() {
 
        try {
                signal.put(1);

        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    private Log createNewProcess(){
        int PID = processManager.getNumProcess() + 1;
        int processSize = sytemCallParametersList.poll();
        int processTime = sytemCallParametersList.poll();
        Process process = new Process(PID, processSize, processTime);

        int numFramesAllocated = memoryManager.allocateMemory(PID, process.getSize());
        if (numFramesAllocated == 0){
            return new Log(EventOutcome.ERROR, EventType.CREATE_PROCESS, "not enough space to create process");
        }

        processManager.putOnNewQueue(process);
        window.newProcess(PID, numFramesAllocated * machine.frameSize, ProcessState.New);
        return new Log(EventOutcome.SUCCESS, EventType.CREATE_PROCESS, "process created. PID "+ PID);
    }

    public static void terminateProcess(int processId){
        os.memoryManager.deallocateMemory(processId);
    }

    private static Log writeToConsole(){
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
        return new Log(EventOutcome.SUCCESS, EventType.PRINT, "print text onto screen");
    }

    private Log createFile(){
        int[] content = new int[sytemCallParametersList.size()-20];
        StringBuilder name = new StringBuilder();
        Integer param;
        int j =0;
        int i =0;

        while(true){
            param = sytemCallParametersList.poll();
            if (param == null){
                break;
            }
            if(j >= 20){
                content[i]=param.intValue();
                i++;
            } else {
                name.append(Character.toString(param));
            }
            j++;

        }
        File file = new File(name.toString(), content);
        Log log = fileSystem.storeFile(file);
        if (log.eventOutcome != EventOutcome.ERROR){
                window.newFile(file);
        }
        return log;
    }

    public static void syslog(Log log){
        os.window.addLog(log);
    }

    public static void getSystemInfos(){
        StringBuilder builder = new StringBuilder();
        builder.append("\n**********************************************'n");
        builder.append("System Infos\n");
        builder.append(String.format("Version: %s", os.version ));
        builder.append(String.format("Running time: %s", Instant.now().compareTo(os.bootTime)));
        builder.append(String.format("Installed memory %d\n", OS.getMemorySize()));
        builder.append(String.format("Used memory %d\n", OS.getUsedMemorySpace()));
        builder.append(String.format("Total storage %d\n", OS.getStorageSize()));
        builder.append(String.format("Used storage %d\n", OS.getUsedStorage()));
        builder.append("**********************************************\n");

        System.out.println(builder.toString());
    }

    private void setSysCallTable(){
        eventHandler.setTask(0, new Task() {
            public Log execute(){
                return createNewProcess();
            }
            public void log(Log log){
                syslog(log);
            }
        });

        eventHandler.setTask(1, new Task() {
            public Log execute(){
               return writeToConsole();
            }
            public void log(Log log){
                syslog(log);
            }
        });

         eventHandler.setTask(2, new Task() {
            public Log execute(){
                return createFile();
            }
            public void log(Log log){
                syslog(log);
            }
        });

        eventHandler.setTask(3, new Task() {
            public Log execute(){
                return processManager.killProcess();
            }
            public void log(Log log){
                syslog(log);
            }
        });

        eventHandler.setTask(4, new Task() {
            public Log execute(){
                Integer PID = processManager.getRunningProcess();
                if (PID == null){
                    return new Log(EventOutcome.ERROR, EventType.GET_PROCESS_ID, "no running process");
                }
                return new Log(EventOutcome.ERROR, EventType.KILL_PROCESS, "no running process");
            }
            public void log(Log log){
                syslog(log);
            }
        });

        eventHandler.setTask(5, new Task() {
            public Log execute(){
                OS.getSystemInfos();
                return new Log(EventOutcome.SUCCESS, EventType.SYSTEM_INFOS, "system infos printed");
            }
            public void log(Log log){
                syslog(log);
            }
        });
    }

    public static long getUsedMemorySpace(){
        return os.memoryManager.getUsedMemory();
    }

    public static long getUsedStorage(){
        return os.fileSystem.getUsedStorage();
    }

    public static void processUpdate(int PID, ProcessState processState){
        os.window.processUpdate(PID, processState);
    }

    public static long getStorageSize(){
        return os.fileSystem.getStorageSize();
    }

    public static long getMemorySize(){
        return os.memoryManager.getMemorySize();
    }

    public static int numFiles(){
        return os.fileSystem.numFiles();
    }

    public static int numLiveProcesses(){
        return os.processManager.numLiveProcesses();
    }

    public static int getNumProcess(){
        return os.processManager.getNumProcess();
    }

    public static List<MemoryPage> loadProcessFrames(int PID){
        return os.fileSystem.getProcessPages(PID);
    }

    public static boolean storePage(MemoryPage page){
        return os.fileSystem.storePage(page);
    }

    synchronized public static ProcessState getProcessState(int PID){
        return os.processManager.getProcessState(PID);
    }

    public static List<MemoryPage> getProcessPages(int PID){
        return os.fileSystem.getProcessPages(PID);
    }

    public static void start(){
        os.eventHandlerThread.start();
        os.startTimer();
    }

    public static void stopProcessScheduler(){
        os.processManagerThread.shutdownNow();
    }
}   