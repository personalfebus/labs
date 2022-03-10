import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.math.plot.Plot2DPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Gauss {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter starting matrix size, final matrix size");
        int startSize = in.nextInt();
        int finalSize = in.nextInt();
        double[] func1 = new double[finalSize - startSize];
        double[] func2 = new double[finalSize - startSize];
        double[] func3 = new double[finalSize - startSize];
        //число обусловленности
        double[] func4 = new double[finalSize - startSize];
        double[] x = new double[finalSize - startSize];


        for (int s = startSize; s < finalSize; s++) {
            ArrayList<ArrayList<Double>> testMatrix = genMatrix(s, s + 1, false);
            RealMatrix testMatrixForInv = MatrixUtils.createRealMatrix(s, s);

            Double sumO = 0.d;
            Double sumI = 0.d;
            for (int i = 0; i < s; i++) {
                for (int j = 0; j < s; j++) {
                    testMatrixForInv.addToEntry(i, j, testMatrix.get(i).get(j));
                }
            }

            RealMatrix inversed = MatrixUtils.inverse(testMatrixForInv);

            for (int i = 0; i < s; i++) {
                for (int j = 0; j < s; j++) {
                    sumI += inversed.getEntry(i, j);
                    sumO += testMatrixForInv.getEntry(i, j);
                }
            }

            func4[s - startSize] = Math.sqrt(sumO) * Math.sqrt(sumI);
            //printMatrix(testMatrix.size(), testMatrix.get(0).size(), testMatrix);
            RealMatrix m1 = defaultGauss(testMatrix);
            RealMatrix m2 = rowGauss(testMatrix);
            RealMatrix m3 = columnGauss(testMatrix);
            RealMatrix m4Etal = columnAndRowGauss(testMatrix);

            Double sum1 = 0.d;
            Double sum2 = 0.d;
            Double sum3 = 0.d;
            Double sumEtal = 0.d;
            for (int i = 0; i < m1.getRowDimension(); i++) {
                sum1 += (m1.getRow(i)[0] - m4Etal.getRow(i)[0]) * (m1.getRow(i)[0] - m4Etal.getRow(i)[0]);
                sum2 += (m2.getRow(i)[0] - m4Etal.getRow(i)[0]) * (m2.getRow(i)[0] - m4Etal.getRow(i)[0]);
                sum3 += (m3.getRow(i)[0] - m4Etal.getRow(i)[0]) * (m3.getRow(i)[0] - m4Etal.getRow(i)[0]);
                sumEtal += m4Etal.getRow(i)[0] * m4Etal.getRow(i)[0];
            }
//        System.out.println("Absolute: " + Math.sqrt(sum1));
            double delta1 = Math.sqrt(sum1) / Math.sqrt(sumEtal) * 100;
            double delta2 = Math.sqrt(sum2) / Math.sqrt(sumEtal) * 100;
            double delta3 = Math.sqrt(sum3) / Math.sqrt(sumEtal) * 100;
//        System.out.println("Otnosit: " + otnosit);
            x[s - startSize] = s;
            func1[s - startSize] = delta1;
            func2[s - startSize] = delta2;
            func3[s - startSize] = delta3;
        }
        //plotting
        Plot2DPanel plot = new Plot2DPanel();
        Plot2DPanel plotObusl = new Plot2DPanel();
        plot.addLinePlot("default+best", Color.GREEN, x, func1);
        plot.addLinePlot("row+best", Color.RED, x, func2);
        plot.addLinePlot("column+best", Color.BLUE, x, func3);
        plotObusl.addLinePlot("число обусловленности", Color.BLUE, x, func4);
        JFrame frame = new JFrame("a plot panel");
        frame.setContentPane(plot);
        frame.setVisible(true);
        JFrame frameObusl = new JFrame("obusl panel");
        frameObusl.setContentPane(plotObusl);
        frameObusl.setVisible(true);
    }

    public static ArrayList<ArrayList<Double>> genMatrix(int n, int m, boolean isDiagGreat) {
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            matrix.add(new ArrayList<>());
            for (int j = 0; j < m; j++) {
                Random r = new Random();
                if (isDiagGreat && i == j) matrix.get(i).add(1000*(n - 1)*r.nextDouble());
                else matrix.get(i).add(1000*r.nextDouble());
                //if (i == 0) System.out.println(matrix.get(i).get(j));
            }
        }
        return matrix;
    }

    private static RealMatrix defaultGauss(ArrayList<ArrayList<Double>> testMatrix) {
        int n = testMatrix.size();
        int m = testMatrix.get(0).size();
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        ArrayList<ArrayList<Double>> clone = new ArrayList<>();
        ArrayList<Integer> positions = new ArrayList<>();
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(n, m - 1);

        for (int i = 0; i < n; i++) {
            matrix.add(new ArrayList<>());
            clone.add(new ArrayList<>());
            for (int j = 0; j < m; j++) {
                if (i == 0) positions.add(j);
                matrix.get(i).add(testMatrix.get(i).get(j));
                clone.get(i).add(matrix.get(i).get(j));
                if (j < m - 1) {
                    realMatrix.addToEntry(i, j, matrix.get(i).get(j));
                }
            }
        }

        for (int k = 0; k < n; k++) {
            int firstNotZero = -1;
            for (int i = k; i < m - 1; i++) {
                if (clone.get(k).get(i) != 0) {
                    firstNotZero = i;
                    swapColumns(k, firstNotZero, matrix, clone, positions);
                    break;
                }
            }

            if (firstNotZero < 0) {
                if (clone.get(k).get(m - 1) != 0) {
                    System.out.println("Unsolvable matrix");
                    return null;
                }
                System.out.println("Есть строка из 0");
                matrix.remove(k--);
                n--;
                continue;
            }

            for (int i = firstNotZero; i < m; i++) {
                clone.get(k).set(i, clone.get(k).get(i) / matrix.get(k).get(k));
            }

            for (int i = k + 1; i < n; i++) {
                double val = clone.get(i).get(k) / clone.get(k).get(k);
                for (int j = 0; j < m; j++) {
                    clone.get(i).set(j, clone.get(i).get(j) - clone.get(k).get(j) * val);
                }
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    matrix.get(i).set(j, clone.get(i).get(j));
                }
            }
        }

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
            tmpArr[positions.get(i)][0] = clone.get(i).get(m - 1);
        }
        RealMatrix xVector = MatrixUtils.createRealMatrix(tmpArr);
        return xVector;
    }

    private static RealMatrix columnGauss(ArrayList<ArrayList<Double>> testMatrix) {
        int n = testMatrix.size();
        int m = testMatrix.get(0).size();
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        ArrayList<ArrayList<Double>> clone = new ArrayList<>();
        ArrayList<Integer> positions = new ArrayList<>();
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(n, m - 1);

        for (int i = 0; i < n; i++) {
            matrix.add(new ArrayList<>());
            clone.add(new ArrayList<>());
            for (int j = 0; j < m; j++) {
                if (i == 0) positions.add(j);
                matrix.get(i).add(testMatrix.get(i).get(j));
                clone.get(i).add(matrix.get(i).get(j));
                if (j < m - 1) {
                    realMatrix.addToEntry(i, j, matrix.get(i).get(j));
                }
            }
        }

        for (int k = 0; k < n; k++) {
            double max = -Double.MAX_VALUE;
            int maxPos = -1;
            for (int i = k; i < n; i++) {
                if (clone.get(i).get(k) > max && clone.get(i).get(k) != 0) {
                    maxPos = i;
                    max = clone.get(i).get(k);
                }
            }

            if (maxPos < 0) {
                printMatrix(n, m, matrix);
                if (clone.get(k).get(m - 1) != 0) {
                    System.out.println("Unsolvable matrix");
                    return null;
                }
                System.out.println("Есть столбец из 0");
                continue;
            } else {
                swapRows(k, maxPos, matrix, clone);
            }

            for (int i = k; i < m; i++) {
                clone.get(k).set(i, clone.get(k).get(i) / matrix.get(k).get(k));
            }

            for (int i = k + 1; i < n; i++) {
                double val = clone.get(i).get(k) / clone.get(k).get(k);
                for (int j = 0; j < m; j++) {
                    clone.get(i).set(j, clone.get(i).get(j) - clone.get(k).get(j) * val);
                }
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    matrix.get(i).set(j, clone.get(i).get(j));
                }
            }
        }

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
            tmpArr[positions.get(i)][0] = clone.get(i).get(m - 1);
        }
        RealMatrix xVector = MatrixUtils.createRealMatrix(tmpArr);
        return xVector;
    }

    private static RealMatrix rowGauss(ArrayList<ArrayList<Double>> testMatrix) {
        int n = testMatrix.size();
        int m = testMatrix.get(0).size();
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        ArrayList<ArrayList<Double>> clone = new ArrayList<>();
        ArrayList<Integer> positions = new ArrayList<>();
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(n, m - 1);

        for (int i = 0; i < n; i++) {
            matrix.add(new ArrayList<>());
            clone.add(new ArrayList<>());
            for (int j = 0; j < m; j++) {
                if (i == 0) positions.add(j);
                matrix.get(i).add(testMatrix.get(i).get(j));
                clone.get(i).add(matrix.get(i).get(j));
                if (j < m - 1) {
                    realMatrix.addToEntry(i, j, matrix.get(i).get(j));
                }
            }
        }

        for (int k = 0; k < n; k++) {
            double max = -Double.MAX_VALUE;
            int maxPos = -1;
            for (int i = k; i < m - 1; i++) {
                if (clone.get(k).get(i) > max && clone.get(k).get(i) != 0) {
                    maxPos = i;
                    max = clone.get(k).get(i);
                }
            }

            if (maxPos < 0) {
                if (clone.get(k).get(m - 1) != 0) {
                    System.out.println("Unsolvable matrix");
                    return null;
                }
                System.out.println("Есть столбец из 0");
                continue;
            } else {
                swapColumns(k, maxPos, matrix, clone, positions);
            }

            for (int i = k; i < m; i++) {
                clone.get(k).set(i, clone.get(k).get(i) / matrix.get(k).get(k));
            }

            for (int i = k + 1; i < n; i++) {
                double val = clone.get(i).get(k) / clone.get(k).get(k);
                for (int j = 0; j < m; j++) {
                    clone.get(i).set(j, clone.get(i).get(j) - clone.get(k).get(j) * val);
                }
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    matrix.get(i).set(j, clone.get(i).get(j));
                }
            }
        }

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
            tmpArr[positions.get(i)][0] = clone.get(i).get(m - 1);
        }

        RealMatrix xVector = MatrixUtils.createRealMatrix(tmpArr);
        //System.out.println(realMatrix.multiply(xVector));
        return xVector;
    }

    private static RealMatrix columnAndRowGauss(ArrayList<ArrayList<Double>> testMatrix) {
        int n = testMatrix.size();
        int m = testMatrix.get(0).size();
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        ArrayList<ArrayList<Double>> clone = new ArrayList<>();
        ArrayList<Integer> positions = new ArrayList<>();
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(n, m - 1);

        for (int i = 0; i < n; i++) {
            matrix.add(new ArrayList<>());
            clone.add(new ArrayList<>());
            for (int j = 0; j < m; j++) {
                if (i == 0) positions.add(j);
                matrix.get(i).add(testMatrix.get(i).get(j));
                clone.get(i).add(matrix.get(i).get(j));
                if (j < m - 1) {
                    realMatrix.addToEntry(i, j, matrix.get(i).get(j));
                }
            }
        }

        for (int k = 0; k < n; k++) {
            double max = -Double.MAX_VALUE;
            int maxPos = -1;
            boolean isColumn = false;

            for (int i = k; i < m - 1; i++) {
                if (clone.get(k).get(i) > max && clone.get(k).get(i) != 0) {
                    isColumn = true;
                    maxPos = i;
                    max = clone.get(k).get(i);
                }
            }
            for (int i = k; i < n; i++) {
                if (clone.get(i).get(k) > max && clone.get(i).get(k) != 0) {
                    isColumn = false;
                    maxPos = i;
                    max = clone.get(i).get(k);
                }
            }

            if (maxPos < 0) {
                if (clone.get(k).get(m - 1) != 0) {
                    System.out.println("Unsolvable matrix");
                    return null;
                }
                System.out.println("Есть столбец из 0");
                continue;
            } else {
                if (isColumn) {
                    swapColumns(k, maxPos, matrix, clone, positions);
                } else {
                    swapRows(k, maxPos, matrix, clone);
                }
            }

            for (int i = k; i < m; i++) {
                clone.get(k).set(i, clone.get(k).get(i) / matrix.get(k).get(k));
            }

            for (int i = k + 1; i < n; i++) {
                double val = clone.get(i).get(k) / clone.get(k).get(k);
                for (int j = 0; j < m; j++) {
                    clone.get(i).set(j, clone.get(i).get(j) - clone.get(k).get(j) * val);
                }
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    matrix.get(i).set(j, clone.get(i).get(j));
                }
            }
        }

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
            tmpArr[positions.get(i)][0] = clone.get(i).get(m - 1);
        }

        RealMatrix xVector = MatrixUtils.createRealMatrix(tmpArr);
        return xVector;
    }

    private static void printMatrix(int n, int m, ArrayList<ArrayList<Double>> matrix) {
        System.out.println("Matrix");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(matrix.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }

    private static void swapColumns(int j1, int j2,
                                    ArrayList<ArrayList<Double>> matrix,
                                    ArrayList<ArrayList<Double>> clone, ArrayList<Integer> positions) {
        for (int i = 0; i < matrix.size(); i++) {
            double tmp = matrix.get(i).get(j1);
            matrix.get(i).set(j1, matrix.get(i).get(j2));
            matrix.get(i).set(j2, tmp);
            clone.get(i).set(j1, matrix.get(i).get(j1));
            clone.get(i).set(j2, matrix.get(i).get(j2));
        }
        int tmp = positions.get(j1);
        positions.set(j1, positions.get(j2));
        positions.set(j2, tmp);
    }

    public static void swapRows(int i1, int i2, ArrayList<ArrayList<Double>> matrix,
                                ArrayList<ArrayList<Double>> clone) {
        ArrayList<Double> tmp = matrix.get(i1);
        matrix.set(i1, matrix.get(i2));
        matrix.set(i2, tmp);
        ArrayList<Double> tmp2 = clone.get(i1);
        clone.set(i1, clone.get(i2));
        clone.set(i2, tmp2);
    }
}
