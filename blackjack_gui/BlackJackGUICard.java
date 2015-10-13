package blackjackgui;

// BlackJackGUICard.java
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
public class BlackJackGUICard extends Card {
    
    private static final int CARD_WIDTH = 73, CARD_HEIGHT = 97; // dimensions of each card
    
    private Image cardImage; // card images
    private Image backsideImage;
    
    private StringBuilder fileLocation; // where to look for the images
    
    /**
     * constructor - creates a new card that automatically locates an image in the proper directory to match
     * @param suit - the suit of the card
     * @param value - the value of the card
     * @param cardRepresentation - how the card is represented as a string (its value) - helps to locate the file
     */
    public BlackJackGUICard(int suit, int value, String cardRepresentation) {
        super(suit,value,cardRepresentation);
        
        fileLocation = new StringBuilder("C:/Users/Michael/Pictures/images/");
        fileLocation.append((toString().toLowerCase()));        
        fileLocation.append(".gif"); // all have the extension
        
        //System.out.println(fileLocation); // for testing
        
        ImageIcon i = new ImageIcon(fileLocation.toString()); // create the back image for the card
        cardImage = i.getImage();
        ImageIcon b = new ImageIcon("C:/Users/Michael/Pictures/images/b.gif");
        backsideImage = b.getImage();
    }
    
    /**
     * constructor - creates a BlackJackGUICard from a normal Card object
     * @param c - the card to transform
     */
    public BlackJackGUICard(Card c) { // turn a card into a GUI card
        super(c.getSuit(),c.getValue(),c.getCardRepresentation());
        
        fileLocation = new StringBuilder("C:/Users/Michael/Pictures/images/");
        fileLocation.append((toString().toLowerCase()));        
        fileLocation.append(".gif");
        
        //System.out.println(fileLocation); // for testing
        
        ImageIcon i = new ImageIcon(fileLocation.toString());
        cardImage = i.getImage();
        ImageIcon b = new ImageIcon("C:/Users/Michael/Pictures/images/b.gif");
        backsideImage = b.getImage();
    }
    
    /**
     * getFrontImage - getter method to return the frontside image of this card
     * @return - the image on the front of this card
     */
    public Image getFrontImage() {
        return cardImage;
    }
    
    /**
     * getBackImage - getter method to return the backside image of this card
     * @return - the image on the back of this card
     */
    public Image getBackImage() {
        return backsideImage;
    }
    
    /**
     * getWidth - getter method to return the width of a card (the constant)
     * @return - the width of a card object - 73
     */
    public static int getWidth() {
        return CARD_WIDTH;
    }
    
    /**
     * getHeight - getter method to return the height of a card of this class (any card)
     * @return - the height of a card object - 97
     */
    public static int getHeight() {
        return CARD_HEIGHT;
    }
    
    /**
     * knockDownAceValue - if an Ace's value is 11, make it 1 (Blackjack operation)
     */
    public void knockDownAceValue() {
        value = 1;
    }
    
    /**
     * knockUpAceValue - if an Ace's value is 1, make it 11 (Blackjack operation)
     */
    public void knockUpAceValue() {
        value = 11;
    }    
}
