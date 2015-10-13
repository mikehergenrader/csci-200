package blackjackgui;

// BlackJackIntroDialog.java
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
public class BlackJackIntroDialog extends JDialog { // handles ALL input at the beginning of the game, except players
    
    private JComboBox numberOfPlayersBox; // select how many players to insert in the game (1-5)
    
    private ButtonGroup numberOfDecksButtons; // how many decks to use
    private JRadioButton twoDecksButton;
    private JRadioButton fourDecksButton;
    private JRadioButton sixDecksButton; 
    
    private JTextField bettingLimitField; // get the betting limit to use for the remainder of the game
    
    private JButton okButton; // the default button
    private JButton cancelButton;
    
    private JPanel entriesPanel; // panels to contain all the components
    private JPanel buttonsPanel;
    private JPanel deckButtonsPanel;
    
    private BlackJackFrame owner; // reference back to the original frame for data to be shared
    
    private int numberOfPlayers = 0;
    private int numberOfDecks = 0;
    private int bettingLimit = 0;
    
    /**
     * constructor - creates a new intro dialog to ask for pregame input
     * @param parent - a reference to the parent frame
     * @param modal - whether or not this dialog should be modal
     */
    public BlackJackIntroDialog(BlackJackFrame parent, boolean modal) {
        super(parent,modal);
        
        owner = parent;

        setLayout(new GridLayout(2,1)); // for now
        
        initializePanels();
        entriesPanel.add(new JLabel("How many players are joining?"));
        
        initializeComboBox();
        initializeButtonGroup();
        initializeTextField();
        initializeButtons();
        
        add(entriesPanel);
        add(buttonsPanel);
        
        addWindowListener(new ClosingListener());
        getRootPane().setDefaultButton(okButton);
        
        setTitle("Blackjack Pregame Options");
        setSize(500,400);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);        
        setVisible(true);        
    }
    
    /**
     * no-arg constructor - cannot make a default object - needs a parent to hold its reference
     */
    private BlackJackIntroDialog() {        
    }
    
    /**
     * initializePanels - create the panels and set their layouts
     */
    private void initializePanels() {
        entriesPanel = new JPanel();
        entriesPanel.setLayout(new GridLayout(3,2));
        
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        
        deckButtonsPanel = new JPanel();
        deckButtonsPanel.setLayout(new GridLayout(3,1));
    }
    
    /**
     * initializeComboBox - creates the combo box and adds in all the options (1-5), then it adds it to the frame
     */
    private void initializeComboBox() {
        JPanel halfPanel = new JPanel(); // make the combo box smaller
        halfPanel.setLayout(new GridLayout(2,3));
        
        numberOfPlayersBox = new JComboBox();        
        
        for(int i = 1; i < 6; i++) {
            numberOfPlayersBox.addItem(String.valueOf(i)); // add the options into the combo box
        }
        
        halfPanel.add(numberOfPlayersBox);
        
        entriesPanel.add(halfPanel);
    }
    
    /**
     * initializeButtonGroup - creates all the radio button objects, adding them both to another panel and to a button group so only one can be selected
     * two decks is teh default option, established when this button is created
     */
    private void initializeButtonGroup() {
       
        twoDecksButton = new JRadioButton("Two",true);
        fourDecksButton = new JRadioButton("Four");
        sixDecksButton = new JRadioButton("Six");
        
        numberOfDecksButtons = new ButtonGroup(); // add the buttons to the button group
        numberOfDecksButtons.add(twoDecksButton);
        numberOfDecksButtons.add(fourDecksButton);
        numberOfDecksButtons.add(sixDecksButton);
        
        entriesPanel.add(new JLabel("How many decks to use?"));
                
        deckButtonsPanel.add(twoDecksButton);
        deckButtonsPanel.add(fourDecksButton);
        deckButtonsPanel.add(sixDecksButton);
        
        entriesPanel.add(deckButtonsPanel);
    }
    
    /**
     * initializeTextField - creates the text field with the default value of 1000, along with a label to give instructions to the user
     */
    private void initializeTextField() {
        bettingLimitField = new JTextField("1000",10);
        JLabel instructions = new JLabel("What is this table\'s betting limit?"); // so player knows what the text box is for
        entriesPanel.add(instructions);
        entriesPanel.add(bettingLimitField);
    }
    
    /**
     * initializeButtons - creates the OK and Cancel buttons (OK is selected as the default button for the dialog), adds their listeners, and adds them to the second panel
     */
    private void initializeButtons() {
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        
        ButtonListener dialogButtonsListener = new ButtonListener(); // inner class to handle button actions
        
        okButton.addActionListener(dialogButtonsListener);
        cancelButton.addActionListener(dialogButtonsListener);
        
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
    }

    /**
     * inner class: handles the events when OK or cancel is pressed, implementing the ActionListener interface
     */
    class ButtonListener implements ActionListener {
        
        /**
         * actionPerformed - handles when a button specified in e is pushed
         * @param e - the source of the event, fired by the JVM
         */
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == okButton) { // user hit ok, so now, check for input
                String bettingInput = bettingLimitField.getText();
                
                try { // make sure betting limit is a legitimate number
                    bettingLimit = Integer.parseInt(bettingInput);
                }
                catch(NumberFormatException ime) {
                    JOptionPane.showMessageDialog(null,"Your betting input is invalid.\nPlease enter a valid amount.");
                    return;
                }
                
                // make sure betting limit is a good value
                if(bettingInput.length() == 0) {
                    JOptionPane.showMessageDialog(null,"You must enter a betting limit.");
                    return;
                }
                if(bettingLimit < 1) { // make sure the limit is at least 1
                    JOptionPane.showMessageDialog(null,"Your limit is too low. Please enter a larger limit.");
                    return;
                }
                    
                numberOfPlayers = Integer.parseInt((String)numberOfPlayersBox.getSelectedItem()); // this will work, shouldn't even need to check exception here - I defined the input
                numberOfDecks = (twoDecksButton.isSelected())?2:(fourDecksButton.isSelected()?4:6); // shortened

                setVisible(false); // "dispose" of this dialog, and set the options back in the application's frame component              
            }
            else if(e.getSource() == cancelButton) { // user doesn't pick options, so exit the game
                System.exit(0);
            }
        }        
    }
    
    /**
     * getNumberOfPlayers - getter method that returns how many players were selected in the JComboBox
     * @return - the number of players for this game
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    
    /**
     * getNumberOfDecks - getter method for how many decks the user selected (from the radio buttons)
     * @return - the number of decks for this game
     */
    public int getNumberOfDecks() {
        return numberOfDecks;
    }
    
    /**
     * getBettingLimit - getter method for the betting limit the user sets in the text box
     * @return - the betting limit for this game
     */
    public int getBettingLimit() {
        return bettingLimit;
    }
}
