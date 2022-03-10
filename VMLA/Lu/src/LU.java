import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class LU {
    private static double[][] a;
    private static double[][] ll;
    private static double[][] uu;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        genMatrix(n);
        //genSymMatrix(n);
        printMatrix(a);
        buildLU();

        double[] b = genVector(n);
        System.out.print("B: ");
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i] + " ");
        }
        System.out.println();

        RealMatrix myB = solveLUEq(b);

        double comp = 0d;
        double compaEtal = 0d;
        for (int i = 0; i < myB.getRowDimension(); i++) {
            comp += (b[i] - myB.getRow(i)[0])*(b[i] - myB.getRow(i)[0]);
            compaEtal += b[i]*b[i];
        }

        double delta = Math.sqrt(comp) / Math.sqrt(compaEtal) * 100;
        System.out.println("Относительная погрешность Ax=b через LU-разложение: " + delta);

        System.out.println("----------------");
        RealMatrix realA = MatrixUtils.createRealMatrix(a);
        LUDecomposition decompositionA = new LUDecomposition(realA);
        System.out.println("det(A) = " + decompositionA.getDeterminant());

        RealMatrix realL = MatrixUtils.createRealMatrix(ll);
        LUDecomposition decompositionL = new LUDecomposition(realL);
        RealMatrix realU = MatrixUtils.createRealMatrix(uu);
        LUDecomposition decompositionU = new LUDecomposition(realU);
        System.out.println("det(L)*det(U) = " + decompositionL.getDeterminant()*decompositionU.getDeterminant());
        System.out.println("------------------");
        RealMatrix inversedA = MatrixUtils.inverse(realA);
        RealMatrix inversedL = MatrixUtils.inverse(realL);
        RealMatrix inversedU = MatrixUtils.inverse(realU);
        System.out.println(inversedA);
        System.out.println(inversedL.multiply(inversedU));
    }

    private static void genMatrix(int n) {
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            matrix[i] = new double[n];
            for (int j = 0; j < n; j++) {
                Random r = new Random();
                if (i == j) matrix[i][j] = 1000*(n)*r.nextDouble();
                else {
                    matrix[i][j] = 1000*r.nextDouble();
                }
            }
        }

        a = matrix;
    }

    public static double[] genVector(int n) {
        double[] vector = new double[n];

        for (int i = 0; i < n; i++) {
            Random r = new Random();
            vector[i] = 1000*r.nextDouble();
        }

        return vector;
    }

    public static void genSymMatrix(int n) {
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            matrix[i] = new double[n];
            for (int j = 0; j < i + 1; j++) {
                Random r = new Random();
                if (i == j) matrix[i][j] = 1000*(n)*r.nextDouble();
                else {
                    matrix[i][j] = 1000*r.nextDouble();
                    matrix[j][i] = matrix[i][j];
                }
            }
        }

        a = matrix;
    }

    private static void buildLU() {
        double[][] l = new double[a.length][a.length];
        double[][] u = new double[a.length][a.length];

        for (int i = 0; i < a.length; i++) {
            l[i] = new double[a.length];
            u[i] = new double[a.length];
        }

        for (int z = 0; z < a.length; z++) {
            for (int j = z; j < a.length; j++) {
                double sum = 0d;
                for (int k = 0; k < z; k++) {
                    sum += l[z][k]*u[k][j];
                }
                u[z][j] = a[z][j] - sum;
                //System.out.println(String.format("u[%d][%d]=%f", z, j, u[z][j]));
            }

            for (int i = z; i < a.length; i++) {
                if (i == z) {
                    l[z][z] = 1;
                    continue;
                }
                double sum = 0d;
                for (int k = 0; k < z; k++) {
                    sum += l[i][k]*u[k][z];
                }
                l[i][z] = 1/u[z][z] * (a[i][z] - sum);
                //System.out.println(String.format("l[%d][%d]=%f", i, z, l[i][z]));
            }
        }

        ll = l;
        uu = u;

        printMatrix(ll);
        printMatrix(uu);

        RealMatrix realL = MatrixUtils.createRealMatrix(ll);
        RealMatrix realU = MatrixUtils.createRealMatrix(uu);
        RealMatrix res = realL.multiply(realU);
        System.out.println(res);

        double comp = 0d;
        double compaEtal = 0d;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                comp += (a[i][j] - res.getEntry(i, j))*(a[i][j] - res.getEntry(i, j));
                compaEtal += a[i][j]*a[i][j];
            }
        }

        double delta = Math.sqrt(comp) / Math.sqrt(compaEtal) * 100;
        System.out.println("Относительная погрешность LU-разложения: " + delta);
    }

    public static RealMatrix solveLUEq(double[] b) {
        int n = ll.length;
        int m = n + 1;
        ArrayList<ArrayList<Double>> matrixL = new ArrayList<>();
        ArrayList<ArrayList<Double>> cloneL = new ArrayList<>();

        for (int i = 0; i < ll.length; i++) {
            matrixL.add(new ArrayList<>());
            cloneL.add(new ArrayList<>());
            for (int j = 0; j < ll.length; j++) {
                matrixL.get(i).add(ll[i][j]);
                cloneL.get(i).add(ll[i][j]);
            }
            matrixL.get(i).add(b[i]);
            cloneL.get(i).add(b[i]);
        }

        RealMatrix y = backGaussTopToBottom(n, m, cloneL, matrixL);
        RealMatrix realMatrix1 = MatrixUtils.createRealMatrix(ll);
        System.out.println(realMatrix1.multiply(y));

        ArrayList<ArrayList<Double>> matrixT = new ArrayList<>();
        ArrayList<ArrayList<Double>> cloneT = new ArrayList<>();
        for (int i = 0; i < uu.length; i++) {
            matrixT.add(new ArrayList<>());
            cloneT.add(new ArrayList<>());
            for (int j = 0; j < uu.length; j++) {
                matrixT.get(i).add(uu[i][j]);
                cloneT.get(i).add(uu[i][j]);
            }
            matrixT.get(i).add(y.getEntry(i, 0));
            cloneT.get(i).add(y.getEntry(i, 0));
        }

        RealMatrix x = backGauss(n, m, cloneT, matrixT);
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(a);
        System.out.println(realMatrix.multiply(x));

        return realMatrix.multiply(x);
    }

    public static RealMatrix backGauss(int n, int m, ArrayList<ArrayList<Double>> clone, ArrayList<ArrayList<Double>> matrix) {
        //Обратный ход
        for (int k = n - 1; k >= 0; k--) {
            for (int i = m - 1; i >= 0; i--) {
                clone.get(k).set(i, clone.get(k).get(i) / matrix.get(k).get(k));
            }

            for (int i = k - 1; i >= 0; i--) {
                double val = clone.get(i).get(k) / clone.get(k).get(k);
                for (int j = m - 1; j >= 0; j--) {
                    clone.get(i).set(j, clone.get(i).get(j) - clone.get(k).get(j) * val);
                }
            }
        }

        double[][] tmpArr = new double[n][1];
        for (int i = 0; i < n; i++) {
            tmpArr[i][0] = clone.get(i).get(m - 1);
        }
        RealMatrix xVector = MatrixUtils.createRealMatrix(tmpArr);
        return xVector;
    }

    public static RealMatrix backGaussTopToBottom(int n, int m, ArrayList<ArrayList<Double>> clone, ArrayList<ArrayList<Double>> matrix) {
        //Обратный ход
        for (int k = 0; k < n; k++) {
            clone.get(k).set(m - 1, clone.get(k).get(m - 1) / matrix.get(k).get(k));
            for (int i = 0; i < m - 1; i++) {
                clone.get(k).set(i, clone.get(k).get(i) / matrix.get(k).get(k));
            }

            for (int i = k + 1; i < n; i++) {
                double val = clone.get(i).get(k) / clone.get(k).get(k);
                for (int j = 0; j < m; j++) {
                    clone.get(i).set(j, clone.get(i).get(j) - clone.get(k).get(j) * val);
                }
            }
        }

        double[][] tmpArr = new double[n][1];
        for (int i = 0; i < n; i++) {
            tmpArr[i][0] = clone.get(i).get(m - 1);
        }
        RealMatrix xVector = MatrixUtils.createRealMatrix(tmpArr);
        return xVector;
    }

    public static void printMatrix(double[][] m) {
        System.out.println("-------------------");
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                System.out.printf("%f ", m[i][j]);
            }
            System.out.println();
        }
        System.out.println("-------------------");
    }
}
