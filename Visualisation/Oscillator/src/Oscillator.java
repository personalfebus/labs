import org.math.plot.Plot2DPanel;

import javax.swing.*;
import java.util.HashMap;

public class Oscillator {
    public static void main(String[] args) {
        Plot2DPanel plotXY = new Plot2DPanel();
        double m = 15.0d;
        double a = 1.2d;
        double w = 2.1d;
        runge_kutt(0, 1.0d, 4.0d, m, 0.01d, a, w, 10000, plotXY);
        JFrame frame2 = new JFrame("a plot panel");
        frame2.setContentPane(plotXY);
        frame2.setSize(500, 500);
        frame2.setVisible(true);
    }

    public static void runge_kutt(double t, double x, double y,
                                  double m, double h, double a, double w,
                                  int n, Plot2DPanel plotXY) {

        double[] xDots = new double[n];
        double[] yDots = new double[n];
        double[] tDots = new double[n];

        for (int i = 0; i < n; i++) {
            if (x != Double.MAX_VALUE && y != Double.MAX_VALUE ) {
                xDots[i] = x;
                yDots[i] = y;
                tDots[i] = t;
            } else System.out.println("AAAAAAAAAAAA");
            HashMap<Character, Double> values = runge_kutt_step(t, x, y, m, h, a, w);
            x = values.get('x');
            y = values.get('y');
            t = tDots[i] + h;
        }

        plotXY.addLinePlot("Осцилятор Ван Дер Поля", xDots, yDots);
        //plotXY.addLinePlot("Осцилятор Ван Дер Поля", tDots, xDots);
    }

    public static HashMap<Character, Double> runge_kutt_step(double t, double x, double y,
                                                             double m, double h, double a, double w) {
        HashMap<Character, Double> result = new HashMap<>();
        //for x
        double k1 = firstLine(t, x, y, m, a, w);
        double k2 = firstLine(t + h/2, x + h/2*k1, y + h/2*k1, m, a, w);
        double k3 = firstLine(t + h/2, x + h/2*k2, y + h/2*k2, m, a, w);
        double k4 = firstLine(t + h, x + h*k3, y + h*k3, m, a, w);
        double x1 = x + h/6*(k1 + 2*k2 + 2*k3 + k4);
        if (Double.isNaN(x1) || Double.isInfinite(x1) || x1 > 10000) x1 = Double.MAX_VALUE;
        result.put('x', x1);

        //for y
        k1 = secondLine(t, x, y, m, a, w);
        k2 = secondLine(t + h/2, x + h/2*k1, y + h/2*k1, m, a, w);
        k3 = secondLine(t + h/2, x + h/2*k2, y + h/2*k2, m, a, w);
        k4 = secondLine(t + h, x + h*k3, y + h*k3, m, a, w);
        double y1 = y + h/6*(k1 + 2*k2 + 2*k3 + k4);
        if (Double.isNaN(y1) || Double.isInfinite(y1) || y1 > 10000) y1 = Double.MAX_VALUE;
        result.put('y', y1);
        return result;
    }

    private static double firstLine(double t, double x, double y, double m, double a, double w) {
        return y;
    }

    private static double secondLine(double t, double x, double y, double m, double a, double w) {
        return a*Math.sin(w*t) + m * (1 - x*x) * y - x;
    }
}
