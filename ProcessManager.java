import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.lang.Runnable;

public class ProcessManager implements Runnable{
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
        runningProcess = null; 
    }

    public void addProcess(Process process) {
        newQueue.offer(process);
    }

    public void run() {
        System.out.println("processManager started!!!");
        while (!stop) {

            while(newQueue.size() != 0){
                Process newProcess = newQueue.poll();
                readyQueue.offer(newProcess);
            }

            if (runningProcess == null && !readyQueue.isEmpty()) {
                runningProcess = readyQueue.poll();
                startTimer(); 
                System.out.println(runningProcess.getId());
                try {
                    Integer s = signal.take(); 
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

    private void moveToReadyQueue(Process process){
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

    public void stopScheduler(){
        stop = false;
    }

    public int getNumProcess(){
        return numProcesses;
    }

}