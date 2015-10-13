package blackjackgui;

// ClosingListener.java
// Michael Hergenrader
// CSCI 200 Fall 2009
// USCID: 5048471246
// 10/11/2009

import java.awt.event.*;

/**
 *
 * @author Michael Hergenrader
 */
public class ClosingListener implements WindowListener {
    
    // this class is only used for intro/setup dialog boxes in the game; if a user hits close, don't go on to the next
    // dialog box/frame, as bad information will be passed; instead, do as the user intends and exit the program
    // only one method is actually implemented, but since WindowListener is an interface, all of them must be included
    
    public void windowActivated(WindowEvent e) {
    }        
    public void windowClosed(WindowEvent e) {
        //System.exit(0);
    }
    
    /**
     * windowClosing - implements the WindowListener interface method to respond when the "x" in the upper right of a window is hit
     * @param e - a WindowEvent that is fired by the JVM when this occurs
     */
    public void windowClosing(WindowEvent e) { // if a dialog box is closed, exit the game, as this is the user's intent (hits the x in the upper right corner of a window)
        System.exit(0);
    }
    public void windowDeactivated(WindowEvent e) { // if a user clicks outside a dialog box window, it hides it, so the better option is just to exit
        //System.exit(0);
    }
    public void windowDeiconified(WindowEvent e) {
    }
    public void windowIconified(WindowEvent e) {
    }
    public void windowOpened(WindowEvent e) {
    }
}
