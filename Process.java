import java.util.Random;

public class Process {

    private  final int PID;
    private final int cycleNumber;
    private final int Size;
    private boolean isTemporary;
    private int cycleLeft;
    private ProcessState status;

    public Process(int PID, int size, int cycleNumber){   
        this.PID = PID;
        this.Size = size;
        this.cycleNumber = cycleNumber;
        this.cycleLeft = cycleNumber;
        this.isTemporary = true;
    }

    int getId()
    {
        return this.PID;
    }
    
    int getSize()
    {
        return this.Size;
    }
    int getCycleNumber()
    {
        return this.cycleNumber;
    }

      public int getPID(){
        return this.PID;
    }

    public void decreaseExecTime(int time){
        if (!this.isTemporary){
            return;
        }
        this.cycleLeft -= time;
    }

    public boolean isFinished(){
        return this.cycleLeft <= 0;
    }

    public void setState(ProcessState state){
        this.status = state;
    }

    public ProcessState getState(){
        return status;
    }

}

    

  