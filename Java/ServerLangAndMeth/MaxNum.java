import java.util.Scanner;

public class MaxNum {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] arr = new int[n];

        for (int i = 0; i < n; i++){
            arr[i] = in.nextInt();
        }

        int count = 0;
        while (count < n){
            int maxSize = Helper.getSize(arr[0]);
            int max = Helper.getDigit(arr[0], maxSize);
            int maxi = 0;

            for (int i = 0; i < arr.length; i++){
                int currSize = Helper.getSize(arr[i]);
                int curr = Helper.getDigit(arr[i], currSize);
                if (curr > max){
                    //System.out.println("HEY " + curr + " > " + max);
                    maxi = i;
                    max = curr;
                    maxSize = currSize;
                }
                else if (curr == max){
                    long num1 = arr[i] * Helper.getSize(arr[maxi]) * 10 + arr[maxi];
                    long num2 = arr[maxi] * Helper.getSize(arr[i]) * 10 + arr[i];
                    if (num1 > num2){
                        //System.out.println("HEY " + curr + " == " + max);
                        maxSize = currSize;
                        max = curr;
                        maxi = i;
                    }
                }
            }
            System.out.print(arr[maxi]);
            arr[maxi] = -1;
            count++;
        }
    }
}

class Helper{
    public static int getSize(int a){
        int size = 1;
        while (a > 9) {
            a /= 10;
            size *= 10;
        }
        return size;
    }

    public static int getDigit(int a, int b){
        a /= b;
        a %= 10;
        return a;
    }
}