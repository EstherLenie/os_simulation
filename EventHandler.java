import java.lang.Runnable;

public class EventHandler implements Runnable{
    private Task[] tasks ;

    public EventHandler(int len){
        this.tasks = new Task[len];
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
                int systemCallId = OS.sytemCallParametersList.poll();
                handleSystemCall(systemCallId);
                OS.semaphoreProd.release();

            }catch(InterruptedException e){

            }
        }
    }

        private  void handleSystemCall(int sistemCallId){
            Task task = tasks[sistemCallId];
            task.execute();
            task.log();
    }
}
