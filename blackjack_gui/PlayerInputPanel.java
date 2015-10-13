package blackjackgui;

// PlayerInputPanel.java
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
public class PlayerInputPanel extends JPanel {
    private JButton enterPlayer, clearEntries;
    private JButton startGame; // starts the game w/o as many players as intended

    private JTextField nameField; // name fields
    private JTextField nicknameField;
    private JTextField moneyField;
    
    private JLabel nameMessage;
    private JLabel successMessage;
    private JLabel moneyMessage; // the messages that display whether the entries were existent and valid
    
    private PlayerRegistrationDialog owner;
    
    /**
     * constructor - creates a new panel for inputting players with entry/clear buttons, a start game button, and text fields for entries
     * @param owner - a reference to the owning frame
     */
    public PlayerInputPanel(PlayerRegistrationDialog owner) {
	this.owner = owner;
        
	setLayout(new GridLayout(4,3));
	
	setupFields();
	setupButtons();
        setupMessageLabels();

	add(new JLabel("Enter name"));
        add(new JLabel("Enter nickname"));
        add(new JLabel("Enter money"));
	
        add(nameField); // add the fields in first (after the labels of course, so player knows what to do)
        add(nicknameField);
        add(moneyField);
	
        add(enterPlayer);
	add(clearEntries);
	add(startGame);
        
        add(nameMessage);
        add(successMessage);
        add(moneyMessage);
    }

    /**
     * setupFields - helper method that creates the text fields and sets tool tips for instructions
     */
    private void setupFields() {
	nameField = new JTextField(20);
	nicknameField = new JTextField(20);
	moneyField = new JTextField(20);

	nameField.setToolTipText("Enter the player\'s name");
	nicknameField.setToolTipText("Enter his/her nickname");
	moneyField.setToolTipText("Enter his starting amount of money.");
    }

    /**
     * setupButtons - helper method the creates the buttons and adds their action listeners
     */
    private void setupButtons() {
	ButtonListener bl = new ButtonListener();
	enterPlayer = new JButton("Add player");
	enterPlayer.addActionListener(bl);

	clearEntries = new JButton("Clear form");
	clearEntries.addActionListener(bl);
        
        startGame = new JButton("Start Game");
        startGame.addActionListener(bl);
    }
    
    /**
     * setupMessageLabels - creates message labels that are initially blank to alert players to events from their input
     */
    private void setupMessageLabels() {        
        nameMessage = new JLabel("");
        successMessage = new JLabel("");
        moneyMessage = new JLabel("");
    }

    /**
     * inner class: ButtonListener provides all the events that occur when a player presses any of the three buttons in the panel
     */
    class ButtonListener implements ActionListener {
	
        /**
         * actionPerformed - implements ActionListener to determine the events that occur when a button is pushed
         * @param e - the event that occurred, containing the source of the event
         */
        public void actionPerformed(ActionEvent e) {
	    if(e.getSource() == enterPlayer) { // enter a player with the input entries in the game
		successMessage.setText("");
                
                int money = 0;
		if(nameField.getText().length() == 0) {
                    nameMessage.setText("Player name not entered.");
		    return;
		}
                else {
                    nameMessage.setText("");
                }
                
		try {
		    money = Integer.parseInt(moneyField.getText());
                    
                    if(money == 0) {
                        moneyMessage.setText("Money amount must be greater than zero.");
                        return;
                    }                    
		}
		catch(NumberFormatException ex) {
                    moneyMessage.setText("Invalid money amount.");
		    return;
		}
                
                moneyMessage.setText(""); // money was ok, so delete any error message

                if(owner.getOwnerFrame() instanceof BlackJackFrame) {                    
                    ((BlackJackFrame)owner.getOwnerFrame()).addPlayer(nameField.getText(),nicknameField.getText(),money);
                }

                successMessage.setText("Player added successfully.");

                owner.addPlayer();
                owner.displayPlayers();
                clearDataEntries();
	    }
            
	    else if(e.getSource() == clearEntries) { // clear the text boxes and message labels
		clearDataEntries();
	    }
            
            else if(e.getSource() == startGame) { // allows players to start a game early so if someone leaves right at the start, they don't have to make a default player for them
                if(owner.getOwnerFrame() instanceof BlackJackFrame) {
                    
                    if(((BlackJackFrame)owner.getOwnerFrame()).getPlayers().isEmpty()) { // there must be at least one player in order to continue
                        JOptionPane.showMessageDialog(null,"You must add at least one player in order to continue.","No players!",JOptionPane.ERROR_MESSAGE);
                    }
                    
                    // there are players, but not as many as was input earlier - ask them if they just wish to continue (someone changed their mind, etc.)
                    else if(((BlackJackFrame)owner.getOwnerFrame()).getPlayers().size() < ((BlackJackFrame)owner.getOwnerFrame()).getNumberOfPlayers()) {
                        int result = JOptionPane.showConfirmDialog(null,"WARNING: You have not entered all players yet.\nWould you like to continue?","Not all players added!",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
                        if(result == JOptionPane.YES_OPTION) { // start the game!
                            owner.setVisible(false);
                        }
                        else if(result == JOptionPane.NO_OPTION) { // go back to add the missing players
                            return; // not really needed
                        }
                    }
                }
            }
	}
        
        /**
         * clearDataEntries - helper method that clears all of the text fields and some message labels to present a blank panel to the player in charge (invoked when a player is entered, for instance, so can 
         * start over without having to manually delete the players
         */
        private void clearDataEntries() {
            nameField.setText("");
            nicknameField.setText("");
            moneyField.setText("");

            nameMessage.setText("");
            moneyMessage.setText("");
        }
    }
}
