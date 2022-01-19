import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Алгоритм Хиршберга
 * Депенденси - blosum62.txt, default.txt, DNAfull.txt - скоринг матрицы, должны лежать в корне проекта
 * Тесты: который запускается - fasta.txt (должен лежать в корне проекта), остальные варианты - tests.txt
 */
public class Lab {
    public static void main(String[] args) throws IOException {
        /**
         * Последовательности чистаются только из 1ого файла формата fasta
         * Закомментирован интефейс взаимодействия с программой через консоль вместо ключей коммандной строки
         */
        Scanner in = new Scanner(System.in);
        if (args.length % 2 != 0) throw new IOException("Bad number of key arguments");
        int gap = -2;
        String pathToFasta = "";
        int type = 3;
        String outputFileName = "output.txt";
        boolean outputToFile = false;
        for (int k = 0; k < args.length; k += 2) {
            switch (args[k]) {
                case "-i": {
                    pathToFasta = args[k + 1];
                    break;
                }
                case "-g": {
                    gap = Integer.parseInt(args[k + 1]);
                    break;
                }
                case "-t": {
                    type = Integer.parseInt(args[k + 1]);
                    break;
                }
                case "-o": {
                    outputToFile = true;
                    outputFileName = args[k + 1];
                    break;
                }
                default: throw new IOException("Bad Key");
            }
        }
        ArrayList<DNA> array = new ArrayList<>();
        FileInputStream stream = new FileInputStream(pathToFasta);
        Scanner scan = new Scanner(stream);
        String str = scan.nextLine();
        StringBuilder builder = new StringBuilder();
        while (scan.hasNextLine()) {
            String n = scan.nextLine();
            if (n.charAt(0) == '>') {
                array.add(new DNA(str, builder.toString(), array.size()));
                str = n;
                builder = new StringBuilder();
                continue;
            }
            builder.append(n);
        }
        array.add(new DNA(str, builder.toString(), array.size()));
        scan.close();

        /**
         * Конфигурация матриц и сопоставляющиъ хэш-таблиц
         */
        HashMap<Character, Integer> charToPosBlosum62 = new HashMap<>();
        final int blosumSize = 24;
        Lab.configureBlosum62(charToPosBlosum62);
        ArrayList<ArrayList<Integer>> blosum62 = new ArrayList<>();
        FileInputStream blosumStream = new FileInputStream("blosum62.txt");
        Scanner blosumScanner = new Scanner(blosumStream);
        for (int i = 0; i < blosumSize; i++) {
            blosum62.add(new ArrayList<>());
            for (int j = 0; j < blosumSize; j++) {
                blosum62.get(i).add(blosumScanner.nextInt());
            }
        }

        HashMap<Character, Integer> charToPosDNAfull = new HashMap<>();
        final int DNAfullSize = 15;
        Lab.configureDNAfull(charToPosDNAfull);
        ArrayList<ArrayList<Integer>> DNAfull = new ArrayList<>();
        FileInputStream DNAfullStream = new FileInputStream("DNAfull.txt");
        Scanner DNAfullScanner = new Scanner(DNAfullStream);
        for (int i = 0; i < DNAfullSize; i++) {
            DNAfull.add(new ArrayList<>());
            for (int j = 0; j < DNAfullSize; j++) {
                DNAfull.get(i).add(DNAfullScanner.nextInt());
            }
        }

        HashMap<Character, Integer> charToPosDef = new HashMap<>();
        final int DefSize = 26;
        Lab.configureDefault(charToPosDef);
        ArrayList<ArrayList<Integer>> Def = new ArrayList<>();
        FileInputStream DefStream = new FileInputStream("default.txt");
        Scanner DefScanner = new Scanner(DefStream);
        for (int i = 0; i < DefSize; i++) {
            Def.add(new ArrayList<>());
            for (int j = 0; j < DefSize; j++) {
                Def.get(i).add(DefScanner.nextInt());
            }
        }

        ArrayList<ArrayList<Integer>> S = new ArrayList<>();
        HashMap<Character, Integer> M = new HashMap<>();

        switch (type) {
            case 1: {
                S = blosum62;
                M = charToPosBlosum62;
                break;
            }
            case 2: {
                S = DNAfull;
                M = charToPosDNAfull;
                break;
            }
            default: {
                S = Def;
                M = charToPosDef;
            }
        }

        String[] result = Hirschberg(gap, array.get(0).getValue(), array.get(1).getValue(), M, S);
        System.out.println(Arrays.toString(result));

        /**
         * Корректное завершение работы программы
         */
        blosumStream.close();
        DNAfullStream.close();
        stream.close();
        if (outputToFile) {
            System.out.close();
        }
    }

