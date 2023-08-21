import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class ProcessManager {
    private Queue<Process> newQueue;    
    private Queue<Process> readyQueue;   
    private Queue<Process> waitingQueue;  
    private Queue<Process> terminatedQueue;

    private Queue<Task> priorityTasks;
    private Queue<Task> nonPriorityTasks;

    private Process runningProcess; 
    private int quantum = 4; 
    private int cycle = 0;

    private Timer timer; 
    private TimerTask timerTask; 

    public ProcessManager() {
        newQueue = new LinkedList<>();
        readyQueue = new LinkedList<>();
        waitingQueue = new LinkedList<>();
        terminatedQueue = new LinkedList<>();
        priorityTask = new LinkedList<>();
        nonPriorityTask = new LinkedList<>();
        runningProcess = null; 
        
      
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handleTimerInterrupt();
            }
        };
    }

    public void addProcess(Process process) {
        newQueue.offer(process);
    }

    public void runProcesses() {
        while (1) {

            if (!priorityTask.isEmpty()){
                executePriotaryTasks();
            }

            if (runningProcess != null && runningProcess.isFinished()) {
                moveToTerminatedQueue(runningProcess);
                runningProcess = null;
            }

            if (runningProcess == null && !readyQueue.isEmpty()) {
                runningProcess = readyQueue.poll();
                cycle++;
                startTimer(); 
            }

            if (runningProcess != null) {
                runningProcess.executeOneTimeUnit();
            }

            if (cycle == 0){
                executeNonPriotaryTask();
            }

        }

        if (timer != null) {
            timer.cancel(); // Arrête la minuterie à la fin de la simulation
        }
    }

    private void handleTimerInterrupt() {
        if (runningProcess != null) {
            readyQueue.offer(runningProcess);
            runningProcess = null;
            if(cycle) > readyQueue.Size(){
                cycle = 0;
            }
        }
    }

    private void startTimer() { 
        if (timer != null) { 
            timer.cancel(); 
        }

        timer.schedule(timerTask, quantum);
    }

    private void executePriotaryTask(){
        Task task = null;
        Process process = null;
        while(!priorityTasks.isEmpty()){
            task = priorityTask.poll();
            task.execute();
            process = waitingQueue.poll();
            readyQueue.offer(process);
        }
    }

    private void putProcessOnReadyQueue(Process process){
        readyQueue.offer(process);
    }

    public void addTask(Task task, boolean priority){
        if(priority){
            priorityTasks.offer(task);
            return 
        }

        nonPriorityTasks.offer(task);
    }

    private void executeNonPriotaryTask(){
        Task task = null;
        int processId = 0;
        while(!nonPriorityTask.isEmpty()){
            task = nonPriorityTask.poll();
            task.execute();

            processId = task.getProcessId();

        }
    }

}