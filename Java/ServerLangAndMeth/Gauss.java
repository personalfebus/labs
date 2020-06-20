import java.util.Scanner;

public class Gauss {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        MatrixGauss Mat = new MatrixGauss(n, n);
        MatrixGauss Bat = new MatrixGauss(n, 1);
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n + 1; j++){
                if (j == n) Bat.addElem(i, 0, in.nextInt(), 1);
                else Mat.addElem(i, j, in.nextInt(), 1);
            }
        }
        Mat.makeStepped(Bat);
        //System.out.println(Mat.toString());
        //System.out.println(Bat.toString());
        if (Mat.fillNumbers(Bat)) System.out.println("No solution");
        else {
            for (int j = 0; j < n; j++){
                System.out.print(Mat.getNom(j, j) + "/" + Mat.getDen(j, j) + "\n");
            }
        }
    }
}

class MatrixGauss{
    private int[][][] arr;

    public MatrixGauss(int i, int j){
        this.arr = new int[i][j][2];
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        s.ensureCapacity(25);
        for (int i = 0; i < this.arr.length; i++){
            for (int j = 0; j < this.arr[0].length; j++){
                if (j == this.arr[0].length - 1) {
                    //if (this.arr[i][j][0] == 0) s.append("0\n");
                    //if (this.arr[i][j][1] == 1) s.append(this.arr[i][j][0] + "\n");
                    s.append(this.arr[i][j][0] + "/" + this.arr[i][j][1] + "\n");
                }
                else {
                    //if (this.arr[i][j][0] == 0) s.append("0 ");
                    //if (this.arr[i][j][1] == 1) s.append(this.arr[i][j][0] + " ");
                    s.append(this.arr[i][j][0] + "/" + this.arr[i][j][1] + " ");
                }
            }
        }
        return s.toString();
    }

    public int getNom(int i, int j){
        if (this.arr[i][j][1] < 0){
            this.arr[i][j][1] *= -1;
            this.arr[i][j][0] *= -1;
        }
        return this.arr[i][j][0];
    }

    public int getDen(int i, int j){
        return this.arr[i][j][1];
    }

    public void addElem(int i, int j, int nominator, int denominator){
        this.arr[i][j][0] = nominator;
        this.arr[i][j][1] = denominator;
    }

    public boolean checkForZeroLines(){
        boolean wh = false;
        for (int i = 0; i < this.arr.length; i++){
            for (int j = 0; j < this.arr[0].length; j++){
                wh = true;
                if (this.arr[i][j][0] != 0) {
                    //System.out.println(this.arr[i][j][0] + " != 0");
                    wh = false;
                    break;
                }
            }
            //System.out.println(wh);
            if (wh) break;
        }
        //System.out.println(wh);
//        for (int j = 0; j < this.arr[0].length; j++){
//            int[] summ = new int[2];
//            summ[0] = 0;
//            summ[1] = 1;
//            for (int i = 0; i < this.arr.length; i++) {
//                summ = this.subElems(summ, this.arr[i][j], 1, 1);
//            }
//            if (summ[0] == 0){
//                wh = true;
//                break;
//            }
//        }
        for (int i = 0; i < this.arr[0].length; i++) {
            if (this.arr[i][i][0] == 0) wh = true;
        }
        return wh;
    }

    private void swapLines(int i, int ii, MatrixGauss B){
        for (int j = 0; j < this.arr[0].length; j++){
            int tmp0 = this.arr[i][j][0];
            int tmp1 = this.arr[i][j][1];
            this.arr[i][j][0] = this.arr[ii][j][0];
            this.arr[i][j][1] = this.arr[ii][j][1];
            this.arr[ii][j][0] = tmp0;
            this.arr[ii][j][1] = tmp1;
        }
        int tmp0 = B.arr[i][0][0];
        int tmp1 = B.arr[i][0][1];
        B.arr[i][0][0] = B.arr[ii][0][0];
        B.arr[i][0][1] = B.arr[ii][0][1];
        B.arr[ii][0][0] = tmp0;
        B.arr[ii][0][1] = tmp1;
    }

