package blackjackgui;

// BlackJackHand.java
// Michael Hergenrader
// CSCI 200 Fall 2009
// USCID: 5048471246
// 10/11/2009

/**
 *
 * @author Michael Hergenrader
 */
public class BlackJackHand extends Hand {    
    private boolean stood;
    private boolean busted;
    private boolean doubledDown;
    private boolean surrendered; // properties specified by the player of what this hand is
    
    /**
     * BlackJackHand - constructs a new specific blackjack hand that inherits from hand and makes its properties defaulted
     */
    public BlackJackHand() {
        resetHand();
    }
    
    /**
     * resetHand - put all properties to false; makes everything default
     */
    public void resetHand() {
        stood = false;
        busted = false;
        doubledDown = false;
        surrendered = false;
    }
    
    /**
     * setStood - sets this hand's property to the value
     * @param value - which boolean value to set the property stood to
     */
    public void setStood(boolean value) {
        stood = value;
    }
    
    /**
     * setBusted - sets this hand's property to the value
     * @param value - which boolean value to set the property busted to
     */
    public void setBusted(boolean value) {
        busted = value;
    }
    
    /**
     * setDoubledDown - sets this hand's property to the value
     * @param value - which boolean value to set the property doubledDown to
     */
    public void setDoubledDown(boolean value) {
        doubledDown = value;
    }
    
    /**
     * setSurrendered - sets this hand's property to the value
     * @param value - which boolean value to set the property surrendered to
     */
    public void setSurrendered(boolean value) {
        surrendered = value;
    }
    
    /**
     * isStood - returns whether the player has ended action on this hand
     * @return - whether player has made this hand stopped
     */
    public boolean isStood() {
        return stood;
    }
    
    /**
     * isBusted - returns whether this hand has busted
     * @return - whether hand has busted
     */
    public boolean isBusted() {
        return busted;
    }
    
    /**
     * isDoubledDown - returns whether player doubled down on this hand
     * @return - whether player doubled down
     */
    public boolean isDoubledDown() {
        return doubledDown;
    }
    
    /**
     * isSurrendered - returns whether the hand has been surrendered by the player
     * @return - whether this hand has been surrendered
     */
    public boolean isSurrendered() {
        return surrendered;
    }
    
    /**
     * isFiveCardCharlie - returns whether a user has five cards that are all 21, giving the equivalent bonus of a blackjack
     * @return - whether this hand has the properties of a "five card charlie"
     */
    public boolean isFiveCardCharlie() {
        return (playerHand.size() == 5 && getHandTotal() <= 21);
    }
    
    /**
     * isComplete - returns whether or nor a hand is finished in a round - allows less typing, as it checks all conditions at once
     * @return - whether this hand is complete for this round
     */
    public boolean isComplete() {
        return (busted || stood || doubledDown || surrendered || isFiveCardCharlie());
    }    
}
