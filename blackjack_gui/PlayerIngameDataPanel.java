package blackjackgui;

// PlayerIngameDataPanel.java
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
public class PlayerIngameDataPanel extends JPanel {

    private BlackJackFrame owner;
    
    private JTextArea informationArea;
    private ArrayList<JTextArea> playerStatLabels;

    /**
     * constructor - constructs a new panel that provides the information about all players, the hand number, and the betting limit
     * @param owner
     */
    public PlayerIngameDataPanel(BlackJackFrame owner) {
        this.owner = owner;
        setBackground(Color.BLACK);
        
        informationArea = new JTextArea(3,24);
        informationArea.setBackground(Color.BLACK);
        informationArea.setForeground(Color.WHITE);
        informationArea.setEditable(false); // set the information area at the top
        
        playerStatLabels = new ArrayList<JTextArea>(); // initialize all the player stat labels
        setLayout(new GridLayout(6,1));
        
        setPreferredSize(new Dimension(220,570));
        setMinimumSize(new Dimension(220,570));

        add(informationArea);
        
        for(int i = 0; i < owner.getPlayers().size(); i++) { // create the fields
            playerStatLabels.add(new JTextArea(6,16));
            playerStatLabels.get(i).setEditable(false);
            playerStatLabels.get(i).setBackground(Color.BLACK);
            
            add(playerStatLabels.get(i));
        }
        
        playerStatLabels.get(0).setForeground(Color.RED); // determine the colors of these stat areas
        if(owner.getPlayers().size() > 1) {
            playerStatLabels.get(1).setForeground(Color.ORANGE);
        }
        if(owner.getPlayers().size() > 2) {
            playerStatLabels.get(2).setForeground(Color.GREEN);
        }
        if(owner.getPlayers().size() > 3) {
            playerStatLabels.get(3).setForeground(Color.BLUE);
        }
        if(owner.getPlayers().size() > 4) {
            playerStatLabels.get(4).setForeground(Color.GRAY);
        }
        
        updateFields(owner.getPlayers()); // update the fields initially
    }
    
    /**
     * updateFields - updates the player info dynamically when invoked
     * @param players - a reference to the players array list of the invoking frame, displays this information
     */ 
    public void updateFields(ArrayList<BlackJackPlayer> players) {
        
        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).isDisabled()) {
                playerStatLabels.get(i).setText(""); // don't include disabled players
                continue;
            }
            
            BlackJackPlayer p = players.get(i);
            playerStatLabels.get(i).setText("Name: " + p.getName() + "\nMoney: $" + p.getMoney() + "\nCurrent Bet: " + (p.isActive()?"$" + p.getBet():"skipping hand") + (p.hasSplit()?", Split Bet: $" + p.getSplitBet():"") + "\nHand Total: " + (p.isActive()?p.getInitialHand().getHandTotal():"n/a") + (p.hasSplit()?"\nSplit Hand Total: " + p.getSplitHand().getHandTotal():"\n"));
        }   
    }
    
    /**
     * updatePlayerStatus - tells the final performance of each player in the game after a hand
     * @param index - the index of the player that receives the status
     * @param status - what status to give; a null string means the player was inactive for the hand
     */
    public void updatePlayerStatus(int index, String status) {
        BlackJackPlayer p = owner.getPlayers().get(index);
        
        if(status == null) {
            playerStatLabels.get(index).append("\n" + p.getName() + " was inactive for this hand.");
            return;
        }

        playerStatLabels.get(index).append("\n" + p.getName() + "\'s hand " + status);      
    }
    
    /**
     * updatePlayerSplitStatus - works just like the method above, but used for split hands
     * @param index - the index of the player that receives the status
     * @param status - what status the player's split hand had
     */
    public void updatePlayerSplitStatus(int index, String status) {
        playerStatLabels.get(index).setText(playerStatLabels.get(index).getText() + "\n" + "\nThe split hand " + status);
    }
    
    /**
     * setInformation - helper method to the frame to update the text in the information area
     * @param text - the text to include
     */
    public void setInformation(String text) {
        informationArea.setText(text);
    }
}
