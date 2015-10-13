package blackjackgui;

// MessagesPanel.java
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
public class MessagesPanel extends JPanel {
    private BlackJackFrame owner;
    
    private JLabel messages;
    
    /**
     * constructor - creates a new panel with just a JLabel that displays what the current player does or whose turn it is in the bottom right of the parent frame
     * @param owner - a reference to the parent frame of this panel (BlackJackFrame)
     */
    public MessagesPanel(BlackJackFrame owner) {
        this.owner = owner;
        
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        
        messages = new JLabel(""); // create the label object that is the only thing in this panel
        add(messages);
        
        setPreferredSize(new Dimension(220,130));
        setMinimumSize(new Dimension(220,130)); // used to help the constraints of the GridBagLayout of the parent frame
    }
    
    /**
     * setMessage - sets the message in the JLabel to a specified string
     * @param text - the text to put into this message label
     */
    public void setMessage(String text) {
        messages.setText(text);
    }
    
    /**
     * setTextColor - sets the foreground color of this JLabel to the color determined by index, the current player who has the turn
     * @param index - the index of the current player, used to determine which color to set the text
     */
    public void setTextColor(int index) {
        Color c;
        switch(index) {
            case 0:
                c = Color.RED;
                break;
            case 1:
                c = Color.ORANGE;
                break;
            case 2:
                c = Color.GREEN;
                break;
            case 3:
                c = Color.BLUE;
                break;
            case 4:
                c = Color.GRAY;
                break;
            default:
                c = Color.WHITE; // so the compiler doesn't complain about not being initialized
        }
        
        messages.setForeground(c);        
    }
}
