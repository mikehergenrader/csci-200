package blackjackgui;

// HandResultsDialog.java
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
public class HandResultsDialog extends JDialog implements ActionListener {

    private BlackJackFrame owner; // reference to owner frame
    
    private JTextArea resultsText;
    private JScrollPane scrollPane; // to display the results
    
    private JButton confirmButton;
    private JButton exitButton; // button objects
    
    /**
     * constructor - creates a new dialog with a text area and two buttons, initializing the text area to be inside a JScrollPane
     * @param owner - reference to the owner frame of this dialog
     * @param modal - sets whether this dialog is modal, or whether it must be answered before further action can be taken
     */
    public HandResultsDialog(BlackJackFrame owner, boolean modal) {
        super(owner,modal);
        this.owner = owner;
        
        resultsText = new JTextArea(4,30);
        resultsText.setEditable(false);
        scrollPane = new JScrollPane(resultsText,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        confirmButton = new JButton("Next Hand!");
        confirmButton.addActionListener(this);
        
        exitButton = new JButton("Exit Game");
        exitButton.addActionListener(this);
        
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(new GridLayout(3,1));
        //setLayout(new BorderLayout());
        
        add(scrollPane);
        add(confirmButton);
        add(exitButton);        
                
        getRootPane().setDefaultButton(confirmButton);

        //addWindowListener(new ClosingListener()); // might be taken out
    }
    
    /**
     * addText - adds text from somewhere outside this dialog (like scores from the main frame) to this text area
     * @param text - the text to be appended in the text area
     */
    public void addText(String text) {
        resultsText.append(text);
    }
    
    /**
     * actionPerformed - implemented for use for the buttons - responds to events when they are clicked
     * @param e - the event sent by the JVM that is tested to see what is clicked
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == confirmButton) {
            setVisible(false); // causes all the add text 
        }
        else if(e.getSource() == exitButton) {
            System.exit(0);
        }
    }   
}
