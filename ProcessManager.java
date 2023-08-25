import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.lang.Runnable;

public class ProcessManager implements Runnable{
    private Queue<Process> newQueue;    
    private Queue<Process> readyQueue;   
    private Queue<Process> waitingQueue;  
    private Queue<Process> terminatedQueue;

    ArrayBlockingQueue<Integer> signal = new ArrayBlockingQueue<>(1);

    private TimerTask timerTask;

    private Process runningProcess; 
    private int quantum = 4; 
    private Timer timer; 
    private boolean stop = false;

    public ProcessManager() {
        newQueue = new LinkedList<>();
        readyQueue = new LinkedList<>();
        waitingQueue = new LinkedList<>();
        terminatedQueue = new LinkedList<>();
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

    public void run() {
        System.out.println("processManager started!!!");
        while (!stop) {

            while(newQueue.size() != 0){
                Process newProcess = newQueue.poll();
                readyQueue.offer(newProcess);
            }

            if (runningProcess != null && runningProcess.isFinished()) {
                moveToTerminatedQueue(runningProcess);
                runningProcess = null;
            }

            if (runningProcess == null && !readyQueue.isEmpty()) {
                runningProcess = readyQueue.poll();
                startTimer(); 

                try {
                    int valeur = signal.take(); 
                    moveToReadyQueue(runningProcess);
                    runningProcess=null;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        }

        if (timer != null) {
            timer.cancel();
        }
    }

    private void handleTimerInterrupt() {
        if (runningProcess != null) {
            runningProcess.decreaseExecTime(quantum);
            try {
                    signal.put(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
        }
    }

    private void startTimer() { 
        if (timer != null) { 
            timer.cancel(); 
        }

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
    }

    private void moveToWaitingQueue(Process process){
        waitingQueue.offer(process);
    }

    public void addNewProcess(Process newProcess){
        newQueue.offer(newProcess);
    }

    public void stopScheduler(){
        stop = false;
    }

}