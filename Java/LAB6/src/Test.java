import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args){
//        Map<Integer, String> mp1 = new HashMap<>();
//
//        mp1.put(0, "ZERO");
//        mp1.put(1, "ONE");
//        mp1.put(2, "TWO");
//
//        mp1.entrySet()
//                .stream()
//                .map(x -> x.getValue())
//                .reduce((x, y) -> x + ", " + y).ifPresent(System.out::println);
        try {
            FileReader reader = new FileReader("D:\\_IT\\IdeaProjects\\LAB6\\src\\tst3.txt");
            Simple smpl = new Simple();
            //Scanner in = new Scanner(System.in);
            Scanner in = new Scanner(reader);
            int n = in.nextInt();
            for (int i = 0; i < n; i++){
                smpl.addFr(in.nextInt(), in.nextInt());
            }
            double x1 = in.nextDouble();
            double y1 = in.nextDouble();
            double x2 = in.nextDouble();
            double y2 = in.nextDouble();

            Point start = new Point(x1, y1);
            Point end = new Point(x2, y2);

            Stream<Map.Entry<Integer, Long>> points = smpl.getPoints(start, end);
            points.map(x -> {
                StringBuilder s = new StringBuilder();
                s.append("In ");
                s.append(x.getKey());
                s.append(" quoter ");
                s.append(x.getValue());
                s.append(" elements\n");
                return s;
            })
                    .reduce((x, y) -> x.append(y)).ifPresent(System.out::println);
            Optional<Fraction> mn = smpl.getMin();
            mn.ifPresent(x -> System.out.println(x.getNominator() + "/" + x.getDenominator()));
        }
        catch (FileNotFoundException er){
            System.out.println("Bad");
        }
    }
}
