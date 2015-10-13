package blackjackgui;

// BlackJackSplashScreenDialog.java
// Michael Hergenrader
// CSCI 200 Fall 2009
// USCID: 5048471246
// 10/11/2009

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Michael Hergenrader
 */
public class BlackJackSplashScreenDialog extends JDialog {
    
    private ImageIcon splashImageIcon;
    private Image splashImage; // image to display in the dialog
    
    /**
     * constructor - locates the image, creates the dialog frame, and initializes it to the panel and image, pauses for a little, then closes the dialog
     */
    public BlackJackSplashScreenDialog() {
        splashImageIcon = new ImageIcon("C:/Users/Michael/Desktop/images/blackjack.jpg"); // should use a try catch block
        
        splashImage = splashImageIcon.getImage();
        add(new SplashScreenPanel());
        
        addWindowListener(new ClosingListener());
        
        setSize(500,450);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Welcome to Blackjack!");
        setResizable(false);
        setVisible(true);
        
        try { // creates a delay in the program - move this inside handleIntroPanel()
            Thread.sleep(1700); // in milliseconds
        }
        catch (InterruptedException ie) {
            System.out.println(ie.getMessage());
        }

        cleanup(); // can dispose of it entirely?/quit?
    }
    
    /**
     * inner class: SplashScreenPanel extends JPanel and redefines paintComponent so it can define how to draw the image on the panel
     */    
    class SplashScreenPanel extends JPanel {
    
        /**
         * paintComponent - defines how to draw this panel - first draws like normal, calling super method, then draws the splashscreen
         * @param g - the graphics context for this component, passed in to use for drawImage to draw to this context/component
         */
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if(splashImage != null) {
                g.drawImage(splashImage,0,0,getWidth(),getHeight(),this);
            }
            else {
                System.out.println("bad splashscreen image"); // just for testing
            }
        }
    }
    
    /**
     * cleanup - disposes of the dialog frame, setting its visibility to false
     */
    private void cleanup() {
        setVisible(false);
    }    
}
