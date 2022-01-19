import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Алгоритм Нидл-Вунша с аффинным штрафом за пропуски
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
        int gapOpen = -10;
        int gapExtend = -1;
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
                case "-gO": {
                    gapOpen = Integer.parseInt(args[k + 1]);
                    break;
                }
                case "-gE": {
                    gapExtend = Integer.parseInt(args[k + 1]);
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

        String[] result = needlemanWunschAffineGap(gapOpen, gapExtend, array.get(0).getValue(), array.get(1).getValue(), M, S);
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
     * алгоритм Нидл-Вунша с аффинным штрафом за пропуски
     * @param gapOpen Открывающий гэп
     * @param gapExtend Продлевающий гэп
     * @param a Первая строка
     * @param b Вторая строка
     * @param M - Маппинг
     * @param S - Скоринг таблица
     * @return Пара за алайненных строк
     */
    public static String[] needlemanWunschAffineGap(int gapOpen, int gapExtend, String a, String b,
                                                    HashMap<Character, Integer> M,
                                                    ArrayList<ArrayList<Integer>> S
    ) {
        int n = a.length();
        int m = b.length();

        int[][] d = new int[n + 1][m + 1];
        int[][] aa = new int[n + 1][m + 1];
        int[][] bb = new int[n + 1][m + 1];
        char[][] t = new char[n + 1][m + 1];

        for (int i = 0; i < n + 1; i++) {
            d[i] = new int[m + 1];
            Arrays.fill(d[i], 0);
            aa[i] = new int[m + 1];
            Arrays.fill(aa[i], 0);
            bb[i] = new int[m + 1];
            Arrays.fill(bb[i], 0);
            t[i] = new char[m + 1];
            Arrays.fill(t[i], '\0');
        }

        d[0][0] = 0;
        d[0][1] = gapOpen + gapExtend;
        d[1][0] = gapOpen + gapExtend;
        for (int i = 2; i < d.length; i++) {
            d[i][0] = d[i - 1][0] + gapExtend;
        }

        for (int j = 2; j < d[0].length; j++) {
            d[0][j] = d[0][j - 1] + gapExtend;
        }

        aa[0][1] = gapOpen + gapExtend;
        for (int i = 0; i < aa.length; i++) {
            aa[i][0] = -1000000000;
        }

        for (int i = 2; i < aa[0].length; i++) {
            aa[0][i] = aa[0][i - 1] + gapExtend;
        }

        bb[0][1] = gapOpen + gapExtend;
        for (int i = 0; i < bb.length; i++) {
            bb[i][0] = -1000000000;
        }

        for (int i = 2; i < bb[0].length; i++) {
            bb[0][i] = bb[0][i - 1] + gapExtend;
        }

        for (int i = 0; i < t.length; i++) {
            t[i][0] = 'b';
        }

        for (int i = 0; i < t[0].length; i++) {
            t[0][i] = 'a';
            t[0][0] = '-';
        }

        for (int i = 1; i < d.length; i++) {
            for (int j = 1; j < d[0].length; j++) {
                bb[i][j] = Math.max(bb[i - 1][j] + gapExtend, d[i - 1][j] + gapExtend + gapOpen);
                aa[i][j] = Math.max(aa[i][j - 1] + gapExtend, d[i][j - 1] + gapExtend + gapOpen);

                int score = S.get(M.get(a.charAt(i - 1))).get(M.get(b.charAt(j - 1)));

                d[i][j] = Math.max(Math.max(d[i - 1][j - 1] + score, aa[i][j]), bb[i][j]);

                if (d[i][j] == d[i - 1][j - 1] + score) {
                    t[i][j] = 'd';
                } else if (d[i][j] == aa[i][j]) {
                    t[i][j] = 'a';
                } else if (d[i][j] == bb[i][j]) {
                    t[i][j] = 'b';
                }
            }
        }

        StringBuilder z = new StringBuilder();
        StringBuilder w = new StringBuilder();
        int i = n;
        int j = m;

        while (i > 0 || j > 0) {
            if (t[i][j] == 'd') {
                z.append(a.charAt(i - 1));
                w.append(b.charAt(j - 1));
                i--;
                j--;
            } else if (t[i][j] == 'b') {
                z.append(a.charAt(i - 1));
                w.append('-');
                i--;
            } else if (t[i][j] == 'a') {
                z.append('-');
                w.append(b.charAt(j - 1));
                j--;
            }
        }

        z.reverse();
        w.reverse();

        return new String[]{z.toString(), w.toString()};
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