    /**
     * Алгоритм Хиршберга
     * @param gap Гэп
     * @param x Первая строка
     * @param y Вторая строка
     * @param M Маппинг
     * @param S Скоринг матрица
     * @return Две алайнутые строки
     */
    public static String[] Hirschberg(int gap, String x, String y,
                                               HashMap<Character, Integer> M,
                                               ArrayList<ArrayList<Integer>> S
    ) {
        if (x.length() < y.length()) {
            return Hirschberg(gap, y, x, M, S);
        }

        StringBuilder Z = new StringBuilder();
        StringBuilder W = new StringBuilder();

        if (x.isEmpty()) {
            for (int i = 0; i < y.length(); i++) {
                Z.append('-');
                W.append(y.charAt(i));
            }

            return new String[]{Z.toString(), W.toString()};
        } else if (y.isEmpty()) {
            for (int i = 0; i < x.length(); i++) {
                Z.append(x.charAt(i));
                W.append('-');
            }

            return new String[]{Z.toString(), W.toString()};
        } else if (x.length() == 1 || y.length() == 1) {
            return needlemanWunsch(gap, x, y, M, S);
        } else {
            int xLen = x.length();
            int xMid = x.length() / 2;
            int yLen = y.length();

            int[] scoreL = NWScore(gap, x.substring(0, xMid), y, M, S);
            int[] scoreR = NWScore(gap, new StringBuilder(x.substring(xMid, xLen)).reverse().toString(), new StringBuilder(y).reverse().toString(), M, S);

            reverseArray(scoreR);

            int yMid = 0;
            int max = scoreR[0];

            for (int j = 0; j < y.length() + 1; j++) {
                if (scoreL[j] + scoreR[j] > max) {
                    max = scoreL[j] + scoreR[j];
                    yMid = j;
                }
            }

            String[] first = Hirschberg(gap, x.substring(0, xMid), y.substring(0, yMid), M, S);
            String[] second = Hirschberg(gap, x.substring(xMid, xLen), y.substring(yMid, yLen), M, S);

            return new String[]{first[0] + second[0], first[1] + second[1]};
        }
    }

    /**
     * Перевернуть массив интов (как оказалось встроенной функции нет)
     * @param arr Массив
     */
    public static void reverseArray(int[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            int tmp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = tmp;
        }

    }

    /**
     * Скоринг Нидл Вунх
     * @param gap Гэп
     * @param x Первая строка
     * @param y Вторая строка
     * @param M Маппинг
     * @param S Скоринг матрица
     * @return Массив - скоринг
     */
    public static int[] NWScore(int gap, String x, String y,
                                      HashMap<Character, Integer> M,
                                      ArrayList<ArrayList<Integer>> S
    ) {
        int[][] F = new int[2][y.length() + 1];

        for (int i = 0; i < 2; i++) {
            F[i] = new int[y.length() + 1];
            Arrays.fill(F[i], 0);
        }

        for (int j = 1; j < y.length() + 1; j++) {
            F[0][j] = F[0][j - 1] + gap;
        }

        for (int i = 1; i < x.length() + 1; i++) {
            F[1][0] = F[0][0] + gap;

            for (int j = 1; j < y.length() + 1; j++) {
                int match = F[0][j - 1] + S.get(M.get(x.charAt(i - 1))).get(M.get(y.charAt(j - 1)));
                int delete = F[0][j] + gap;
                int insert = F[1][j - 1] + gap;

                F[1][j] = Math.max(Math.max(match, insert), delete);
            }

            F[0] = F[1].clone();
        }

        int[] result = new int[y.length() + 1];
        Arrays.fill(result, 0);
        for (int j = 0; j < y.length() + 1; j++) {
            result[j] = F[1][j];
        }

        return result;
    }

