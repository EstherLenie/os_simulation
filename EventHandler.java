import java.lang.Runnable;

public class EventHandler implements Runnable{
    private Task[] tasks ;
    private volatile boolean newEvent;

    public EventHandler(int len, boolean newEvent){
        this.tasks = new Task[len];
        this.newEvent = newEvent;
    }

    public void setTask(int index, Task task){
        this.tasks[index]=task;
    }

    public void executeTask(int index){
        tasks[index].execute();
    }

    public void run(){
        while (true){
            try{
                OS.semaphoreCon.acquire();
                OS.stopProcessScheduler();
               OS.processSync.acquire();
                int systemCallId = OS.sytemCallParametersList.poll();
                handleSystemCall(systemCallId);
                OS.semaphoreProd.release();
                OS.restartScheduler();
                OS.processSync.release();; 

            }catch(InterruptedException e){
                System.out.println("afefew");
            }
        }
    }

        private  void handleSystemCall(int sistemCallId){
            Task task = tasks[sistemCallId];
            Log log = task.execute();
            task.log(log);
    }
}
