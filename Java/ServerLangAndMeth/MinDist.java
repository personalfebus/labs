import java.util.Scanner;

public class MinDist {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        char x = in.next().charAt(0), y = in.next().charAt(0);
        int boo = 0;
        int left = 0, right = 0;
        int sol = s.length();
        for (int i = 0; i < s.length(); i++){
            //System.out.println(s.charAt(i));
            if (s.charAt(i) == x){
                //System.out.println("got " + x);
                if ((boo == 0) || (boo == 1)) {
                    right = i;
                    boo = 1;
                }
                if (boo == 2){
                    right = i;
                    int tmp = Math.abs(right - left) - 1;
                    System.out.println("GOT EM " + left + " " + right + " => " + tmp);
                    if (tmp < sol) sol = tmp;
                    boo = 1;
                }
            }
            else if (s.charAt(i) == y){
                //System.out.println("got " + y);
                if ((boo == 0) || (boo == 2)) {
                    left = i;
                    boo = 2;
                }
                if (boo == 1){
                    left = i;
                    int tmp = Math.abs(right - left) - 1;
                    System.out.println("GOT EM " + left + " " + right + " => " + tmp);
                    if (tmp < sol) sol = tmp;
                    boo = 2;
                }
            }
        }

        System.out.println(sol);
    }
}