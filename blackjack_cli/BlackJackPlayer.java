package blackjack;

/**
 *
 * @author Michael Hergenrader
 */
public class BlackJackPlayer {

    private String playerName;
    private int playerMoney; // variables prompted at the beginning
    
    private BlackJackHand hand;
    private BlackJackHand splitHand;
    private BlackJackHand currentHand; // just a reference to which hand is active - might not be needed
    
    private int playerBet;
    private int splitBet; // the bets attached to the hands       
    
    private boolean handsSplit; // whether the player has split
    private boolean active; // whether or not this player is even involved in this particular hand
    
    private BlackJackPlayer() { // private default constructor forces a player to have both a name and a starting amount of money
        
    }
    
    /**
     * BlackJackPlayer - creates a new player for the game with a name and a starting amount of money
     * @param playerName - the name of the player
     * @param playerMoney - the starting amount of money that the player brings to the "table"
     */
    public BlackJackPlayer(String playerName, int playerMoney) {
        this.playerName = playerName;
        this.playerMoney = playerMoney;
        
        setupPlayer();        
    }
    
    /**
     * BlackJackPlayer - constructs a new player from an existing one - performs a deep copy into this new player object
     * @param other - the player from which to copy to this one
     */
    public BlackJackPlayer(BlackJackPlayer other) { // copy constructor, essentially
        this.playerName = new String(other.getName());
        this.playerMoney = other.getMoney();
        
        setupPlayer();
    }
    
    /**
     * setupPlayer - initializes the fields of BlackJackPlayer, used by constructors as a helper method
     */
    private void setupPlayer() {
     
        hand = new BlackJackHand();
        splitHand = new BlackJackHand(); // initialize the hands
        
        handsSplit = false;
        currentHand = hand;
        
        active = true; // by default, player is involved in a hand, unless he specifies otherwise, and the hands aren't split
    }
    
    /**
     * setupForHand - resets the properties and contents of both hands and puts the reference to the one that is the original
     */
    public void setupForHand() { 
        hand.resetHand();
        splitHand.resetHand();
        
        handsSplit = false;
        currentHand = hand;
    }
    
    /**
     * setCurrentHandToOriginal - sets the reference to point to the original, non-split hand
     */
    public void setCurrentHandToOriginal() {
        currentHand = hand;
    }
    
    /**
     * setCurrentHandToSplit - sets the reference to point to the split hand
     */
    public void setCurrentHandToSplit() {
        currentHand = splitHand;
        handsSplit = true;
    }
    
    /**
     * setHandsSplit - determines whether a player now has split hands
     * @param value - true or false value that determines whether the hands are split
     */
    public void setHandsSplit(boolean value) { // this should be taken out (done above)
        handsSplit = value;
    }
    
    /**
     * hasBlackjack - determines whether a player has a blackjack: the first two cards must add up to 21 (doesn't count if has split the hands)
     * @return - whether the user has a blackjack in the beginning
     */
    public boolean hasBlackjack() {
        return (hand.getHandTotal() == 21 && hand.size() == 2 && !handsSplit); // NOT for currentHand - can only have at beginning of the game
    }
    
    /**
     * displayPlayerHand - prints out the original hand and its total, and if split, prints out the split hand and its total
     */
    public void displayPlayerHand() {        
        if(currentHand == hand) { // draw the player's current hand
            System.out.print("Your hand: ");
            for(int i = 0; i < currentHand.size(); i++) {
                Card c = currentHand.get(i);
                System.out.print(c.toString() + " ");
            }
            System.out.println("\nYour score: " + hand.getHandTotal());
        }
        
        if(handsSplit) { // if the player's hand was split, draw this as well
            System.out.print("Your split hand: ");
            for(int i = 0; i < splitHand.size(); i++) {
                Card c = splitHand.get(i);
                System.out.print(c.toString() + " ");
            }
            System.out.println("\nYour split hand score: " + splitHand.getHandTotal());
        }
    }
    
    /**
     * returnHandsToDeck - discards any hands with cards in them and returns them to the specified deck
     * @param deck - the deck to which the player returns the cards
     * @throws java.lang.NullPointerException if deck is null
     */
    public void returnHandsToDeck(Deck deck) throws NullPointerException { // clean up the hands and return all cards to the deck               
        if(deck == null) {
            throw new NullPointerException("This deck does not exist.");
        }
        
        for(int i = 0; i < hand.size(); i++) {
            Card c = (Card)hand.get(i);
            if(c.getValue() == 1) {
                c.knockUpAceValue(); // return the ace to its normal value
            }
            c.setFaceUp(true); // make sure everything is face up again
            deck.addCard(new Card(c)); // put a new Card object back in the specified deck
        }       
        hand.discardHand(); // clear the hand
        
        // if the hand was split, do the same here
        if(handsSplit) {
            for(int i = 0; i < splitHand.size(); i++) {
                Card c = (Card)splitHand.get(i);
                if(c.getValue() == 1) {
                    c.knockUpAceValue();
                }
                c.setFaceUp(true);
                deck.addCard(new Card(c));
            }
            splitHand.discardHand();
        }       
    }
    
    /**
     * getName - returns the player name
     * @return - this player's name
     */
    public String getName() {
        return playerName;
    }
    
    /**
     * getMoney - getter method to get how much money the player has
     * @return - the player's total money
     */
    public int getMoney() {
        return playerMoney;
    }
    
    /**
     * getBet - returns the bet placed on the original hand
     * @return - the bet on the original hand
     */
    public int getBet() {
        return playerBet;
    }

    /**
     * setMoney - sets how much money a player has
     * @param money - how much to give the player
     */
    public void setMoney(int money) { // if the user wishes to buy back chips
        playerMoney = money;
    }    
    
    /**
     * setBet - sets the bet that a player has
     * @param betValue - what the value of the player's original bet is
     */
    public void setBet(int betValue) {
        playerBet = betValue;
    }
    
    /**
     * getSplitBet - returns the value of the bet on the split hand
     * @return - the split bet value
     */
    public int getSplitBet() {
        return splitBet;
    }
    
    /**
     * setSplitBet - determines the bet that is placed on the splitHand
     * @param betValue - what to set the splitBet to
     */
    public void setSplitBet(int betValue) {
        splitBet = betValue;
    }
    
    /**
     * canSplit - decides whether or not a player can split his hand: he can't have done so already, he must only have 2 cards, and they must be of equal value (flexible splitting)
     * @return - whether a user can split a hand
     */
    public boolean canSplit() {
        return (!handsSplit && hand.size() == 2 && ((Card)(hand.get(0))).getValue() == ((Card)(hand.get(1))).getValue());
    }
    
    /**
     * hasSplit - determines whether the user has already split the hand
     * @return - whether user has already decided to split
     */
    public boolean hasSplit() {
        return handsSplit;
    }
    
    /**
     * setActive - decides whether this player is actively participating in the hand
     * @param value - true or false: whether player is in this hand
     */
    public void setActive(boolean value) {
        active = value;
    }
    
    /**
     * isActive - determines whether the player is active in a hand
     * @return - whether the player is participating in the hand
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * getCurrentHand - gets which hand is currently being used
     * @return - the reference to the hand that is being used
     */
    public BlackJackHand getCurrentHand() {
        return currentHand;
    }
    
    /**
     * getInitialHand - returns a reference to the original, non-split hand
     * @return the hand private instance variable reference
     */
    public BlackJackHand getInitialHand() {
        return hand;
    }
    
    /**
     * getSplitHand - returns a reference to a splitHand
     * @return - the splitHand private instance variable reference
     */
    public BlackJackHand getSplitHand() {
        return splitHand;
    }
}
