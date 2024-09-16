import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.TimerTask;

public class ProcessManager {
    private HashMap<Integer, Process> processes;
    private Queue<Process> newQueue;    
    private Queue<Process> readyQueue;   
    //private Queue<Process> waitingQueue;  
    private Queue<Process> terminatedQueue;


    private Process runningProcess; 
    private int quantum; 
    private Semaphore sync;

    public ProcessManager(Semaphore sync, int quantum) {
        newQueue = new LinkedList<>();
        readyQueue = new LinkedList<>();
       // waitingQueue = new LinkedList<>();
        terminatedQueue = new LinkedList<>();
        processes = new HashMap<>();
        runningProcess = null; 
        this.quantum = quantum;
        this.sync = sync;

    }

    public int numLiveProcesses(){
        int total = readyQueue.size() + newQueue.size();
        return runningProcess != null? total + 1 : total;
    }

    public void run() {
        try  {
            sync.acquire();
            while(newQueue.size() != 0){
                Process newProcess = newQueue.poll();
                newProcess.setState(ProcessState.Ready);
                processes.put(newProcess.getPID(), newProcess);
                readyQueue.offer(newProcess);
                OS.processUpdate(newProcess.getPID(), ProcessState.Ready);
            }

            if (runningProcess == null && !readyQueue.isEmpty()) {
                Process process = readyQueue.poll();
                startProcess(process); 
            } else if (runningProcess != null){
                runningProcess.decreaseExecTime(quantum);
                if (runningProcess.isFinished()) {
                    moveToTerminatedQueue(runningProcess);
                    OS.processUpdate(runningProcess.getPID(), ProcessState.Terminated);
                    runningProcess = null;
                }else{                    
                    moveToReadyQueue(runningProcess);
                    OS.processUpdate(runningProcess.getPID(), ProcessState.Ready);
                    runningProcess=null;
                }
                
            }

        }catch(InterruptedException e){
        }finally{
            sync.release();
        }     
}



    private void startProcess(Process process) {
        OS.loadProcessFrames(process.getPID());
        runningProcess = process;
        OS.processUpdate(runningProcess.getPID(), ProcessState.Running);
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
        process.setState(ProcessState.New);
        newQueue.offer(process);
        processes.put(process.getPID(), process);
    }

    // private void moveToWaitingQueue(Process process){
    //     waitingQueue.offer(process);
    // }

    public Integer getRunningProcess(){
        if (runningProcess == null){
            return null;
        }

        return runningProcess.getPID();
    }

    public Log killProcess(){
        if (runningProcess == null){
            return new Log(EventOutcome.ERROR, EventType.KILL_PROCESS, "no running process");
        }
        moveToTerminatedQueue(runningProcess);
        OS.processUpdate(runningProcess.getPID(), ProcessState.Terminated);
        runningProcess = null;
        return new Log(EventOutcome.ERROR, EventType.KILL_PROCESS, "no running process");
    }

    public int getNumProcess(){
        return processes.size();
    }

}