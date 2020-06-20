import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Simple {
    private ArrayList<Fraction> fractions;

    public Simple(){
        fractions = new ArrayList<>();
    }

    public void addFr(int nom, int den){
        fractions.add(new Fraction(nom, den));
    }

    public Stream<Map.Entry<Integer, Long>> getPoints(Point start, Point end){
        double ax = start.getX();
        double ay = start.getY();
        double bx = end.getX();
        double by = end.getY();
        return fractions.stream()
                .filter(x -> x.getNominator() <= x.getDenominator())
                .map(x -> {
                    double nextx = bx * x.getNominator() / x.getDenominator() + ax * (x.getDenominator() - x.getNominator()) / x.getDenominator();
                    double nexty = by * x.getNominator() / x.getDenominator() + ay * (x.getDenominator() - x.getNominator()) / x.getDenominator();
                    //System.out.println("nexts: " + nextx + " " + nexty + " ; Part = " + x.getNominator() + "/" + x.getDenominator() + "; Start = " + ax + " " + ay + "; End = " + bx + " " + by);
                    return new Point(nextx, nexty);
                })
                .collect(Collectors.groupingBy(x -> {
                    if (x.getX() >= 0) {
                        if (x.getY() >= 0) return 0;
                        else return 1;
                    }
                    else if (x.getY() >= 0) return 2;
                    else return 3;
                 }, Collectors.counting()))
                .entrySet()
                .stream();
    }

    public Optional<Fraction> getMin(){
        return fractions.stream()
                .min(new Comparator<Fraction>(){
                    @Override
                    public int compare(Fraction o1, Fraction o2) {
                        return o1.getNominator() * o2.getDenominator() - o1.getDenominator() * o2.getNominator();
                    }
                });
    }
}

class Fraction{
    private int nominator;
    private int denominator;

    public Fraction(int nom, int den){
        nominator = nom;
        denominator = den;
    }

    public int getDenominator() {
        return denominator;
    }

    public int getNominator() {
        return nominator;
    }
}
