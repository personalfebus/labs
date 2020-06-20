public class Matrix implements Comparable<Matrix> {
    private int[][][] arr;

    public int compareTo(Matrix Ob){
        if (this.getRank() == Ob.getRank()) return 0;
        if (this.getRank() < Ob.getRank()) return -1;
        if (this.getRank() > Ob.getRank()) return 1;
        return 0;
    }

    public Matrix(int k, int p){
        this.arr = new int[k][p][2];
    }

    public int getNom(int i, int j){
        return this.arr[i][j][0];
    }

    public int getDen(int i, int j){
        return this.arr[i][j][1];
    }

    public void addElem(int i, int j, int nominator, int denominator){
        this.arr[i][j][0] = nominator;
        this.arr[i][j][1] = denominator;
    }

    private void swapLines(int i, int ii){
        for (int j = 0; j < this.arr[0].length; j++){
            int tmp0 = this.arr[i][j][0];
            int tmp1 = this.arr[i][j][1];
            this.arr[i][j][0] = this.arr[ii][j][0];
            this.arr[i][j][1] = this.arr[ii][j][1];
            this.arr[ii][j][0] = tmp0;
            this.arr[ii][j][1] = tmp1;
        }
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
            int k = this.nod(this.arr[i][jj][0], this.arr[i][jj][1]);
            this.arr[i][jj][0] /= k;
            this.arr[i][jj][1] /= k;
        }
    }

    private void checkForZero(int i, int j){
        if (this.arr[i][j][0] == 0){
            for (int ii = i + 1; ii < this.arr.length; ii++){
                if (this.arr[ii][j][0] != 0){
                    swapLines(i, ii);
                    break;
                }
            }
        }
    }

    private void subElems(int i, int j, int jj, int cde, int cnu){
        int den1 = this.arr[i][jj][1];
        int den2 = this.arr[j][jj][1] * cde;
        int nom1 = this.arr[i][jj][0];
        int nom2 = this.arr[j][jj][0] * cnu;
        int nok = den1 * den2 / this.nod(den1, den2);
        nom1 = nom1 * nok / den1;
        nom2 = nom2 * nok / den2;
        nom1 -= nom2;
        this.arr[i][jj][0] = nom1;
        this.arr[i][jj][1] = nok;
        this.reduceFR(i, jj);
    }

    public int getRank(){
        for (int j = 0; j < this.arr[1].length; j++){
            for (int i = j + 1; i < this.arr.length; i++){
                this.checkForZero(i, j);
                int cnu = (this.arr[i][j][0] * this.arr[j][j][1]);
                int cde = (this.arr[i][j][1] * this.arr[j][j][0]);
                for (int jj = j; jj < this.arr[1].length; jj++){
                    if ((cde == 0) || (cnu == 0)) continue;
                    subElems(i, j, jj, cde, cnu);
                }
            }
        }

        int rank = 0;

        for (int i = 0; i < this.arr.length; i++){
            int boo = 0;
            for (int j = 0; j < this.arr[1].length; j++){
                if (this.arr[i][j][0] != 0){
                    boo = 1;
                    break;
                }
            }
            rank += boo;
        }
        return rank;
    }
}