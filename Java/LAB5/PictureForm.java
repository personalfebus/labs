import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class PictureForm {
    private JPanel mainPanel;
    private JTextField areaField;
    private JSpinner diameterSpinner;
    private CanvasPanel canvasPanel;

    public PictureForm(){
        Dimension jj = mainPanel.getPreferredSize();
        canvasPanel.setDimensions(jj.width, jj.height);
//        Dimension jk = canvasPanel.getPreferredSize();
//        System.out.println(jk.width);
        diameterSpinner.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                int diameter = (int)diameterSpinner.getValue();
                canvasPanel.setDiameter(diameter);
                double area = Math.PI * diameter * diameter / 4;
                areaField.setText(String.format("%.2f", area));
            }
        });

        diameterSpinner.setValue(100);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Окружность");
        frame.setContentPane(new PictureForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
