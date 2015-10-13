package blackjack;

import javax.swing.*;
import java.awt.*;
import java.net.*;

/**
 *
 * @author Michael Hergenrader
 */
public class BlackjackGUICard extends Card {

    private ImageIcon cardImage;
    private ImageIcon backsideImage;
    
    private BlackjackGUICard() { // don't allow - needs to have an image for this card
        
    }
    
    
    // could reduce this down using this()
    public BlackjackGUICard(int suit, int value, String cardRepresentation, Image front, Image back) {
        super(suit,value,cardRepresentation);
        
        cardImage = new ImageIcon(front);
        backsideImage = new ImageIcon(back);
    }
    
    public BlackjackGUICard(int suit, int value, String cardRepresentation, String frontImageName, String backImageName) {
        super(suit,value,cardRepresentation);
        
        cardImage = new ImageIcon(frontImageName);
        backsideImage = new ImageIcon(backImageName);
    }
    
    public BlackjackGUICard(int suit, int value, String cardRepresentation, URL frontImageLocation, URL backImageLocation) {
        super(suit,value,cardRepresentation);
        
        cardImage = new ImageIcon(frontImageLocation);
        backsideImage = new ImageIcon(backImageLocation);
    }
    
    public Image getCardFrontImage() {
        return cardImage.getImage();
    }
    
    public Image getCardBackImage() {
        return backsideImage.getImage();
    }
    
}
