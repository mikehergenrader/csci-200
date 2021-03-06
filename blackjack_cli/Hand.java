package blackjack;

import java.util.*;

/**
 *
 * @author Michael Hergenrader
 */
public class Hand {   // add a toString method 
    protected LinkedList<Card> playerHand;
        
    /**
     * Hand - default constructor/no-int - creates a new empty linked list
     */
    public Hand() {
        playerHand = new LinkedList();
    }
    
    /**
     * addCard - adds a card to the end of a player's hand
     * @param c - the card to be appended
     */
    public void addCard(Card c) { // this seems like a duplicate method
        if(c != null) {
            playerHand.add(c);
        }
    }
    
    /**
     * toString - returns the entire hand in the proper format
     * @return - a string representation of all the cards in a hand
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for(Card c : playerHand) {
            sb.append(new String(c.toString() + " "));
        }
        return sb.toString();
    }
    
    /**
     * getHandTotal - returns the point total of this hand object - automatically takes care of Aces
     * @return - the point total of this represented Hand object
     */
    public int getHandTotal() {
        
        int handTotal = 0;
        for(int i = 0; i < playerHand.size(); i++) {
            handTotal += ((Card)playerHand.get(i)).getValue();
        }
        
        if(handTotal > 21) {
            for(int i = 0; i < playerHand.size(); i++) {
                Card c = (Card)playerHand.get(i);
                if(c.getValue() == 11) {
                    c.knockDownAceValue();
                    handTotal -= 10;
                }
                
                if(handTotal <= 21) {
                    break;
                }
            }
        }        
        return handTotal;
    }
    
    /**
     * size - returns the size of this hand (how many cards)
     * @return - how many cards are in the hand
     */
    public int size() {
        return playerHand.size();
    }
    
    /**
     * getLast - returns the last card in the hand without removing it
     * @return - the last Card object in this Hand
     */
    public Card getLast() {
        return (Card)playerHand.getLast();
    }
    
    /**
     * removeLast - returns the last card in this Hand and removes it also
     * @return - the last Card object in this Hand
     */
    public Card removeLast() {
        return (Card)playerHand.removeLast();
    }
    
    /**
     * get - accesses a particular element of the linked list, and returns the Card object there
     * @param index - index of the Card (the get method in LinkedList will throw an Exception if not in range)
     * @return - the Card object at that index
     */
    public Card get(int index) {
        return playerHand.get(index);
    }
    
    /**
     * discardHand - throws away all the Card objects in this hand, it's total value and size are now both 0
     */
    public void discardHand() {
        playerHand.clear();
    }
}   