    private int nod(int a, int b) {
        while (b != 0) {
            int tmp = a % b;
            a = b;
            b = tmp;
        }
        return a;
    }

    private void reduceFR(int i, int jj){
        if ((this.arr[i][jj][1] != 0) && (this.arr[i][jj][0] != 0)){
            if ((this.arr[i][jj][1] < 0) && (this.arr[i][jj][0] < 0)){
                this.arr[i][jj][1] = -1 * this.arr[i][jj][1];
                this.arr[i][jj][0] = -1 * this.arr[i][jj][0];
            }
            int k = this.nod(this.arr[i][jj][0], this.arr[i][jj][1]);
            this.arr[i][jj][0] /= k;
            this.arr[i][jj][1] /= k;
        }
        else if (this.arr[i][jj][0] == 0) this.arr[i][jj][1] = 1;
    }

    private void checkForZero(int i, int j, MatrixGauss B){
        if (this.arr[i][j][0] == 0){
            for (int ii = i + 1; ii < this.arr.length; ii++){
                if (this.arr[ii][j][0] != 0){
                    swapLines(i, ii, B);
                    break;
                }
            }
        }
    }

    private int[] subElems(int[] a, int[] b, int cde, int cnu){
        int den1 = a[1];
        int den2 = b[1] * cde;
        int nom1 = a[0];
        int nom2 = b[0] * cnu;
        //System.out.println(a[0] + "/" + a[1] + " " + b[0] + "/" + b[1] + " " + cde + " " + cnu);
        int nok = den1 * den2 / this.nod(den1, den2);
        nom1 = nom1 * nok / den1;
        nom2 = nom2 * nok / den2;
        nom1 -= nom2;
        a[0] = nom1;
        a[1] = nok;
        return a;
    }

    public void makeStepped(MatrixGauss B){
        for (int j = 0; j < this.arr[1].length - 1; j++){
            this.checkForZero(j, j, B);
            for (int i = j + 1; i < this.arr.length; i++){
//                this.checkForZero(i, j, B);
                if (this.arr[i][j][0] != 0) {
                    int cnu = (this.arr[i][j][0] * this.arr[j][j][1]);
                    int cde = (this.arr[i][j][1] * this.arr[j][j][0]);
                    for (int jj = j; jj < this.arr[1].length; jj++) {
                        if ((cde == 0) || (cnu == 0)) continue;
                        this.arr[i][jj] = this.subElems(this.arr[i][jj], this.arr[j][jj], cde, cnu);
                        this.reduceFR(i, jj);
                    }
                    B.arr[i][0] = this.subElems(B.arr[i][0], B.arr[j][0], cde, cnu);
                    B.reduceFR(i, 0);
                }
            }
            //System.out.println(this.toString());
        }
    }

    public boolean fillNumbers(MatrixGauss B){
        //System.out.println(this.toString());
        if (this.checkForZeroLines()){
            return true;
        }
        for (int j = this.arr[0].length - 1; j >= 0; j--){
            int prevNom = this.arr[j][j][0];
            int prevDen = this.arr[j][j][1];
            if (prevNom != 0) {
                int[] newND = new int[2];
                newND[0] = B.getNom(j, 0);
                newND[1] = B.getDen(j, 0);
                for (int jj = j + 1; jj < this.arr[0].length; jj++) {
                    newND = this.subElems(newND, this.arr[j][jj], 1, 1);
                }
                if (j == 2) {
                    //System.out.println(newND[0] + "/" + newND[1]);
                }
                this.arr[j][j][0] = prevDen * newND[0];
                this.arr[j][j][1] = prevNom * newND[1];
                reduceFR(j, j);
            }
            for (int i = j - 1; i >= 0; i--) {
                this.arr[i][j][0] = this.arr[j][j][0] * this.arr[i][j][0];
                this.arr[i][j][1] = this.arr[j][j][1] * this.arr[i][j][1];
            }
            //System.out.println(j);
            //System.out.println(B.toString());
            //System.out.println(this.toString());
        }
        return false;
    }
}