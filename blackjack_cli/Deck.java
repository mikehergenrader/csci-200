package blackjack;

import java.util.*;

/**
 *
 * @author Michael Hergenrader
 */
public class Deck {
    private List<Card> shoe; // shoe representation
    private boolean jokersAdded;
    private int numberOfDecks;
    
    /**
     * Deck - default constructor/no-int: constructs just one deck without jokers and shuffles it
     */
    public Deck() {
        numberOfDecks = 1;
        jokersAdded = false;
        shoe = new ArrayList(); // this is an ArrayList because only need to insert and remove at the ends of the list (efficient)
        
        buildDeck();
        shuffleDeck();
    }
    
    /**
     * Deck - constructs a "shoe" of decks with the specified amount, then constructs and shuffles it
     * @param numberOfDecks - how many decks to include in the shoe
     */
    public Deck(int numberOfDecks) {
        this.numberOfDecks = numberOfDecks;
        jokersAdded = false;
        shoe = new ArrayList();
        
        buildDeck();
        shuffleDeck();
    }
    
    /**
     * Deck - constructs a "shoe" with a specified number of decks, then initializes and shuffles it, user can include jokers
     * @param numberOfDecks - how many decks to include in the "shoe"
     * @param jokersAdded - whether jokers should be added - if false, equivalent to previous constructor
     */
    public Deck(int numberOfDecks, boolean jokersAdded) { // doesn't quite do the right thing - jokers not added yet
        this.numberOfDecks = numberOfDecks;
        this.jokersAdded = jokersAdded;
        shoe = new ArrayList();
        
        buildDeck();
        shuffleDeck();
    }
    
    /**
     * buildDeck - initializes the deck with all 52 cards and their correct representations - used by constructors
     */
    private void buildDeck() {
        String representation;
        int cardValue;
        
        for(int a = 0; a < numberOfDecks; a++) {
            for(int i = 1; i < 5; i++) { // suit values (0 = wild/jokers)
                for(int j = 2; j < 15; j++) { // card values

                    switch(j) { // if a face card, convert it to have the correct value, but a different String representation
                        case 11:
                            cardValue = 10;
                            representation = "J";
                            break;
                        case 12:
                            cardValue = 10;
                            representation = "Q";
                            break;
                        case 13:
                            cardValue = 10;
                            representation = "K";
                            break;
                        case 14:
                            cardValue = 11; // by default - if bust, subtract 10 from this one
                            representation = "A";
                            break;
                        default:
                            cardValue = j;
                            representation = String.valueOf(j);
                    }
                    shoe.add(new Card(i,cardValue,representation));
                }
            }
        }
        
        if(jokersAdded) {
            for(int i = 0; i < numberOfDecks; i++) { // add two jokers per deck
                shoe.add(new Card(Card.NONE,12,"Joker"));
                shoe.add(new Card(Card.NONE,12,"Joker"));
            }
        }
    }
    
    /**
     * printDeck - testing method to print out the entire shoe of cards - can be shuffled or unshuffled
     */
    public void printDeck() {
        System.out.println("Shoe size: " + shoe.size());
        int a = 0;
        for(Card c : shoe) {
            System.out.print(c.toString() + " "); // separate the individual decks, if sorted will be better shown
            a++;
            if(a % 52 == 0) {
                System.out.println();
            }            
        }
        System.out.println();
    }
    
    public int size() {
        return shoe.size();
    }
    
    /**
     * shuffleDeck - shuffles up the ArrayList used in this class - all the cards are now mixed
     */
    public void shuffleDeck() {
        Collections.shuffle(shoe);
    }
    
    /**
     * removeTopCard - takes off the Card from the top of the deck and returns it
     * @return - the top card
     */
    public Card removeTopCard() {
        return (Card)shoe.remove(0);    
    }
    
    /**
     * addCard - adds a card back in to the back of the "shoe"
     * @param c - the card to add back into the shoe
     */
    public void addCard(Card c) {
        if(c != null) {
            shoe.add(c);
        }
    }        
}
