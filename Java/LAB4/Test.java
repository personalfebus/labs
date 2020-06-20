import java.util.Scanner;

public class Test{
    public static void main(String[] args){
        Integer[] arr = new Integer[]{1, 1, 2, 3, 3, 5, 8, 9, 10};
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] mas = new int[n];
        for (int i = 0; i < n; i++){
            mas[i] = in.nextInt();
        }

        FibSeq A = new FibSeq(arr);
        System.out.println("new Arr: " + A.toString(arr));

        for (Integer[] integers : A) {
            System.out.println(A.toString(integers));
        }

        arr = new Integer[]{1, 1, 1, 2, 3, 5, 8, 9, 10};

        A.setArr(arr);
        System.out.println("new Arr: " + A.toString(arr));

        for (Integer[] integers : A) {
            System.out.println(A.toString(integers));
        }
    }
}
