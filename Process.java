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

public class OurProcess extends Process  {

    private  final int PID;
    private final int cycle_number;
    private final int Size;
  
    private static final int MIN_SIZE = 256; // Lowest possible memory size for a process
	private static final int MAX_SIZE = 1024; // Highest possible memory size for a process
    private static final int MAX_CYCLE = 4; // Max cycle pour accomplir le process
    private static final int MIN_CYCLE = 0; //MIN CYCLE pour accomplir le process

    public OurProcess(id PID, name string)
    {   
        this.PID = generateId();
        this.Size = generatedSize();
        this.cycle_number =generatedTime();
        
    
    }
    private int generateId ()
    {
        return Simulation.Nombre_Process++; // retourne le nombre de process cree lors de la simulation
    }
    private int generatedSize ()// generer aleatoirement la quantite d'espace RAM que le process aura besoin
    {
        Random random =new Random();
        int size = random.nextInt(MAX_SIZE-MIN_SIZE+1)+ MIN_SIZE;
        return size;
    }
    private int generatedTime () // generer le nombre de cycle necessaire pour finir le processus
    {
        Random random= new Random();
        int cycle_number= random.nextInt(MAX_CYCLE- MIN_CYCLE+1)+ MIN_CYCLE;
        return cycle_number;
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
        return this.cycle_number;
    }
}
