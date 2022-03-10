import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.math.plot.Plot2DPanel;

import javax.swing.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Shtr {
    private static final ExecutorService taskExecutor = ForkJoinPool.commonPool();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Scanner in = new Scanner(System.in);
        int k = in.nextInt();
        int n = 2;
        double[] plotShtr = new double[k];
        double[] plotN3 = new double[k];
        double[] plotShtrPar = new double[k];
        double[] x = new double[k];

        for (int i = 0; i < k; i++) {
            double[][] matrix1 = genMatrix(n);
            double[][] matrix2 = genMatrix(n);
            //printMatrix(matrix1);
            //printMatrix(matrix2);
            long shtrBefore = System.currentTimeMillis();
            double[][] resultShtr = multShtr(matrix1, matrix2);
            long shtrAfter = System.currentTimeMillis();
            //printMatrix(resultShtr);

//        RealMatrix real1 = MatrixUtils.createRealMatrix(matrix1);
//        RealMatrix real2 = MatrixUtils.createRealMatrix(matrix2);
//        System.out.println(real1.multiply(real2));

            long N3Before = System.currentTimeMillis();
            double[][] resultN3 = multiplyN3(matrix1, matrix2);
            long N3After = System.currentTimeMillis();

            long shtrParBefore = System.currentTimeMillis();
            double[][] resultShtrPar = multShtrPar(matrix1, matrix2);
            long shtrParAfter = System.currentTimeMillis();

            System.out.println(n);
            System.out.println(shtrAfter - shtrBefore);
            System.out.println(N3After - N3Before);
            System.out.println(shtrParAfter - shtrParBefore);
            System.out.println("-----------");
            plotShtr[i] = shtrAfter - shtrBefore;
            plotN3[i] = N3After - N3Before;
            plotShtrPar[i] = shtrParAfter - shtrParBefore;
            x[i] = n;
            //printMatrix(resultN3);
            n = n << 1;
            //System.out.println(n);
        }

        //plotting
        Plot2DPanel plot = new Plot2DPanel();
        plot.addLinePlot("Shtrassen", Color.GREEN, x, plotShtr);
        plot.addLinePlot("N3", Color.RED, x, plotN3);
        plot.addLinePlot("ShtrassenParallel", Color.BLUE, x, plotShtrPar);
        JFrame frame = new JFrame("a plot panel");
        frame.setContentPane(plot);
        frame.setVisible(true);
    }

    private static double[][] multiplyN3(double[][] matrix1, double[][] matrix2) {
        double[][] result = new double[matrix1.length][matrix1.length];
        for (int i = 0; i < matrix1.length; i++) {
            result[i] = new double[matrix1.length];
            for (int k = 0; k < matrix1.length; k++) {
                for (int j = 0; j < matrix1.length; j++) {
                    result[i][k] += matrix1[i][j] * matrix2[j][k];
                }
            }
        }

        return result;
    }

    private static double[][] genMatrix(int n) {
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            matrix[i] = new double[n];
            for (int j = 0; j < n; j++) {
                Random r = new Random();
                matrix[i][j] = 1000*r.nextDouble();
            }
        }

        return matrix;
    }

    private static double[][] multShtr(double[][] matrix1, double[][] matrix2) {
        int n = matrix1.length / 2;

        if (n > 32) {
            double[][] A11 = new double[n][n];
            double[][] A12 = new double[n][n];
            double[][] A21 = new double[n][n];
            double[][] A22 = new double[n][n];

            double[][] B11 = new double[n][n];
            double[][] B12 = new double[n][n];
            double[][] B21 = new double[n][n];
            double[][] B22 = new double[n][n];

            for (int i = 0; i < matrix1.length; i++) {

                if (i < n) {
                    A11[i] = new double[n];
                    A12[i] = new double[n];
                    A21[i] = new double[n];
                    A22[i] = new double[n];

                    B11[i] = new double[n];
                    B12[i] = new double[n];
                    B21[i] = new double[n];
                    B22[i] = new double[n];
                }

                for (int j = 0; j < matrix1.length; j++) {
                    if (i < n) {
                        if (j < n) {
                            A11[i][j] = matrix1[i][j];
                            B11[i][j] = matrix2[i][j];
                        } else {
                            A12[i][j - n] = matrix1[i][j];
                            B12[i][j - n] = matrix2[i][j];
                        }
                    } else {
                        if (j < n) {
                            A21[i - n][j] = matrix1[i][j];
                            B21[i - n][j] = matrix2[i][j];
                        } else {
                            A22[i - n][j - n] = matrix1[i][j];
                            B22[i - n][j - n] = matrix2[i][j];
                        }
                    }
                }
            }
//            System.out.println("A11:");
//            printMatrix(A11);
//            System.out.println("A12:");
//            printMatrix(A12);
//            System.out.println("A21:");
//            printMatrix(A21);
//            System.out.println("A22:");
//            printMatrix(A22);
//
//            System.out.println("B11:");
//            printMatrix(B11);
//            System.out.println("B12:");
//            printMatrix(B12);
//            System.out.println("B21:");
//            printMatrix(B21);
//            System.out.println("B22:");
//            printMatrix(B22);


            double[][] D = multShtr(combine(A11, A22), combine(B11, B22));
//            System.out.println("D:");
//            printMatrix(D);
            double[][] D1 = multShtr(subtract(A12, A22), combine(B21, B22));
//            System.out.println("D1:");
//            printMatrix(D1);
            double[][] D2 = multShtr(subtract(A21, A11), combine(B11, B12));
//            System.out.println("D2:");
//            printMatrix(D2);
            double[][] H1 = multShtr(combine(A11, A12), B22);
//            System.out.println("H1:");
//            printMatrix(H1);
            double[][] H2 = multShtr(combine(A21, A22), B11);
//            System.out.println("H2:");
//            printMatrix(H2);
            double[][] V1 = multShtr(A22, subtract(B21, B11));
//            System.out.println("V1:");
//            printMatrix(V1);
            double[][] V2 = multShtr(A11, subtract(B12, B22));
//            System.out.println("V2:");
//            printMatrix(V2);

            return buildMatrixFrom4(combine(combine(D, D1), subtract(V1, H1)), combine(V2, H1), combine(V1, H2), combine(combine(D, D2), subtract(V2, H2)));
        } else {
            return multiplyN3(matrix1, matrix2);
        }
    }

    private static double[][] multByScalar(double[][] matrix, int sc) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] *= sc;
            }
        }

        return matrix;
    }

    private static double[][] combine(double[][] matrix1, double[][] matrix2) {
        double[][] result = new double[matrix1.length][matrix1.length];
        for (int i = 0; i < matrix1.length; i++) {
            result[i] = new double[matrix1.length];
            for (int j = 0; j < matrix1.length; j++) {
                result[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }

        return result;
    }

    private static double[][] subtract(double[][] matrix1, double[][] matrix2) {
        double[][] result = new double[matrix1.length][matrix1.length];
        for (int i = 0; i < matrix1.length; i++) {
            result[i] = new double[matrix1.length];
            for (int j = 0; j < matrix1.length; j++) {
                result[i][j] = matrix1[i][j] - matrix2[i][j];
            }
        }

        return result;
    }

    private static double[][] buildMatrixFrom4(double[][] matrix1, double[][] matrix2, double[][] matrix3, double[][] matrix4) {
        int n = matrix1.length * 2;
        double[][] matrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            matrix[i] = new double[n];
            for (int j = 0; j < n; j++) {
                if (i < matrix1.length) {
                    if (j < matrix1.length) {
                        matrix[i][j] = matrix1[i][j];
                    } else {
                        matrix[i][j] = matrix2[i][j - matrix1.length];
                    }
                } else {
                    if (j < matrix1.length) {
                        matrix[i][j] = matrix3[i - matrix1.length][j];
                    } else {
                        matrix[i][j] = matrix4[i - matrix1.length][j - matrix1.length];
                    }
                }
            }
        }

        return matrix;
    }

    private static double[][] buildZeroMatrix(int n) {
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            matrix[i] = new double[n];
            for (int j = 0; j < n; j++) {
                matrix[i][j] = 0;
            }
        }

        return matrix;
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

    private static double[][] multShtrPar(double[][] matrix1, double[][] matrix2) {
        //System.out.println("XUY " + matrix1.length);
        int n = matrix1.length / 2;

        if (n > 32) {
            double[][] A11 = new double[n][n];
            double[][] A12 = new double[n][n];
            double[][] A21 = new double[n][n];
            double[][] A22 = new double[n][n];

            double[][] B11 = new double[n][n];
            double[][] B12 = new double[n][n];
            double[][] B21 = new double[n][n];
            double[][] B22 = new double[n][n];

            for (int i = 0; i < matrix1.length; i++) {

                if (i < n) {
                    A11[i] = new double[n];
                    A12[i] = new double[n];
                    A21[i] = new double[n];
                    A22[i] = new double[n];

                    B11[i] = new double[n];
                    B12[i] = new double[n];
                    B21[i] = new double[n];
                    B22[i] = new double[n];
                }

                for (int j = 0; j < matrix1.length; j++) {
                    if (i < n) {
                        if (j < n) {
                            A11[i][j] = matrix1[i][j];
                            B11[i][j] = matrix2[i][j];
                        } else {
                            A12[i][j - n] = matrix1[i][j];
                            B12[i][j - n] = matrix2[i][j];
                        }
                    } else {
                        if (j < n) {
                            A21[i - n][j] = matrix1[i][j];
                            B21[i - n][j] = matrix2[i][j];
                        } else {
                            A22[i - n][j - n] = matrix1[i][j];
                            B22[i - n][j - n] = matrix2[i][j];
                        }
                    }
                }
            }

            ArrayList<CompletableFuture<double[][]>> futures = new ArrayList<>();


            //D - 0
            futures.add(CompletableFuture.supplyAsync(() -> multShtrPar(combine(A11, A22), combine(B11, B22)), taskExecutor));
            //futures.add(taskExecutor.submit(() -> multShtrPar(combine(A11, A22), combine(B11, B22))));
            //D1 - 1
            futures.add(CompletableFuture.supplyAsync(() -> multShtrPar(subtract(A12, A22), combine(B21, B22)), taskExecutor));
            //D2 - 2
            futures.add(CompletableFuture.supplyAsync(() -> multShtrPar(subtract(A21, A11), combine(B11, B12)), taskExecutor));
            //H1 - 3
            futures.add(CompletableFuture.supplyAsync(() -> multShtrPar(combine(A11, A12), B22), taskExecutor));
            //H2 - 4
            futures.add(CompletableFuture.supplyAsync(() -> multShtrPar(combine(A21, A22), B11), taskExecutor));
            //V1 - 5
            futures.add(CompletableFuture.supplyAsync(() -> multShtrPar(A22, subtract(B21, B11)), taskExecutor));
            //V2 - 6
            futures.add(CompletableFuture.supplyAsync(() -> multShtrPar(A11, subtract(B12, B22)), taskExecutor));

            try {
                CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
                List<double[][]> result = allOfFuture.thenApply(v -> {
                    return futures.stream().map(f -> f.getNow(null)).collect(Collectors.toList());
                }).get();
                return buildMatrixFrom4(combine(combine(result.get(0), result.get(1)), subtract(result.get(5), result.get(3))), combine(result.get(6), result.get(3)), combine(result.get(5), result.get(4)), combine(combine(result.get(0), result.get(2)), subtract(result.get(6), result.get(4))));
            } catch (Exception ex) {
                return null;
            }

            //return buildMatrixFrom4(combine(combine(futures.get(0).get(), futures.get(1).get()), subtract(futures.get(5).get(), futures.get(3).get())), combine(futures.get(6).get(), futures.get(3).get()), combine(futures.get(5).get(), futures.get(4).get()), combine(combine(futures.get(0).get(), futures.get(2).get()), subtract(futures.get(6).get(), futures.get(4).get())));
        } else {
            return multiplyN3(matrix1, matrix2);
        }
    }
}