    /**
     *
     * @param gap Гэп
     * @param a Первая строка
     * @param b Вторая строка
     * @param M Маппинг
     * @param S Скоринг матрица
     * @return Массив из двух алайнутых строк
     */
    public static String[] needlemanWunsch(int gap, String a, String b,
                                                    HashMap<Character, Integer> M,
                                                    ArrayList<ArrayList<Integer>> S
    ) {
        int[][] F = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i < a.length() + 1; i++) {
            F[i] = new int[b.length() + 1];
            for (int j = 0; j < b.length() + 1; j++) {
                F[i][j] = 0;
            }
        }

        for (int i = 0; i < a.length() + 1; i++) {
            F[i][0] = gap * i;
        }

        for (int j = 0; j < b.length() + 1; j++) {
            F[0][j] = gap * j;
        }

        for (int i = 1; i < a.length() + 1; i++) {
            for (int j = 1; j < b.length() + 1; j++) {
                int match = F[i - 1][j - 1] + S.get(M.get(a.charAt(i - 1))).get(M.get(b.charAt(j - 1)));
                int delete = F[i - 1][j]  + gap;
                int insert = F[i][j - 1] + gap;

                F[i][j] = Math.max(Math.max(match, insert), delete);
            }
        }

        StringBuilder alignmentA = new StringBuilder();
        StringBuilder alignmentB = new StringBuilder();

        int i = a.length();
        int j = b.length();

        while (i > 0 && j > 0) {
            int score = F[i][j];
            int scoreDiag = F[i - 1][j - 1];
            int scoreUp = F[i][j - 1];
            int scoreLeft = F[i - 1][j];

            char aChar = a.charAt(i - 1);
            char bChar = b.charAt(j - 1);

            if (score == scoreDiag + S.get(M.get(aChar)).get(M.get(bChar)) && aChar == bChar) {
                alignmentA.insert(0, aChar);
                alignmentB.insert(0, bChar);
                i--;
                j--;
            } else if (score == scoreLeft + gap) {
                alignmentA.insert(0, aChar);
                alignmentB.insert(0, '-');
                i--;
            } else {
                alignmentA.insert(0, '-');
                alignmentB.insert(0, bChar);
                j--;
            }
        }

        while (i > 0) {
            alignmentA.insert(0, a.charAt(i - 1));
            alignmentB.insert(0, '-');
            i--;
        }

        while (j > 0) {
            alignmentA.insert(0, '-');
            alignmentB.insert(0, b.charAt(j - 1));
            j--;
        }

