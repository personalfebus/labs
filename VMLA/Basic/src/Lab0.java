import java.util.ArrayList;
import java.util.Scanner;

public class Lab0 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter opperation");
            int type = scanner.nextInt();
            switch (type) {
                case 1: {
                    System.out.println("Scalar Product");
                    System.out.println("Enter dimensions");
                    int dim = scanner.nextInt();
                    ArrayList<Float> vect1 = new ArrayList<>();
                    ArrayList<Float> vect2 = new ArrayList<>();
                    System.out.println("Enter vector1");
                    for (int i = 0; i < dim; i++) {
                        vect1.add(scanner.nextFloat());
                    }
                    System.out.println("Enter vector2");
                    for (int i = 0; i < dim; i++) {
                        vect2.add(scanner.nextFloat());
                    }
                    System.out.println(scalarProduct(vect1, vect2));
                    break;
                }
                case 2: {
                    System.out.println("Matrix Product");
                    System.out.println("Enter matrix1 dimensions");
                    int n1 = scanner.nextInt();
                    int m1 = scanner.nextInt();
                    System.out.println("Enter matrix2 dimensions");
                    int n2 = scanner.nextInt();
                    int m2 = scanner.nextInt();
                    if (n1 != m2) {
                        System.out.println("Bad dimensions error - i1 must be equal to j2");
                    } else {
                        ArrayList<ArrayList<Float>> matrix1 = new ArrayList<>();
                        ArrayList<ArrayList<Float>> matrix2 = new ArrayList<>();
                        System.out.println("Enter matrix1");
                        for (int i = 0; i < n1; i++) {
                            matrix1.add(new ArrayList<>());
                            for (int j = 0; j < m1; j++) {
                                matrix1.get(i).add(scanner.nextFloat());
                            }
                        }
                        System.out.println("Enter matrix2");
                        for (int i = 0; i < n2; i++) {
                            matrix2.add(new ArrayList<>());
                            for (int j = 0; j < m2; j++) {
                                matrix2.get(i).add(scanner.nextFloat());
                            }
                        }
                        matrixProduct(matrix1, matrix2);
                    }
                    break;
                }
                case 3: {
                    System.out.println("Transposition");
                    System.out.println("Enter matrix dimensions");
                    int n = scanner.nextInt();
                    int m = scanner.nextInt();
                    ArrayList<ArrayList<Float>> matrix = new ArrayList<>();
                    System.out.println("Enter matrix");
                    for (int i = 0; i < n; i++) {
                        matrix.add(new ArrayList<>());
                        for (int j = 0; j < m; j++) {
                            matrix.get(i).add(scanner.nextFloat());
                        }
                    }
                    matrixTransposition(matrix);
                    break;
                }
                default:
                    System.out.print("Bad");
            }
        }
    }

    public static float scalarProduct(ArrayList<Float> vect1, ArrayList<Float> vect2) {
        float result = 0;
        for (int i = 0; i < vect1.size(); i++) {
            result += vect1.get(i)*vect2.get(i);
        }
        return result;
    }

    public static void matrixProduct(ArrayList<ArrayList<Float>> matrix1, ArrayList<ArrayList<Float>> matrix2) {
        ArrayList<ArrayList<Float>> result = new ArrayList<>();
        for (int i = 0; i < matrix2.size(); i++) {
            result.add(new ArrayList<>());
            for (int j = 0; j < matrix1.get(0).size(); j++) {
                float rez = 0;
                for (int k = 0; k < matrix1.size(); k++) {
                    rez += matrix1.get(k).get(j)*matrix2.get(i).get(k);
                }
                result.get(i).add(rez);
            }
        }
        printMatrix(result);
    }

    public static void matrixTransposition(ArrayList<ArrayList<Float>> matrix) {
        ArrayList<ArrayList<Float>> result = new ArrayList<>();
        for (int i = 0; i < matrix.size(); i++) {
            result.add(new ArrayList<>());
            for (int j = 0; j < matrix.get(0).size(); j++) {
                result.get(i).add(matrix.get(i).get(j));
            }
        }
        printMatrix(result);
    }

    public static void printMatrix(ArrayList<ArrayList<Float>> matrix) {
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(0).size(); j++) {
                System.out.print(matrix.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
}
