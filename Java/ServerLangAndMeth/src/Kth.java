import java.util.Scanner;

public class Kth {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long k = in.nextLong();

        long c = 0, a = 9, b = 1;
        while (k >= (c + a * b)){
            c += a * b;
            a *= 10;
            b++;
        }
        //System.out.println(a + " " + b + " " + c);
        //return;
        long start = a / 9;
        long step;
        if (b == 1) step = 1;
        else step = start / 10;
        //System.out.println(start + " " + step + " " + b);
        for (long i = start; i < k + 2; i += step){
            if (c == k){
                //System.out.println(i);
                System.out.println(i / start);
                break;
            }
            if (c + step * b > k){
                if (step == 1){
                    //i += step;
                    long gg = c + b - k;
                    //System.out.println(c + " " + k + " " + " " + i + " " + gg);
                    for (long j = 0; j < b - (k - c) - 1; j++){
                        i /= 10;
                    }
                    System.out.println(i % 10);
                    break;
                }
                else {
                    //System.out.println("Step decreased: " + step + " -> " + step / 10);
                    step /= 10;
                    i -= step;
                    continue;
                }
            }
            c += step * b;
        }
        in.close();
    }
}