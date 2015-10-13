package blackjackgui;

// BetInputDialog.java
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
public class BetInputDialog extends JDialog implements ActionListener {

    private BlackJackFrame parent; // reference to owning frame
    
    private JPanel inputPanel;
    private JPanel buttonsPanel; // panels that control the look of this dialog
    
    private JTextField betField; // field to enter bet
    private JButton enterButton;
    private JButton skipHandButton;    
    
    /**
     * constructor - creates and displays a new dialog box to obtain each player's bet before the game process actually begins
     * @param parent - reference to the parent frame of this box
     * @param modal - whether or not this box should be made modal (whether it MUST be dealt with before anything else)
     * @param turnIndex - number to which player is currently betting; used to set the button colors
     */
    public BetInputDialog(BlackJackFrame parent, boolean modal, int turnIndex) {
        super(parent,modal);
        this.parent = parent;
        
        inputPanel = new JPanel();
        buttonsPanel = new JPanel();
        
        betField = new JTextField(10);
        enterButton = new JButton("Bet!");
        enterButton.setToolTipText("Enter your bet for this hand.");
        skipHandButton = new JButton("Skip Hand");
        skipHandButton.setToolTipText("Choose to sit out this hand.");
        
        enterButton.addActionListener(this);
        skipHandButton.addActionListener(this);
        
        inputPanel.add(betField);
        
        buttonsPanel.add(enterButton);
        buttonsPanel.add(skipHandButton);

        setButtonColors(turnIndex);
        setLayout(new GridLayout(2,1));

        add(inputPanel);
        add(buttonsPanel);        
                
        getRootPane().setDefaultButton(enterButton);
        
        setTitle("Starting Hand " + parent.getCurrentHandNumber() + ", " + parent.getCurrentPlayer().getName() + "\'s turn");
        setSize(300,100);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);        
        setVisible(true);        
    }
    
    /**
     * default constructor is not allowed to be invoked - the one above must be used
     */
    private BetInputDialog() {
    }
    
    /**
     * setButtonColors - helper method to constructor - will set the buttons to a certain color based on which index (player) is now betting
     * @param index - index of the current player
     */
    private void setButtonColors(int playerIndex) {
        Color c;
        switch(playerIndex) {
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
        
        enterButton.setBackground(c);
        skipHandButton.setBackground(c);    
    }
    
    /**
     * actionPerformed - implementation of the ActionListener interface, providing actions to the two buttons in the dialog box
     * @param e - the ActionEvent fired by the JVM, used to determine the source of the click
     */
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == enterButton) {            
            try {        
                int bet = Integer.parseInt(betField.getText());

                if(bet < 1) { // make sure the bet is high enough
                    JOptionPane.showMessageDialog(null,"Your bet must be one or higher. Please enter a valid bet.","Invalid Bet",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(bet > Math.min(parent.getBettingLimit(),parent.getCurrentPlayer().getMoney())) { // don't let user go over what they have
                    JOptionPane.showMessageDialog(null,"You cannot bet that high. The maximum you can bet is $" + Math.min(parent.getBettingLimit(),parent.getCurrentPlayer().getMoney()) + ".\nPlease enter a valid bet.","Invalid Bet",JOptionPane.ERROR_MESSAGE);
                    return;
                }   
                
                parent.getCurrentPlayer().setBet(bet); // create the bet, and subtract it from the money amount
                parent.getCurrentPlayer().setMoney(parent.getCurrentPlayer().getMoney() - bet);
                
                setVisible(false); // dispose of this dialog
            }
            catch(NumberFormatException nfe) { // if user doesn't enter a correct number
                JOptionPane.showMessageDialog(null,"Invalid entry for bet.\nPlease enter a valid amount to bet.","Invalid Bet",JOptionPane.ERROR_MESSAGE);
                return;
       
            }            
        }
        else if(e.getSource() == skipHandButton) { // make player inactive for this hand
            parent.getCurrentPlayer().setActive(false);
            parent.getCurrentPlayer().setBet(0);
            setVisible(false); // dispose of this dialog
        }
    }    
}
