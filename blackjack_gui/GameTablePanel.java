package blackjackgui;

// GameTablePanel.java
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
public class GameTablePanel extends JPanel {

    private BlackJackFrame owner;
    
    private ImageIcon tableImageIcon;
    private Image tableImage; // image of the blackjack table
    
    private ImageIcon yellowChipIcon, redChipIcon, blueChipIcon, blackChipIcon, orangeChipIcon, whiteChipIcon;
    private Image yellowChipImage, redChipImage, blueChipImage, blackChipImage, orangeChipImage, whiteChipImage;
    
    /**
     * constructor - creates a new panel, creates the applicable images, and sets the size values to work with GridBagLayout
     * @param owner - a reference to the parent frame of this panel
     */
    public GameTablePanel(BlackJackFrame owner) {
        this.owner = owner;
        
        createImages();
        
        setPreferredSize(new Dimension(980,570));
        setMinimumSize(new Dimension(980,570));
        setMaximumSize(new Dimension(980,570));
    }
    
    /**
     * createImages - with a hardcoded location, creates all images from their respective icons for the table and the chips
     */
    private void createImages() {
        tableImageIcon = new ImageIcon("C:/Users/Michael/Pictures/images/michaels_blackjack_table.jpg"); // should use a try catch block
        tableImage = tableImageIcon.getImage();
        
        // initialize chip images
        yellowChipIcon = new ImageIcon("C:/Users/Michael/Pictures/images/yellowchip.jpg");
        redChipIcon = new ImageIcon("C:/Users/Michael/Pictures/images/redchip.jpg");
        blueChipIcon = new ImageIcon("C:/Users/Michael/Pictures/images/bluechip.jpg");
        blackChipIcon = new ImageIcon("C:/Users/Michael/Pictures/images/blackchip.jpg");
        orangeChipIcon = new ImageIcon("C:/Users/Michael/Pictures/images/orangechip.jpg");
        whiteChipIcon = new ImageIcon("C:/Users/Michael/Pictures/images/whitechip.jpg");

        yellowChipImage = yellowChipIcon.getImage();
        redChipImage = redChipIcon.getImage();
        blueChipImage = blueChipIcon.getImage();
        blackChipImage = blackChipIcon.getImage();
        orangeChipImage = orangeChipIcon.getImage();
        whiteChipImage = whiteChipIcon.getImage();
    }    
    
    /**
     * paintComponent - overrides how to paint this component, taking in the graphics context: used to paint the table image on the panel, as well as the chips, after the normal painting is done
     * @param g - the graphics context of this panel to use for painting
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(tableImage != null) {
            g.drawImage(tableImage,0,0,getWidth(),getHeight(),this);
        }
        else {
            System.out.println("bad background image"); // just for testing
        }
        
        drawDealerCards(g);
        drawPlayerCards(g);

        // draw chips for the players      
        drawChips(g);
    }
   
    /**
     * drawDealerCards - draws the cards that the dealer currently has on the table
     * @param g - the graphics context of this panel to utilize
     */
    public void drawDealerCards(Graphics g) throws NullPointerException {
        
        BlackJackHand h = owner.getDealer().getCurrentHand();
        int x = 450;
        int y = 10;
        for(int i = 0; i < h.size(); i++) {
            
            if(h.get(i) != null) { // make sure there are not any null cards
                drawCard(g,x,y,h.get(i));
                x += 13;
            }
            else {
                throw new NullPointerException("Dealer card is null.");
            }
        }
         
        repaint(); // refreshes the drawing so the cards appear
    }
    