        return new String[]{alignmentA.toString(), alignmentB.toString()};
    }

    /**
     * рабочее крестьянское заполнение хэш мапы сопоставляющей символ в таблице с позицей его в массиве
     * @param charToPosBlosum62 Мапа
     */
    public static void configureBlosum62(HashMap<Character, Integer> charToPosBlosum62) {
        charToPosBlosum62.put('A', 0);
        charToPosBlosum62.put('R', 1);
        charToPosBlosum62.put('N', 2);
        charToPosBlosum62.put('D', 3);
        charToPosBlosum62.put('C', 4);
        charToPosBlosum62.put('Q', 5);
        charToPosBlosum62.put('E', 6);
        charToPosBlosum62.put('G', 7);
        charToPosBlosum62.put('H', 8);
        charToPosBlosum62.put('I', 9);
        charToPosBlosum62.put('L', 10);
        charToPosBlosum62.put('K', 11);
        charToPosBlosum62.put('M', 12);
        charToPosBlosum62.put('F', 13);
        charToPosBlosum62.put('P', 14);
        charToPosBlosum62.put('S', 15);
        charToPosBlosum62.put('T', 16);
        charToPosBlosum62.put('W', 17);
        charToPosBlosum62.put('Y', 18);
        charToPosBlosum62.put('V', 19);
        charToPosBlosum62.put('B', 20);
        charToPosBlosum62.put('Z', 21);
        charToPosBlosum62.put('X', 22);
        charToPosBlosum62.put('-', 23);
    }

    /**
     * рабочее крестьянское заполнение хэш мапы сопоставляющей символ в таблице с позицей его в массиве
     * @param charToPosDefault Мапа
     */
    public static void configureDefault(HashMap<Character, Integer> charToPosDefault) {
        charToPosDefault.put('A', 0);
        charToPosDefault.put('B', 1);
        charToPosDefault.put('C', 2);
        charToPosDefault.put('D', 3);
        charToPosDefault.put('E', 4);
        charToPosDefault.put('F', 5);
        charToPosDefault.put('G', 6);
        charToPosDefault.put('H', 7);
        charToPosDefault.put('I', 8);
        charToPosDefault.put('J', 9);
        charToPosDefault.put('K', 10);
        charToPosDefault.put('L', 11);
        charToPosDefault.put('M', 12);
        charToPosDefault.put('N', 13);
        charToPosDefault.put('O', 14);
        charToPosDefault.put('P', 15);
        charToPosDefault.put('Q', 16);
        charToPosDefault.put('R', 17);
        charToPosDefault.put('S', 18);
        charToPosDefault.put('T', 19);
        charToPosDefault.put('U', 20);
        charToPosDefault.put('V', 21);
        charToPosDefault.put('W', 22);
        charToPosDefault.put('X', 23);
        charToPosDefault.put('Y', 24);
        charToPosDefault.put('Z', 25);
        charToPosDefault.put('-', 26);
    }

    /**
     * рабочее крестьянское заполнение хэш мапы сопоставляющей символ в таблице с позицей его в массиве
     * @param charToPosDNAfull Мапа
     */
    public static void configureDNAfull(HashMap<Character, Integer> charToPosDNAfull) {
        charToPosDNAfull.put('A', 0);
        charToPosDNAfull.put('T', 1);
        charToPosDNAfull.put('G', 2);
        charToPosDNAfull.put('C', 3);
        charToPosDNAfull.put('S', 4);
        charToPosDNAfull.put('W', 5);
        charToPosDNAfull.put('R', 6);
        charToPosDNAfull.put('Y', 7);
        charToPosDNAfull.put('K', 8);
        charToPosDNAfull.put('M', 9);
        charToPosDNAfull.put('B', 10);
        charToPosDNAfull.put('V', 11);
        charToPosDNAfull.put('H', 12);
        charToPosDNAfull.put('D', 13);
        charToPosDNAfull.put('N', 14);
    }
}

//большая часть класса уже не нужна, осталась от 0ой лабораторной
class DNA implements Comparable<DNA>{
    private String name;
    private String value;
    private String valueNew;
    private String full;
    public int rating;
    public int startingIndex;

    public DNA(String full, String value, int index) {
        String tmp1 = full.substring(full.indexOf('|') + 1);
        this.name = tmp1.substring(0, tmp1.indexOf('|'));
        this.value = value;
        this.full = full;
        this.startingIndex = index;
    }

    public void outStr() {
        System.out.println("Index:" + startingIndex);
        System.out.println("Name: " + name);
        System.out.println("DNA: " + value);
        System.out.println("Rating: " + rating);
    }

    public String getValue() {
        return value;
    }

    @Override
    public int compareTo(DNA other) {
        return Integer.compare(this.rating, other.rating);
    }
}

