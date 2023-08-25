import java.lang.Thread;
import java.util.Scanner;

public class User{
    public static void main(String[] args){
        Service s = new Service();
        Thread t = new Thread(s);

        t.start();

        Scanner scanner = new Scanner(System.in);

        int a = scanner.nextInt();
        s.seta(a);
    }
}