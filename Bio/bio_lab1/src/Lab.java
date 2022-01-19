import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Lab {
    public static void main(String[] args) throws IOException {
        //Последовательности чистаются только из 1ого файла формата fasta
        //Закомментирован интефейс взаимодействия с программой через консоль вместо ключей коммандной строки
        Scanner in = new Scanner(System.in);
//        System.out.println("Enter gap value");
//      final int gap = in.nextInt();
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
//        System.out.println("Enter (english)type value: аминокислота=1, нуклиотид=2, дефолт=3");
//        final int type = in.nextInt();
        ArrayList<DNA> array = new ArrayList<>();
//        FileInputStream stream = new FileInputStream("D:\\_IT\\IdeaProjects\\bio_lab1\\fasta.txt");
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

        HashMap<Character, Integer> charToPosBlosum62 = new HashMap<>();
        final int blosumSize = 24;
        Lab.configureBlosum62(charToPosBlosum62);
        ArrayList<ArrayList<Integer>> blosum62 = new ArrayList<>();
        FileInputStream blosumStream = new FileInputStream("D:\\_IT\\IdeaProjects\\bio_lab1\\blosum62.txt");
        Scanner blosumScanner = new Scanner(blosumStream);
        for (int i = 0; i < blosumSize; i++) {
            blosum62.add(new ArrayList<>());
            for (int j = 0; j < blosumSize; j++) {
                blosum62.get(i).add(blosumScanner.nextInt());
            }
        }
        //System.out.println("Blosum62 configured");

        HashMap<Character, Integer> charToPosDNAfull = new HashMap<>();
        final int DNAfullSize = 15;
        Lab.configureDNAfull(charToPosDNAfull);
        ArrayList<ArrayList<Integer>> DNAfull = new ArrayList<>();
        FileInputStream DNAfullStream = new FileInputStream("D:\\_IT\\IdeaProjects\\bio_lab1\\DNAfull.txt");
        Scanner DNAfullScanner = new Scanner(DNAfullStream);
        for (int i = 0; i < DNAfullSize; i++) {
            DNAfull.add(new ArrayList<>());
            for (int j = 0; j < DNAfullSize; j++) {
                DNAfull.get(i).add(DNAfullScanner.nextInt());
            }
        }
        //System.out.println("DNAfull configured");

        //System.out.println("Size=" + array.size());
        ArrayList<ArrayList<Integer>> D = new ArrayList<>();
        ArrayList<ArrayList<Integer>> Ptr = new ArrayList<>();
        StringBuilder str1 = new StringBuilder(array.get(0).getValue());
        StringBuilder str2 = new StringBuilder(array.get(1).getValue());
        for (int i = 0; i < str1.length() + 1; i++) {
            D.add(new ArrayList<>());
            Ptr.add(new ArrayList<>());
            for (int j = 0; j < str2.length() + 1; j++) {
                if (i == 0) {
                    D.get(i).add(gap*j);
                    if (j == 0) {
                        Ptr.get(i).add(-1);
                    } else {
                        Ptr.get(i).add(0);
                    }
                } else if (j == 0) {
                    D.get(i).add(gap*i);
                    Ptr.get(i).add(2);
                } else {
                    D.get(i).add(0);
                    Ptr.get(i).add(-1);
                }
            }
        }

        //a b c e->d = 0    e->c = 3    e->h = 6
        //d e f e->a = 1    e->f = 4    e->g = 7
        //g h i e->b = 2    e->i = 5    в алгоритме возможны только 0, 1, 2 тк идем в обратном направлении

        for (int i = 1; i < str1.length() + 1; i++) {
            for (int j = 1; j < str2.length() + 1; j++) {
                int max = Integer.MIN_VALUE;
                int route1 = 0, route2 = 0, route3 = 0;

                //свитч для разных входных данных (одни юзают блосом62, другие ДНКфулл, остальные дефолт)
                switch (type) {
                    case 1: {
                        route1 = D.get(i - 1).get(j - 1) + blosum62.get(charToPosBlosum62.get(str1.charAt(i - 1)))
                                .get(charToPosBlosum62.get(str2.charAt(j - 1)));
                        route2 = D.get(i - 1).get(j) + gap;
                        route3 = D.get(i).get(j - 1) + gap;
                        break;
                    }
                    case 2: {
                        route1 = D.get(i - 1).get(j - 1) + DNAfull.get(charToPosDNAfull.get(str1.charAt(i - 1)))
                                .get(charToPosDNAfull.get(str2.charAt(j - 1)));
                        route2 = D.get(i - 1).get(j) + gap;
                        route3 = D.get(i).get(j - 1) + gap;
                        break;
                    }
                    case 3: {
                        if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                            route1 = D.get(i - 1).get(j - 1) + 1;
                        } else route1 = D.get(i - 1).get(j - 1) - 1;
                        route2 = D.get(i - 1).get(j) + gap;
                        route3 = D.get(i).get(j - 1) + gap;
                        break;
                    }
                    default: {
                        System.out.println("Entered invalid type (Use english letters), default matrix will be used");
                        if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                            route1 = D.get(i - 1).get(j - 1) + 1;
                        } else route1 = D.get(i - 1).get(j - 1) - 1;
                        route2 = D.get(i - 1).get(j) + gap;
                        route3 = D.get(i).get(j - 1) + gap;
                    }
                }


                if (max < route1) {
                    max = route1;
                    Ptr.get(i).set(j, 1);
                }
                if (max < route2) {
                    max = route2;
                    Ptr.get(i).set(j, 2);
                }
                if (max < route3) {
                    max = route3;
                    Ptr.get(i).set(j, 0);
                }
                D.get(i).set(j, max);
            }
        }

        if (outputToFile) {
            PrintStream printStream = new PrintStream(outputFileName);
            System.setOut(printStream);
        }

        //Result Output

        System.out.println("D:");
        for (int i = 0; i < D.size(); i++) {
            for (int j = 0; j < D.get(i).size(); j++) {
                if (D.get(i).get(j) < 0) System.out.print(" " + D.get(i).get(j));
                else System.out.print("  " + D.get(i).get(j));
            }
            System.out.println();
        }

        System.out.println("Ptr:");
        for (int i = 0; i < Ptr.size(); i++) {
            for (int j = 0; j < Ptr.get(i).size(); j++) {
                if (Ptr.get(i).get(j) < 0) System.out.print(" " + Ptr.get(i).get(j));
                else System.out.print("  " + Ptr.get(i).get(j));
            }
            System.out.println();
        }

        StringBuilder mod1 = new StringBuilder();
        StringBuilder mod2 = new StringBuilder();
        int curPosI = D.size() - 1;
        int curPosJ = D.get(0).size() - 1;
        int curDir = Ptr.get(curPosI).get(curPosJ);

        System.out.println("------------------");
        while (curDir > -1) {
            //System.out.println(curDir);
            switch (curDir) {
                case 0: {
                    mod2.append(str2.charAt(curPosJ - 1));
                    mod1.append('-');
                    curPosJ--;
                    break;
                }
                case 1: {
                    mod1.append(str1.charAt(curPosI - 1));
                    mod2.append(str2.charAt(curPosJ - 1));
                    curPosI--;
                    curPosJ--;
                    break;
                }
                case 2: {
                    mod2.append('-');
                    mod1.append(str1.charAt(curPosI - 1));
                    curPosI--;
                    break;
                }
                default: System.out.println("???");
            }
            curDir = Ptr.get(curPosI).get(curPosJ);
        }

        System.out.println("Old strings:");
        System.out.println(str1);
        System.out.println(str2);
        System.out.println("New strings:");
        System.out.println(mod1.reverse());
        System.out.println(mod2.reverse());
        System.out.println("Score: " + D.get(str1.length()).get(str2.length()));

        blosumStream.close();
        DNAfullStream.close();
        stream.close();
        if (outputToFile) {
            System.out.close();
        }
    }

    public static void configureBlosum62(HashMap<Character, Integer> charToPosBlosum62) {
        //рабочее крестьянское заполнение хэш мапы сопоставляющей символ в таблице с позицей его в массиве
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

    public static void configureDNAfull(HashMap<Character, Integer> charToPosDNAfull) {
        //рабочее крестьянское заполнение хэш мапы сопоставляющей символ в таблице с позицей его в массиве
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
