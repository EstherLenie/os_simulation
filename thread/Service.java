import java.lang.Runnable;
import java.lang.Thread;

public class Service implements Runnable{
    public int a = 13;

    public void seta(int value){
        a=value;
    }
  
    public void run(){
        while(true){
            System.out.println(a);
            try{
                Thread.sleep(5000);
            }
            catch(InterruptedException e){

            }
        }
    }
}