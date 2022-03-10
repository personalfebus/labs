import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class Holeckiy {
    private static double[][] ll;
    private static double[][] lt;
    private static double[][] a;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        double[][] m = genSymMatrix(n);

        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }

        buildHolec(m);
        double[] b = genVector(n);
        System.out.print("B: ");
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i] + " ");
        }
        System.out.println();

        RealMatrix myB = solveHolecEq(b);

        double comp = 0d;
        double compaEtal = 0d;
        for (int i = 0; i < myB.getRowDimension(); i++) {
                comp += (b[i] - myB.getRow(i)[0])*(b[i] - myB.getRow(i)[0]);
                compaEtal += b[i]*b[i];
        }

        double delta = Math.sqrt(comp) / Math.sqrt(compaEtal) * 100;
        System.out.println(delta);
    }

    public static double[] genVector(int n) {
        double[] vector = new double[n];

        for (int i = 0; i < n; i++) {
            Random r = new Random();
            vector[i] = 1000*r.nextDouble();
        }

        return vector;
    }

    public static double[][] genSymMatrix(int n) {
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

        return matrix;
    }

    public static void buildHolec(double[][] matrix) {
        double[][] l = new double[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            l[0] = new double[matrix.length];
            for (int j = 0; j < matrix[i].length; j++) {
                if (j > i) l[i][j] = 0d;
                else l[i][j] = Double.MAX_VALUE;
            }
        }

        l[0][0] = Math.sqrt(matrix[0][0]);

        for (int i = 1; i < matrix.length; i++) {
            l[i][0] = matrix[i][0]/l[0][0];
            double sum = 0d;
            for (int p = 0; p < i; p++) {
                if (l[i][p] == Double.MAX_VALUE) {
                    double sum2 = 0d;
                    for (int z = 0; z < p; z++) {
                        sum2 += l[p][z]*l[i][z];
                    }
                    //System.out.println(String.format("sum2 = %f; matrix[%d][%d]= %f; l[%d][%d] = %f", sum2, i, p, matrix[i][p],
                            //p, p, l[p][p]));
                    l[i][p] = (1/l[p][p])*(matrix[i][p] - sum2);
                }
                sum += l[i][p]*l[i][p];
            }

            //System.out.println(String.format("sum=%f;matrix[%d][%d]=%f", sum, i, i, matrix[i][i]));
            l[i][i] = Math.sqrt(matrix[i][i] - sum);
            //System.out.println(String.format("l[%d][%d]=%f", i, i, l[i][i]));
            //printMatrix(l);
        }

        printMatrix(l);
        RealMatrix holec = MatrixUtils.createRealMatrix(l);

        double[][] lTransp = transpose(l);

        printMatrix(lTransp);
        RealMatrix holecTransp = MatrixUtils.createRealMatrix(lTransp);
        RealMatrix res = holec.multiply(holecTransp);
        System.out.println(res);

        double comp = 0d;
        double compaEtal = 0d;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                comp += (matrix[i][j] - res.getEntry(i, j))*(matrix[i][j] - res.getEntry(i, j));
                compaEtal += matrix[i][j]*matrix[i][j];
            }
        }

        double delta = Math.sqrt(comp) / Math.sqrt(compaEtal) * 100;
        System.out.println(delta);

        ll = l;
        lt = lTransp;
    }

    public static RealMatrix solveHolecEq(double[] b) {
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
        for (int i = 0; i < lt.length; i++) {
            matrixT.add(new ArrayList<>());
            cloneT.add(new ArrayList<>());
            for (int j = 0; j < lt.length; j++) {
                matrixT.get(i).add(lt[i][j]);
                cloneT.get(i).add(lt[i][j]);
            }
            matrixT.get(i).add(y.getEntry(i, 0));
            cloneT.get(i).add(y.getEntry(i, 0));
        }

        RealMatrix x = backGauss(n, m, cloneT, matrixT);
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(a);
        System.out.println(realMatrix.multiply(x));

        return realMatrix.multiply(x);
    }

    public static double[][] transpose(double[][] l) {
        double[][] lTransp = new double[l.length][l.length];
        for (int i = 0; i < lTransp.length; i++) {
            lTransp[i] = new double[l.length];
            for (int j = 0; j < lTransp.length; j++) {
                lTransp[i][j] = l[j][i];
            }
        }
        return lTransp;
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

    public static void printMatrix(double[][] l) {
        System.out.println("-------------------");
        for (int i = 0; i < l.length; i++) {
            for (int j = 0; j < l.length; j++) {
                System.out.printf("%f ", l[i][j]);
            }
            System.out.println();
        }
    }
}
