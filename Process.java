public class Process{
    private final int id;
    private final String name;
    private int execTime;
    private boolean temporary;

    public Process(int id, String name, int execTime){
        this.id=id;
        this.name=name;
        this.execTime=execTime;
        this.temporary = true;
    }

    public Process(int id, String name){
        this.id=id;
        this.name=name;
        this.execTime=10000;
        this.temporary = false;
    }

    public int getPID(){
        return this.id;
    }

    public void decreaseExecTime(int time){
        if (!this.temporary){
            return;
        }
        this.execTime -= time;
    }

    public boolean isFinished(){
        return this.execTime <= 0;
    }
}