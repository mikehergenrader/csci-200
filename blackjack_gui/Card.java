package blackjackgui;

// Card.java
// Michael Hergenrader
// CSCI 200 Fall 2009
// USCID: 5048471246
// 10/11/2009

/**
 *
 * @author Michael Hergenrader
 */
public class Card {
    public static final int NONE = 0, HEARTS = 1, DIAMONDS = 2, CLUBS = 3, SPADES = 4; // represents the suits
    
    protected int value; // the Card fields
    protected int suit;
    protected String cardRepresentation;
    
    protected boolean faceUp; // used only for the dealer in this particular game
    
    protected Card() { // made protected so a subclass can work, but also so this isn't actually called explicitly by a user
        
    }
    
    /**
     * Card() - constructs a card with a suit, a value, and how it will be displayed on screen
     * @param suit
     * @param value
     * @param cardRepresentation
     */
    public Card(int suit, int value, String cardRepresentation) {
        this.suit = suit;
        this.value = value;
        this.cardRepresentation = new String(cardRepresentation);
        
        faceUp = true; // by default, all cards are face up
    }
    
    /**
     * Card() - constructs a copy of another Card object - deep copy
     * @param otherCard
     */
    public Card(Card otherCard) { // essentially a copy constructor
        this.suit = otherCard.getSuit();
        this.value = otherCard.getValue();
        this.cardRepresentation = new String(otherCard.getCardRepresentation());
        
        this.faceUp = otherCard.isFaceUp();
    }
    
    /**
     * getSuit - gets the suit
     * @return - the suit of the card
     */
    public int getSuit() {
        return suit;
    }
    
    /**
     * getValue - gets the value of the card
     * @return - the card's value
     */
    public int getValue() {
        return value;
    }
    
    /**
     * getCardRepresentation - returns the String representation for a card - its value as a String
     * @return - how the card is displayed (the String representation of the card's value)
     */
    public String getCardRepresentation() {
        return cardRepresentation;
    }
    
    /**
     * toString - overrides the Object class toString
     * @return - how the card is drawn on screen with a value
     */
    public String toString() {
        return new String(getCardRepresentation()+getSuitString());
    }
    
    /**
     * getSuitString
     * @return - the String value of the particular card suit
     */
    private String getSuitString() {
        switch(suit) {
            case NONE:
                return ""; // might not need this since compiler demands the last line anyway
            case HEARTS:
                return "H";
            case DIAMONDS:
                return "D";
            case CLUBS:
                return "C";
            case SPADES:
                return "S";
        }
        return ""; // so compiler doesn't complain
    }    
    
    /**
     * isFaceUp - returns whether this card is face up
     * @return - the faceUp boolean instance variable value
     */
    public boolean isFaceUp() {
        return faceUp;
    }
    
    /**
     * setFaceUp - sets a card either face up or face down
     * @param value - if true, the card is face up; else, it is face down
     */
    public void setFaceUp(boolean value) {
        faceUp = value;
    }
}
