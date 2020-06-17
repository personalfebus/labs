import java.math.BigInteger;
import java.util.Scanner;

public class FastFib {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        Matrix Mat = new Matrix(BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ZERO);
        int num = in.nextInt();
        System.out.println(Mat.getFib(num - 1));
    }
}

class Matrix{
    private BigInteger[][] A;

    public Matrix(BigInteger a11, BigInteger a12, BigInteger a21, BigInteger a22){
        this.A = new BigInteger[2][2];
        this.A[0][0] = a11;
        this.A[0][1] = a12;
        this.A[1][0] = a21;
        this.A[1][1] = a22;
    }

    public Matrix getPower(int p){
        if (p == 1) return this;
        Matrix K = this.getPower(p / 2);
        return this.getMulti(K, K);
    }

    public Matrix getMulti(Matrix M1, Matrix M2){
        BigInteger a11 = M1.A[0][0].multiply(M2.A[0][0]).add(M1.A[0][1].multiply(M2.A[1][0]));
        BigInteger a12 = M1.A[0][0].multiply(M2.A[0][1]).add(M1.A[0][1].multiply(M2.A[1][1]));
        BigInteger a21 = M1.A[1][0].multiply(M2.A[0][0]).add(M1.A[1][1].multiply(M2.A[1][0]));
        BigInteger a22 = M1.A[1][0].multiply(M2.A[0][1]).add(M1.A[1][1].multiply(M2.A[1][1]));
        return new Matrix(a11, a12, a21, a22);
    }

    public BigInteger getFib(int n){
        if (n == 0) return BigInteger.ONE;
        if (n == 1) return BigInteger.ONE;

        int MAXSIZE = 19;
        int[] arrP = new int[MAXSIZE];
        int count = 0;
        for (int i = 0; i < MAXSIZE; i++) {
            int tmp = (1 << i) & n;
            if (tmp != 0) {
                arrP[count] = tmp;
                //System.out.println(arrP[count] + " " + count + " " + i);
                count++;
            }
        }

        Matrix E = new Matrix(BigInteger.ONE, BigInteger.ZERO, BigInteger.ONE, BigInteger.ZERO);
        for (int i = 0; i < MAXSIZE; i++){
            if (arrP[i] != 0){
                E = this.getMulti(E, this.getPower(arrP[i]));
            }
        }
        return E.A[0][0];
    }
}
