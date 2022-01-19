import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Comparator;

public class Lab {
    public static void main(String[] args) throws Exception {
        ArrayList<DNA> array = new ArrayList<>();
        FileInputStream stream = new FileInputStream("D:\\_IT\\eclipse-workspace\\bio_lab0\\uniprot_sprot.fasta");
        //uniprot_sprot.fasta
        Scanner scan = new Scanner(stream);
        String str = scan.nextLine();
        StringBuilder builder = new StringBuilder();
        int cnt = 0;
        int template = 55;
        while (scan.hasNextLine()) {
            if (cnt % 10000 == 0) System.out.println(cnt);
            cnt++;
            String n = scan.nextLine();
            if (n.charAt(0) == '>') {
                if (str.contains("P05979")) {
                    System.out.println("GOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOD");
                    template = array.size();
                }
                array.add(new DNA(str, builder.toString(), array.size()));
                str = n;
                builder = new StringBuilder();
                continue;
            }
            builder.append(n);
        }
        scan.close();

        HashMap<Character, Integer> a;

        final int outputSize = 20;

        for (int j = 0; j < array.size(); j++) {
            array.get(template).rate(array.get(j));
            if (j % 1000 == 0) System.out.println(j);
        }

        array.sort(Comparator.comparingInt(d -> d.rating));


        for (int j = array.size() - 1; j > array.size() - outputSize - 1; j--) {
            int top = array.size() - j;
            System.out.println(top + ") ");
            array.get(j).outStr();
        }
    }
}

//Эталон - 55 номер

class DNA implements Comparable<DNA>{
    private String name;
    private String value;
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

    public void rate(DNA other) {
        //SUMM{commonLen^(2)} for all commonStrs

        String str1 = value;
        String str2 = other.value;

        if (str1.length() <= str2.length()) {
            findSubStrs(str2, str1, other);
        } else {
            findSubStrs(str1, str2, other);
        }
    }

    private void findSubStrs(String str1, String str2, DNA other) {
        for (int i = str2.length() - 1; i > 1; i--) {
            for (int j = 0; j < str2.length() - i; j++) {
                String tmp1 = str2.substring(j, j + i + 1);
                if (str1.contains(tmp1)) {
                    str2 = str2.substring(0, j) + str2.substring(j + i + 1);
                    other.rating += tmp1.length() * tmp1.length();
                }
            }
        }
    }

    @Override
    public int compareTo(DNA other) {
        return Integer.compare(this.rating, other.rating);
    }
}
