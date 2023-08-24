import java.util.Random;

public class EventGenerator
{
   public static void main(String[] args)
    {
        int EventId ;
        Random random = new Random() ;

        EventId = random.nextInt(6);
        System.out.println(EventId);
    }
    
}