    /**
     * drawPlayerCards - draws out the cards for every player in the game
     * @param g - the graphics context of this panel to utilize
     */
    private void drawPlayerCards(Graphics g) {
        
        //BlackJackHand h = owner.getDealer().getCurrentHand();
        
        for(int i = 0; i < owner.getPlayers().size(); i++) {
            
            if(owner.getPlayers().get(i).isDisabled() || !owner.getPlayers().get(i).isActive()) {
                continue; // don't draw cards that aren't there (player not in this round/game)
            }
            
            int x = 0, y = 0, dx = 0;
            
            switch(i) { // set the values of where to start drawing and the dx
                case 0:
                    x = 878;
                    y = 256;
                    dx = -13;
                    break;
                case 1:
                    x = 714;
                    y = 366;
                    dx = -13;
                    break;
                case 2:
                    x = 454;
                    y = 446;
                    dx = 13;
                    break;
                case 3:
                    x = 200;
                    y = 378;
                    dx = 13;
                    break;
                case 4:
                    x = 88;
                    y = 256;
                    dx = 13;
                    break;
            }
            
            BlackJackPlayer p = owner.getPlayers().get(i);
            for(int j = 0; j < p.getCurrentHand().size(); j++) {                
                try {
                    drawCard(g,x+dx*j,y-10*j,p.getCurrentHand().get(j)); // draw the card in the player's hand
                }
                catch(NullPointerException e) {
                    //JOptionPane.showMessageDialog(null,"player " + j + " " + p.getCurrentHand().get(j).toString());
                    //System.out.println("NULL POINTER: " + "player " + j + " " + p.getCurrentHand().get(j).toString());
                    System.out.println(e.getMessage());
                }
                repaint(); // refreshes the drawing area
            }
        }
    }
    
    /**
     * drawCard - draws a card on the panel
     * @param g - the graphics context of this panel to invoke drawImage
     * @param x - the x component of the panel to start drawing at
     * @param y - the y component of the panel to start drawing at
     * @param c - the card to draw
     */
    private void drawCard(Graphics g, int x, int y, BlackJackGUICard c) throws NullPointerException {
        if(c == null) {
            throw new NullPointerException("Null Pointer! Card doesn't exist! x = " + x + ", y = " + y);
        }
        g.drawImage(c.isFaceUp()?c.getFrontImage():c.getBackImage(), x, y, this);
    }
    
    /**
     * drawChips - draws out chips that correspond to a player's bet, as would work in a real-life game
     * for instance: $1005 as a bet would draw a 1000 chip and a 5 chip, as they represent this bet (only draws a chip once, not once each time for a bet to keep the drawing clear)
     * @param g - the graphics context of this panel to utilize
     */
    private void drawChips(Graphics g) {
        for(int i = 0; i < owner.getPlayers().size(); i++) {
            
            if(owner.getPlayers().get(i).isDisabled() || !owner.getPlayers().get(i).isActive()) {
                continue;
            }
            
            int x = 0, y = 0, dy = 0;
            
            switch(i) { // set the values of where to start drawing and the dy
                case 0:
                    x = 692;
                    y = 94;
                    dy = 10;
                    break;
                case 1:
                    x = 574;
                    y = 170;
                    dy = 5;
                    break;
                case 2:
                    x = 444;
                    y = 180;
                    dy = 0;
                    break;
                case 3:
                    x = 298;
                    y = 128;
                    dy = -5;
                    break;
                case 4:
                    x = 216;
                    y = 30;
                    dy = -10;
                    break;
            }
            // chip dimensions: 35x35
            
            BlackJackPlayer p = owner.getPlayers().get(i);
            int bet = p.getBet();            
            
            if((bet / 1000) > 0) { // if divisible by 1000, draw a chip to represent 1000's and so on
                g.drawImage(orangeChipImage,x,y,35,35,this);
                bet %= 1000; // then apply the next level down of money to this rule
            }
            if((bet / 50) > 0) {
                g.drawImage(blackChipImage,x+9,y-dy,35,35,this);
                bet %= 50;
            }
            if((bet / 10) > 0) {
                g.drawImage(blueChipImage,x+18,y-dy*2,35,35,this);
                bet %= 10;
            }
            if((bet / 5) > 0) {
                g.drawImage(redChipImage,x+27,y-dy*3,35,35,this);
                bet %= 5;
            }
            if((bet / 1) > 0) {
                g.drawImage(yellowChipImage,x+36,y-dy*4,35,35,this);
            }
            repaint(); // refreshes the drawing buffer to include the drawn chips            
        }      
    }
}
