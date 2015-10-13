package blackjackgui;

// PlayerDisplayPanel.java
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
public class PlayerDisplayPanel extends JPanel {// implements ActionListener {
    private PlayerRegistrationDialog owner;
    
    private JScrollPane scrollableTextArea;
    private JTextArea playersData; // objects to display the players
  
    private ArrayList<BlackJackPlayer> players; // temporarily stores the players

    /**
     * constructor - creates a new display area in this panel where all registered players are displayed
     * @param owner - a reference to the owning frame
     * @param players - the players that are currently registered in this game, used for displayPlayers
     */
    public PlayerDisplayPanel(PlayerRegistrationDialog owner, ArrayList<BlackJackPlayer> players) {
	this.owner = owner;
        this.players = players;
        
        setLayout(new GridLayout(1,1));
	playersData = new JTextArea(8,10);
        playersData.setEditable(false);
	scrollableTextArea = new JScrollPane(playersData,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	

	add(scrollableTextArea);
    }

    /**
     * displayPlayers - writes to the text display area the information about every entered player in the system
     */
    public void displayPlayers() {
	playersData.setText("");
        for(BlackJackPlayer p : players) {        
           playersData.append("Name: " + p.getName() + "\nNickname: " + (p.getNickname().length()==0?"none":p.getNickname()) + "\nMoney amount: $" + p.getMoney() + "\n\n");
        }
    }
}
