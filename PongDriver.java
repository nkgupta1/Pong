
   import javax.swing.JFrame;
   public class PongDriver
   {
      public static void main(String[] args)
      { 
         JFrame frame = new JFrame("Pong Project");
         frame.setSize(800, 500);    //makes the mouse location correct
         frame.setLocation(0, 0);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         PongPanel p = new PongPanel();
         frame.setContentPane(p);
         p.requestFocus();
         frame.setVisible(true);
         
      }
   }