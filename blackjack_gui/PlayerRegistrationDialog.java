package blackjackgui;

// PlayerRegistrationDialog.java
// Michael Hergenrader
// CSCI 200 Fall 2009
// USCID: 5048471246
// 10/11/2009

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 *
 * @author Michael Hergenrader
 */
public class PlayerRegistrationDialog extends JDialog {
    
    private PlayerInputPanel inputPanel;
    private PlayerDisplayPanel displayPanel;
    
    private int numberOfPlayersRegistered; // fields that determine whether this dialog has completed its task
    private int numberOfPlayers;
    
    private BlackJackFrame ownerFrame;

    /**
     * constructor - creates a new popup dialog that will allow the players to input their data for the game before it starts
     * @param ownerFrame - reference to the parent frame of this dialog box
     * @param numberOfPlayers - how many players were chosen to be input
     * @param players - the players array list of the parent frame; the dialog box is just a middle ground that passes it to the display panel
     */
    public PlayerRegistrationDialog(BlackJackFrame ownerFrame, int numberOfPlayers, ArrayList<BlackJackPlayer> players) {
        super(ownerFrame,true);
        this.ownerFrame = ownerFrame;
        
	inputPanel = new PlayerInputPanel(this);
	displayPanel = new PlayerDisplayPanel(this,players);

	setLayout(new GridLayout(2,1));
        addWindowListener(new ClosingListener()); // exit the game if the dialog box is closed

	add(inputPanel);
	add(displayPanel);
        
        numberOfPlayersRegistered = 0;
        this.numberOfPlayers = numberOfPlayers;
        
        setSize(600,400);
        setLocationRelativeTo(null);
        setTitle("Player Registration - " + numberOfPlayers + " players left to register");
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    /**
     * displayPlayers - call the display panel method display players so that the input panel can reach this method (communicates across the panels)
     */
    public void displayPlayers() {
	displayPanel.displayPlayers();
    }
    
    /**
     * getOwnerFrame - getter method that returns a reference to the parent frame
     * @return - a reference to the parent frame
     */
    public BlackJackFrame getOwnerFrame() {
        return ownerFrame;
    }
    
    /**
     * addPlayer - called in the input panel to add the player with the input fields to the count - if the count equals the number of players in the game, start it up!
     * else, display how many more players should be input (this can be circumvented by the Start game button of the input panel)
     */
    public void addPlayer() {
        numberOfPlayersRegistered++;
        if(numberOfPlayersRegistered == numberOfPlayers) { // all entered
            JOptionPane.showMessageDialog(null,"All players entered successfully!","Game starting...",JOptionPane.INFORMATION_MESSAGE);
            setVisible(false);
        }
        
        setTitle("Player Registration - " + (int)(numberOfPlayers - numberOfPlayersRegistered) + " players left to register");
    }
}
