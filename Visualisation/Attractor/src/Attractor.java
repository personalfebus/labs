import java.awt.*;
import java.util.HashMap;

import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.math.plot.Plot2DPanel;
import org.math.plot.Plot3DPanel;

import javax.swing.*;

public class Attractor {
    public static void main(String[] args) {
        Plot2DPanel plotXY = new Plot2DPanel();
        for (double b = 0.0d; b < 2; b += 0.1) {
            runge_kutt(0, 0, 0, 0, 0.2d, b, 5.7d, 0.01d, 100, plotXY);
        }
        JFrame frame2 = new JFrame("a plot panel");
        frame2.setContentPane(plotXY);
        frame2.setVisible(true);
    }

    //Аттрактор Рёсслера
    //x' = -y - z
    //y' = x + ay
    //z' = b + z(x - c)

    //a = 0.2, b = 0.2, c = 5.7
    //a = 0.1, b = 0.1 c = 14
    public static double firstLine(double t, double x, double y, double z, double a, double b, double c) {
        return -y - z;
    }

    public static double secondLine(double t, double x, double y, double z, double a, double b, double c) {
        return x + a*y;
    }

    public static double thirdLine(double t, double x, double y, double z, double a, double b, double c) {
        return b + z*(x - c);
    }

    public static void runge_kutt(double t, double x, double y, double z,
                                  double a, double b, double c,
                                  double h, int n, Plot2DPanel plotXY) {

        double[] xDots = new double[n];
        double[] yDots = new double[n];
        double[] zDots = new double[n];
        double[] bDots = new double[n];

        for (int i = 0; i < n; i++) {
            if (x != Double.MAX_VALUE && y != Double.MAX_VALUE && z != Double.MAX_VALUE) {
                xDots[i] = x;
                yDots[i] = y;
                zDots[i] = z;
                bDots[i] = b;
            } else i--;
            HashMap<Character, Double> values = runge_kutt_step(t, x, y, z, a, b, c, h);
            x = values.get('x');
            y = values.get('y');
            z = values.get('z');
        }

        plotXY.addScatterPlot("Проекция Аттрактора Рёсслера XY", bDots, zDots);

//        Plot3DPanel plot = new Plot3DPanel();
//        //plot.addLinePlot("Аттрактор Рёсслера", Color.GREEN, xDots, yDots, zDots);
//        plot.addScatterPlot("Аттрактор Рёсслера", xDots, yDots, zDots);
//        JFrame frame1 = new JFrame("a plot panel");
//        frame1.setContentPane(plot);
//        frame1.setVisible(true);
//
//        Plot2DPanel plotXY = new Plot2DPanel();
//        plotXY.addScatterPlot("Проекция Аттрактора Рёсслера XY", xDots, yDots);
//        JFrame frame2 = new JFrame("a plot panel");
//        frame2.setContentPane(plotXY);
//        frame2.setVisible(true);
    }

    public static HashMap<Character, Double> runge_kutt_step(double t, double x, double y, double z,
                                                             double a, double b, double c,
                                                             double h
    ) {
        HashMap<Character, Double> result = new HashMap<>();
        //for x
        double k1 = firstLine(t, x, y, z, a, b, c);
        double k2 = firstLine(t + h/2, x + h/2*k1, y + h/2*k1, z + h/2*k1, a, b, c);
        double k3 = firstLine(t + h/2, x + h/2*k2, y + h/2*k2, z + h/2*k2, a, b, c);
        double k4 = firstLine(t + h, x + h*k3, y + h*k3, z + h*k3, a, b, c);
        double x1 = x + h/6*(k1 + 2*k2 + 2*k3 + k4);
        if (Double.isNaN(x1) || Double.isInfinite(x1) || x1 > 10000) x1 = Double.MAX_VALUE;
        result.put('x', x1);

        //for y
        k1 = secondLine(t, x, y, z, a, b, c);
        k2 = secondLine(t + h/2, x + h/2*k1, y + h/2*k1, z + h/2*k1, a, b, c);
        k3 = secondLine(t + h/2, x + h/2*k2, y + h/2*k2, z + h/2*k2, a, b, c);
        k4 = secondLine(t + h, x + h*k3, y + h*k3, z + h*k3, a, b, c);
        double y1 = y + h/6*(k1 + 2*k2 + 2*k3 + k4);
        if (Double.isNaN(y1) || Double.isInfinite(y1) || y1 > 10000) y1 = Double.MAX_VALUE;
        result.put('y', y1);

        //for z
        k1 = thirdLine(t, x, y, z, a, b, c);
        k2 = thirdLine(t + h/2, x + h/2*k1, y + h/2*k1, z + h/2*k1, a, b, c);
        k3 = thirdLine(t + h/2, x + h/2*k2, y + h/2*k2, z + h/2*k2, a, b, c);
        k4 = thirdLine(t + h, x + h*k3, y + h*k3, z + h*k3, a, b, c);
        double z1 = z + h/6*(k1 + 2*k2 + 2*k3 + k4);
        if (Double.isNaN(z1) || Double.isInfinite(z1) || z1 > 10000) z1 = Double.MAX_VALUE;
        result.put('z', z1);
        return result;
    }
}
