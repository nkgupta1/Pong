   import javax.swing.*;
   import java.awt.*;
   import java.awt.event.*;
   import java.awt.image.*;
   import java.util.Scanner;
   
   public class PongPanel extends JPanel
   {
      private static final int FRAMEX = 800;
      private static final int FRAMEY = 500;
      private static final Color BACKGROUND = new Color(204, 204, 204);
      private static final Color BALL_COLOR = Color.BLACK;
      private static final Color PRIZE_COLOR = Color.RED;
      private static final Color BUMPER_COLOR = Color.BLUE;
      private static final double BALL_DIAM = 50;
      private static final double PRIZE_DIAM = 25;
      private static final int BUMPER_X_WIDTH = 20;
      private static final int BUMPER_Y_WIDTH = 125;
      private static final int BUMPER_SPEED = 20;
      private static final int SCORE_SCALE = 35;
   
      private BufferedImage myImage;
      private Graphics myBuffer;
      private Ball ball;
      private Bumper bumperL;
      private Bumper bumperR;
      private int pointsP1;
      private int pointsP2;
      private Timer timer;
      private ImageIcon score = new ImageIcon("+1.gif");
      private ImageIcon explosion = new ImageIcon("Explosion.png");
      private int time1; //to get the score to apear
      private int time2;
      private int explosionScale;
      private int explosionY;
      private int limit;
      
      public PongPanel()
      {
         myImage =  new BufferedImage(FRAMEX, FRAMEY, BufferedImage.TYPE_INT_RGB);
         myBuffer = myImage.getGraphics();
         
         myBuffer.setColor(BACKGROUND);
         myBuffer.fillRect(0, 0, FRAMEX,FRAMEY);
         	
      	//ask when to end the game
         String inputValue = JOptionPane.showInputDialog("Play to...");
         if(inputValue==null)
         {
            System.exit(0);
         }
         Scanner s = new Scanner(inputValue);
         limit =  s.nextInt();
      	
         // create ball and set dx and dy
         ball = new Ball(FRAMEX/2, FRAMEY/2, BALL_DIAM, BALL_COLOR);
         ball.setdx(8);
         ball.setdy(7);
      
                
         // create bumpers
         bumperL = new Bumper(0, 0, BUMPER_X_WIDTH, BUMPER_Y_WIDTH, BUMPER_COLOR);
         bumperR = new Bumper(FRAMEX-BUMPER_X_WIDTH, FRAMEY-BUMPER_Y_WIDTH, BUMPER_X_WIDTH, BUMPER_Y_WIDTH, BUMPER_COLOR);
      
      	      
         pointsP1 = 0;
         pointsP2 = 0;
         timer = new Timer(5, new Listener());
         timer.start();
         addKeyListener(new Key());
         setFocusable(true);
      }
      private class Key extends KeyAdapter                                         
      {
         public void keyPressed (KeyEvent e)
         {
         	//moving the bumpers
            if(e.getKeyCode() == KeyEvent.VK_UP&&bumperR.getY()>0)
               bumperR.setY(bumperR.getY()-BUMPER_SPEED);
            if(e.getKeyCode() == KeyEvent.VK_DOWN&&bumperR.getY()<FRAMEY-bumperR.getYWidth())
               bumperR.setY(bumperR.getY()+BUMPER_SPEED);
            if(e.getKeyCode() == KeyEvent.VK_W&&bumperL.getY()>0)
               bumperL.setY(bumperL.getY()-BUMPER_SPEED);
            if(e.getKeyCode() == KeyEvent.VK_S&&bumperL.getY()<FRAMEY-bumperL.getYWidth())
               bumperL.setY(bumperL.getY()+BUMPER_SPEED);
            if(e.getKeyCode() == KeyEvent.VK_R)
            {
               ball.setX(FRAMEX/2);
               ball.setY(FRAMEY/2);
               ball.setdx(Math.random() * 24 - 6);          // to move vertically
               ball.setdy(Math.random() * 24 - 6);          // to move sideways
            }
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            {
               try
               {
                  Thread.sleep(200);
                  System.exit(0);
               }
                  catch(InterruptedException a)
                  { 
                                  
                  }
            }
         }
      }
      public void paintComponent(Graphics g)
      {
         g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
      }
          
      private class Listener implements ActionListener
      {
         public void actionPerformed(ActionEvent e)
         {
            // clear buffer and move ball
            myBuffer.setColor(BACKGROUND);
            myBuffer.fillRect(0,0,FRAMEX,FRAMEY); 
            ball.move(FRAMEX, FRAMEY);         
         	
            // check for collisions
            BumperCollision.collide(bumperL, ball);
            BumperCollision.collide(bumperR, ball);
         	         	
         	//for scoring
            if(time1>0) //left side graphics
            {
               myBuffer.drawImage(score.getImage(), FRAMEX/2 + 200, 5, SCORE_SCALE, SCORE_SCALE, null);
               myBuffer.drawImage(explosion.getImage(), 0, explosionY-explosionScale/2, explosionScale, explosionScale, null);
               explosionScale+=2;
               time1--;
            }
            else if(time2>0) //Right side graphics
            {
               myBuffer.drawImage(score.getImage(), FRAMEX/2 - 200, 5, SCORE_SCALE, SCORE_SCALE, null);
               myBuffer.drawImage(explosion.getImage(), FRAMEX - explosionScale, explosionY-explosionScale/2, explosionScale, explosionScale, null);
               explosionScale+=2;
               time2--;
            }
         	
         	//check for points
            if(ball.getX()==0+ball.getRadius())
            {
               pointsP2++;
               myBuffer.drawImage(score.getImage(), FRAMEX/2 + 200, 5, SCORE_SCALE, SCORE_SCALE, null);
               explosionY = (int)ball.getY();
               explosionScale=0;
               myBuffer.drawImage(explosion.getImage(), 0, explosionY, explosionScale, explosionScale, null);
               time1=30;
            }
            else if(ball.getX()==FRAMEX-ball.getRadius())
            {
               pointsP1++;
               myBuffer.drawImage(score.getImage(), FRAMEX/2 - 200, 5, SCORE_SCALE, SCORE_SCALE, null);
               explosionY = (int)ball.getY();
               explosionScale=0;
               myBuffer.drawImage(explosion.getImage(), FRAMEX, explosionY, explosionScale, explosionScale, null);
               time2=30;
            
            }
         	
         
            // draw ball, bumpers
            ball.draw(myBuffer);
            bumperL.draw(myBuffer);
            bumperR.draw(myBuffer);
                    
            // update points
            myBuffer.setColor(Color.black);
            myBuffer.setFont(new Font("Monospaced", Font.BOLD, 24));
            myBuffer.setColor(Color.RED);
            myBuffer.drawString("P1: " + pointsP1, FRAMEX/2 - 150, 25);
            myBuffer.setColor(Color.BLUE);
            myBuffer.drawString("P2: " + pointsP2, FRAMEX/2 + 100, 25);
            myBuffer.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 36));
            myBuffer.setColor(new Color((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256)));
            myBuffer.drawString("PONG", FRAMEX/2 - 50, 75);
         	
            if(pointsP1>=limit) //exit condition
            {
               try
               {
                  Thread.sleep(1000);
                  System.exit(0);
               }
                  catch(InterruptedException a)
                  { 
                                  
                  }
            }
            else if(pointsP2>=limit)
            {
               try
               {
                  Thread.sleep(1000);
                  System.exit(0);
               }
                  catch(InterruptedException a)
                  { 
                                  
                  }
            }
            
            repaint();
         }
      } 
      
   	// checks to see if the ball & prize collide
   	// if so, increments hits & relocates prize	
      public void collide(Ball b, Polkadot p)
      {
         // find distance between ball & prize centers
         double dist = distance(b.getX(), b.getY(), p.getX(), p.getY());
         
         if(dist < p.getRadius() + b.getRadius())
         {
         //            hits++;
            p.jump(FRAMEX,FRAMEY);    	// relocate prize
         }
      }
   		
      private double distance(double x1, double y1, double x2, double y2)
      {
         return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
      }
   }