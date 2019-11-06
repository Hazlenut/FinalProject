import java.awt.*;
import javax.swing.*;

public class Test2 {
   
   public static void main(String[] arg) {
      
      JFrame frame = new JFrame("JFrame");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      frame.setPreferredSize(new Dimension((int)(.5*screen.getWidth()), (int)(.7*screen.getHeight())));
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
      
   }
   
}