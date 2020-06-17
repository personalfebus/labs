import java.util.Arrays;

public class Test {
    public static void main(String[] args){
//        int n = Integer.parseInt(args[0]);
        Sentance[] arr = new Sentance[]{
                new Sentance("2S1 22F8 "),
                new Sentance(" 43 55 2 1"),
                new Sentance(" 43 55 2F 1"),
                new Sentance("33"),
                new Sentance("33f")
        };

        //int kk = arr[0].parseDec();
        //System.out.println(kk);
//        Sentance[] arr = new Sentance[n];
//        for (int i = 0; i < n; i++){
//            arr[i] = new Sentance(args[i + 1]);
//        }

        Arrays.sort(arr);
        for (int i = 0; i < 5; i++){
            System.out.println(arr[i].toString());
        }
    }
}
