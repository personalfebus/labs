import javax.swing.*;
import java.awt.*;

public class CanvasPanel extends JPanel {
    private int diameter = 20;
    private int MAX_X;
    private int MAX_Y;
    private Planet mainPlanet;

    private class Planet{
        final private Color clr;
        final private int centerX;
        final private int centerY;
        final private boolean isFilled;

        public Planet(Color color, int x, int y, boolean fill){
            centerX = x;
            centerY = y;
            clr = color;
            isFilled = fill;
        }

        public int getCenterX() {
            return centerX;
        }

        public int getCenterY() {
            return centerY;
        }

        public Color getClr() {
            return clr;
        }

        public boolean isFilled() {
            return isFilled;
        }
    }

    private int fibGen(int i){
        if (i == 0) return 1;
        if (i == 1) return 1;
        else return fibGen(i - 1) + fibGen(i - 2);
    }

    public void setDimensions(int x, int y){
        MAX_X = x;
        MAX_Y = y;
        mainPlanet = new Planet(Color.RED, x / 3, y / 3, true);
    }

    public void setDiameter(int d){
        diameter = d;
        repaint();
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);

//        int r = (int)(Math.random() * 255);
//        int green = (int)(Math.random() * 255);
//        int b = (int)(Math.random() * 255);
//        float[] clrss = Color.RGBtoHSB(r, green, b, null);
//        g.setColor(Color.getHSBColor(clrss[0], clrss[1], clrss[2]));

        int center_x = mainPlanet.getCenterX();
        int centerY = mainPlanet.getCenterY();

        for (;;){
            int curr = fibGen(i);
            if (curr < diameter / 3) continue;
            //if (centerX - curr - diameter / 2 - diameter / 6 < 0) break;
            //if (centerX + curr + diameter / 2 + diameter / 6 > MAX_X) break;
            int next_orbit_x = center_x - curr - diameter / 2;
            int next_orbit_y = centerY - curr - diameter / 2;
            int next_orbit_dia = (diameter + curr) * 2;

            g.setColor(Color.BLUE);
            g.drawOval(next_orbit_x, next_orbit_y, next_orbit_dia, next_orbit_dia);
            //int xtmp = next_orbit_x - diameter / 6;
            //int ytmp = centerY + diameter / 3;
            int next_set_x;
            int next_set_y;
            int next_set_dia = diameter / 3;
            //int angle = i * 2;
            //int next_set_x = next_orbit_x + (int)(next_orbit_dia * Math.random()) - diameter / 6;
            //int next_set_y = (int)(Math.sqrt(next_orbit_dia * next_orbit_dia / 4 - next_set_x * next_set_x)) + diameter / 3;

            //Math.PI / 10 * angle
            //System.out.println(Math.cos(3.14));
            //int next_set_x = (int)(Math.cos( - Math.PI / 6) * xtmp - Math.sin( - Math.PI / 6) * ytmp);
            //int next_set_y = (int)(Math.sin( - Math.PI / 6) * xtmp + Math.cos( - Math.PI / 6) * ytmp);
            //System.out.println(next_set_x + " " + next_set_y);
            if (i % 5 == 0){
                next_set_x = center_x + diameter / 3;
                next_set_y = next_orbit_y - diameter / 6;
            }
            else if (i % 5 == 1){
                int x = (int)(center_x - next_orbit_dia / Math.sqrt(8));
                next_set_x =  x + diameter / 3;
                next_set_y = x + diameter / 3;
            }
            else if (i % 5 == 2){
                next_set_x = center_x + diameter / 3;
                next_set_y = next_orbit_y + next_orbit_dia - diameter / 6;
            }
            else if (i % 5 == 3){
                int x = (int)(center_x + next_orbit_dia / Math.sqrt(8));
                next_set_x = x + diameter / 3;
                next_set_y = x + diameter / 3;
            }
            else {
                next_set_x = next_orbit_x + next_orbit_dia - diameter / 6;
                next_set_y = centerY + diameter / 3;
            }
            if ((next_set_x < 0) ||
                    (next_set_y < 0) ||
                    (next_set_y + next_set_dia * 2 > MAX_Y) ||
                    (next_set_x + next_set_dia * 2 > MAX_X)) break;
            g.setColor(Color.GREEN);
            g.fillOval(next_set_x, next_set_y, next_set_dia, next_set_dia);
        }

        g.setColor(Color.RED);
        g.fillOval(center_x, centerY, diameter, diameter);
    }
}
