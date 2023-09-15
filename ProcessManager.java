import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.lang.Runnable;

public class ProcessManager implements Runnable{
    private HashMap<Integer, Process> processes;
    private Queue<Process> newQueue;    
    private Queue<Process> readyQueue;   
    //private Queue<Process> waitingQueue;  
    private Queue<Process> terminatedQueue;

    ArrayBlockingQueue<Integer> signal = new ArrayBlockingQueue<>(1);

    private TimerTask timerTask;

    private Process runningProcess; 
    private int quantum = 8000; 
    private Timer timer; 
    private boolean stop = false;
    private int numProcesses;

    public ProcessManager() {
        newQueue = new LinkedList<>();
        readyQueue = new LinkedList<>();
       // waitingQueue = new LinkedList<>();
        terminatedQueue = new LinkedList<>();
        processes = new HashMap<>();
        runningProcess = null; 
    }

    public void addProcess(Process process) {
        process.setState(ProcessState.New);
        newQueue.offer(process);
    }

    public void run() {
        OS.log("processManager started!!!");
        while (!stop) {

            while(newQueue.size() != 0){
                Process newProcess = newQueue.poll();
                newProcess.setState(ProcessState.Ready);
                processes.put(newProcess.getPID(), newProcess);
                readyQueue.offer(newProcess);
            }

            if (runningProcess == null && !readyQueue.isEmpty()) {
                Process process = readyQueue.poll();
                startProcess(process); 
                System.out.println(runningProcess.getId());
                try {
                    signal.take(); 
                    runningProcess.decreaseExecTime(2);
                    if (runningProcess.isFinished()) {
                        moveToTerminatedQueue(runningProcess);
                        runningProcess = null;
                        numProcesses--;
                        continue;
                    }
                    moveToReadyQueue(runningProcess);
                    runningProcess=null;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        }

        if (timer != null) {
            try{
                timer.wait();
            }catch (InterruptedException e){

            }
            runningProcess = null;
        }
    }

    private void startProcess(Process process) {
        OS.memoryManager.loadProcessFrames(process.getPID());
        runningProcess = readyQueue.poll();
        startTimer(); 
    }

    private void handleTimerInterrupt() {
        timer.cancel();
            try {
                    signal.put(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
    }

    private void startTimer() { 
        if (timer != null) { 
            timer.cancel(); 
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handleTimerInterrupt();
            }
        };
        timer = new Timer("processManagerTimer");
        timer.schedule(timerTask, quantum);
    }

    public ProcessState getProcessState(int PID){
        Process process = processes.get(PID);
        if (process == null)
            return null;
        
        return process.getState();
    }

    private void moveToReadyQueue(Process process){
        process.setState(ProcessState.Ready);
        readyQueue.offer(process);
    }

    private void moveToTerminatedQueue(Process process){
        terminatedQueue.offer(process);
    }

    public void putOnNewQueue(Process process){
        newQueue.offer(process);
        numProcesses++;
    }

    // private void moveToWaitingQueue(Process process){
    //     waitingQueue.offer(process);
    // }

    public void addNewProcess(Process newProcess){
        newQueue.offer(newProcess);
    }

    public Integer getRunningProcess(){
        if (runningProcess == null){
            return null;
        }

        return runningProcess.getPID();
    }

    public void killProcess(){
        if (runningProcess == null){
            return ;
        }
        moveToTerminatedQueue(runningProcess);
        runningProcess = null;
    }

    public void stopScheduler(){
        stop = false;
    }

    public int getNumProcess(){
        return numProcesses;
    }

}