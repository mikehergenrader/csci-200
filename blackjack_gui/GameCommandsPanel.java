package blackjackgui;

// GameCommandsPanel.java
// Michael Hergenrader
// CSCI 200 Fall 2009
// USCID: 5048471246
// 10/11/2009

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Michael Hergenrader
 */
public class GameCommandsPanel extends JPanel {

    private BlackJackFrame owner; // reference to the owning frame
    private JButton hit, stand, doubleDown, split, surrender;
    private JButton toggleHandView; // special button that is used for split hands

    /**
     * constructor - creates a new GameCommandsPanel and initializes its look
     * @param owner - a parameter to the frame that owns this, so data can be shared
     */
    public GameCommandsPanel(BlackJackFrame owner) {
        this.owner = owner;
        
        setBackground(Color.BLACK);   
        
        setPreferredSize(new Dimension(980,130)); // used to help format GridBagLayout
        setMinimumSize(new Dimension(980,130));
        setMaximumSize(new Dimension(980,130));
        
        initializeButtons();
    }
    
    /**
     * initializeButtons - a helper method to the constructor that will set the button text, add their action listener, and add them to the panel
     */
    private void initializeButtons() {        
        hit = new JButton("Hit");
        stand = new JButton("Stand");
        doubleDown = new JButton("Double Down");
        split = new JButton("Split");
        surrender = new JButton("Surrender");
        toggleHandView = new JButton("View Split Hand");
        
        CommandListener cl = new CommandListener();
        hit.addActionListener(cl);
        stand.addActionListener(cl);
        doubleDown.addActionListener(cl);
        split.addActionListener(cl);
        surrender.addActionListener(cl);
        toggleHandView.addActionListener(cl);

        add(hit);
        add(stand);
        add(doubleDown);
        add(split);
        add(surrender);
        add(toggleHandView);
    }
    
    /**
     * setButtonColors - changes the background color of the button objects according to which player, specified by index, is currently active
     * @param index - the index of the player whose turn it is (helps denote changes in turn)
     */
    public void setButtonColors(int index) {
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
                c = Color.WHITE; // to avoid compiler complaints
        }
        
        hit.setBackground(c); // set the background of these buttons to the specified color
        stand.setBackground(c);
        doubleDown.setBackground(c);
        split.setBackground(c);
        surrender.setBackground(c);
        toggleHandView.setBackground(c);        
    }
    
    /**
     * setHitButtonEnabled - a helper method for the owner frame that can disable or enable the use of the button, based on a parameter
     * @param value - whether the button should be enabled or disabled
     */
    public void setHitButtonEnabled(boolean value) {
        hit.setEnabled(value);
    }
    
    /**
     * setStandButtonEnabled - a helper method for the owner frame that can disable or enable the use of the button, based on a parameter
     * @param value - whether the button should be enabled or disabled
     */
    public void setStandButtonEnabled(boolean value) {
        stand.setEnabled(value);
    }
        
    /**
     * setDoubleDownButtonEnabled - a helper method for the owner frame that can disable or enable the use of the button, based on a parameter
     * @param value - whether the button should be enabled or disabled
     */
    public void setDoubleDownButtonEnabled(boolean value) {
        doubleDown.setEnabled(value);
    }
    
    /**
     * setSurrenderButtonEnabled - a helper method for the owner frame that can disable or enable the use of the button, based on a parameter
     * @param value - whether the button should be enabled or disabled
     */
    public void setSurrenderButtonEnabled(boolean value) {
        surrender.setEnabled(value);
    }
    
    /**
     * setSplitButtonEnabled - a helper method for the owner frame that can disable or enable the use of the button, based on a parameter
     * @param value - whether the button should be enabled or disabled
     */
    public void setSplitButtonEnabled(boolean value) {
        split.setEnabled(value);
    }
    
    /**
     * setViewButtonEnabled - a helper method for the owner frame that can disable or enable the use of the button, based on a parameter
     * @param value - whether the button should be enabled or disabled
     */
    public void setViewButtonEnabled(boolean value) {
        toggleHandView.setEnabled(value);
    }
    
    /**
     * setViewButtonText - toggles the text of the view button - this is called by the owning frame to give the duality of buttons to a single button (switches the text to represent that the hands have been switched)
     * @param text - the text to appear in the button
     */
    public void setViewButtonText(String text) {
        toggleHandView.setText(text);
    }
    
    /**
     * inner class: CommandListener - provides the ActionListener implementation for this panel in an easy to read inner class, providing functionality to all the buttons, invoking the owner frame's methods (as it handles the game)
     */
    class CommandListener implements ActionListener {
        
        /**
         * actionPerformed - implements ActionListener for all buttons in the panel, calling the appropriate player action for each
         * @param e - the event fired by the JVM (used to determine the source)
         */
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == hit) {
                owner.playerHits();
            }
            else if(e.getSource() == stand) {
                owner.playerStands();
            }
            else if(e.getSource() == doubleDown) {
                owner.playerDoublesDown();
            }
            else if(e.getSource() == split) {
                owner.playerSplits();
            }
            else if(e.getSource() == surrender) {
                owner.playerSurrenders();
            }
            else if(e.getSource() == toggleHandView) {
                owner.toggleHandView();                
            }
        }
    }    
}
