import java.lang.Thread;

public class Simulation{
    public static void main(String[] args){
        Thread os = new Thread(new OS ());
        os.start();
    }
}