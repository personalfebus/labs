import java.util.Arrays;
import java.util.Scanner;

public class Test3 {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
//        Variaty[] tst = new Variaty[in.nextInt()];
//        for (int i = 0; i < tst.length; i++) {
//            int n = in.nextInt();
//            String[] s = new String[n];
//            for (int j = 0; j < n; j++){
//                s[j] = in.nextLine();
//            }
//            tst[i] = new Variaty(s);
//        }
        Variaty[] arr = new Variaty[]{
                new Variaty(new String[]{
                        "abc1 23",
                        "abc1 23",
                        "abc1 23"
                }),
                new Variaty(new String[]{
                        "aA bB cC 444",
                        "aa bb cc 444",
                        "ab 646464",
                        "b 666 a",
                        "aqwertyb"
                })
        };
        Arrays.sort(arr);
        //Arrays.sort(tst);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
//        for (int i = 0; i < arr.length; i++) {
//            System.out.println(tst[i]);
//        }
    }
}
