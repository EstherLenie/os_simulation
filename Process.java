import java.lang.*;
import java.lang.Comparable;
import java.util.Random;

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
