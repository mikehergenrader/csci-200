package blackjack;

/**
 *
 * @author Michael Hergenrader
 */
public class Card {
    public static final int NONE = 0, HEARTS = 1, DIAMONDS = 2, CLUBS = 3, SPADES = 4; // represents the suits
    
    private int value; // the Card fields
    private int suit;
    private String cardRepresentation;
    
    private boolean faceUp; // used only for the dealer in this particular game
    
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
     * knockDownAceValue - if an Ace's value is 11, make it 1
     */
    public void knockDownAceValue() {
        value = 1;
    }
    
    /**
     * knockUpAceValue - if an Ace's value is 1, make it 11
     */
    public void knockUpAceValue() {
        value = 11;
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

/*
CSCI 200 - HW #1 - Blackjack with Console Interface
Michael Hergenrader
USCID: 5048471246
Email: mhergenr@usc.edu

Compiling Information:
To compile this program, open up the minibuffer in emacs and type in the following:
javac Blackjack.java BlackJackGame.java Card.java Deck.java Hand.java

This will compile all files together. To run the program, at the command prompt, type in:
java Blackjack

and this will start the program.

THE INTERFACE:

In a hand, the interface will display XY. X is the value of the card, and Y is the representation of the suit as its first letter.

DESIGN:

The overall design consisted of five classes, each with a specific function. In one manner or another, they invoke one another
in order to fulfill their own operations.

The main class is Blackjack.java - this file includes the main method, which prompts for user input in terms of their name,
how much money they would like to start with (in playing against the House), the betting limit they would like to have enforced, and
the number of decks to be included in the shoe, which will hold all Card objects. The Hand class is composed of Card objects, and the ideas
of a players hand are enforced by the Hand class itself as well as BlackJackGame.java, which houses all the rules. Now, these classes shall
be introduced individually:

Blackjack.java:

The purpose of this class is to take user input for the program at the beginning, before the actual game loop starts. It will ask for the name,
initial amount of money, betting limit for the table, and how many decks should be used in the multi-deck shoe. It enforces that all of these
values will not be corrupt or of the wrong type, checking both their values, if they are the correct type, to see if they make sense, as well as
checking their types: if the type is wrong, an exception will be caught (the specific type of this depends on which input is done) and the program
will continue and inform the user of his mistake. It uses the Scanner class throughout the main method to gain all this input.

The main method is the only method of this class, and the only variables are all local to it: they represent the four input areas that must be
incorporated in order to start the game. At the end, a BlackJackGame instance is constructed, and this is how to start the game.

BlackJackGame.java:

This class handles all of the rules, output, and input from the player inside its methods. It will first initialize all the fields in the constructor,
then send the execution off to the mainGame method, which will put default values inside the actual game variables and prompt the user for their bet,
making sure the input is valid, both in terms of type and size. Afterwards, if the user hasn't exited by entering a "q" or a "Q" (case insensitive), 
the game will start, drawing how much money the user has and their dealt hand, followed by the enabled commands in a menu-style input format.

The user will type in a correct value (but if they don't, it will be caught successfully), and the program will execute that command inside the inGameMenu
method of the class. This will allow for hitting, standing, doubling down, splitting, surrendering, and exiting. If a user hits, it will check if he has
busted or has aces. He can stand, ending the current hand. He can double down, doubling his bet and receiving only one new card. He can split the hand (see
below for the exact "casino" regulations) and receive a new card, he can surrender his hand before play has truly commenced, and finally, can exit the 
program quickly.

Overall, this class's responsibility is to incorporate the casino regulations and general Blackjack rules into code, combined with a user interface displayed
through the command prompt in Unix.

The constructor will take input to start a game - there is no default constructor, since this game requires input to start the game, making it have a more
personal experience. The mainGame method is invoked by the constructor to prompt the player for his bet, and to manage all the hands that a user might play.
This method calls the inGameMenu method, which will display what commands a user can do, as well as match their responses to the overall game. Finally, this
will calculate the scoring given out by each hand and clear them for the next hand. In short, mainGame will manage the hand transitions, and inGameMenu will
be more specific, taking care of all the details of each hand, but it is not alone. The displayGameMenu() and displayPlayerHand() methods will output the current
menu options, as well as the hand information, respectively. The addCardToHand method will remove a Card object from the top of the deck and place it in a player's
"hand."

Card.java:

This class's responsibility is to represent a card used in a Blackjack game - it has a suit, a value, and a way to represent it. The final term signifies that 10 =
JACK = QUEEN = KING, for example, and all have the same value, but not the same String representation. This final term, thus, gives a way to differentiate them in
this way in the absence of a feasible GUI. The constructors can both instantiate a Card from raw data, and also from another Card (essentially providing a deep copy).
Most of the methods are getter methods in this class, passing back its three data parts. However, there are two final interesting methods to take note of that are
more specific to a Blackjack game: knockDownAceValue and knockUpAceValue: these provide the flexible rule in the game that an ACE may represent 1 or 11. In the game,
invoking one of these methods changes the value manually to ensure that the hand scoring is working properly. The getHandTotal in the Hand class invokes these to
manually enforce that the scoring works.

Hand.java:

In a linked list of Card objects, represents what a true hand would be in a real game. It contains the Cards, provides a way of adding up their values, and includes
methods that will get and remove certain parts of it, abstracting the privatized linked list. Finally, it includes a discardHand() method to clear everything to
prepare for the next hand. In short, this class's purpose is to represent the collection of cards a player receives in a game and what operations are done to them.

Deck.java:

The final class uses an ArrayList to store all of its cards, since efficiency and storage are the main goals. The name is a bit of a misnomer; it should be called
a "shoe", which is a gaming term for more than one deck compiled into a single "deck." Yet the description demanded it be named "Deck" and all the operations are
the same, regardless of the name; therefore, "Deck" it is. There are three constructors. The first makes an actual deck of 52 cards without jokers and shuffles them
up (this is the default/no-arg constructor). The second takes a single parameter, in which it tells the class to create a "shoe" containing a specific number of decks.
Jokers are also not included in this one. Finally, the third asks whether the user would like jokers (this is the only difference when it is compared to the second). If
"true" is placed as the second parameter, the buildDeck method will include jokers as well. And like the others, it shuffles all the cards.

The buildDeck method is invoked by all constructors, creating 52 cards for each deck included in the total, differentiating between 10s and Jacks, etc. It will
add jokers if told to do so. The method printDeck is only for testing - it will print out all the elements of the deck using the toString of the Card class. The
shuffleDeck method invokes Collections.shuffle(), so as not to reinvent the wheel. And the last two operations are to take a card off the top, and to put a discarded
one back underneath. The main purpose of this class is therefore obvious: to handle all the operations of the game deck for Blackjack.

TESTING:

These classes have all been tested exhaustively, using both unit and system testing. The Card class was done first, constructing cards with various values and printing
them out. This was the easiest, since most of the methods are getters.

The Deck class was done next, constructing different kinds of Decks of all sizes and making sure that buildDeck worked properly. The class relies on Collections.shuffle()
to do the shuffling, so there were no problems there, and printDeck was invoked numerous times.

The Hand class was logically next, done in a way that the Card objects would successfully be accessible in a linked list while still having this structure
abstracted and encapsulated to a "Hand" object.

The most difficult class to test was the Blackjack.java with the main class because it relies completely on user input to do anything - before the BlackJackGame.java
class itself can be instantiated. Negative numbers, empty String objects, Doubles, and random Strings were just a few of the inputs used to attempt to crash the program. All
were successfully found and stopped. Any values can be used to test this class and it will now work successfully.

The BlackJackGame class was also moderately difficult: not only from faulty user input that was found through plugging in a lot of bogus values, but also from the
combinations of all the rules placed into a single class. For example, splitting must be able to have a hit, to double down, etc. The five-card charlie rule was
incorporated as well. And the drawing had to be in a clear manner, so the user could identify how much money remained, what his hand is, and what bet he has placed.
All cases were tested: 5-card charlies, Aces, split cards, busting, standing at certain values (in all the various scoring ranges), doubling down (and attempting to
twice or after a hit), trying to split twice, trying to surrender after more cards have been dealt. In addition, numerous user entries were input, like bets that
were too high or too low, wrong commands, trying to do something that wasn't displayed, etc.

All these tests contributed to the success, bug-free nature of this program - they prove it works because of the widespread amount of tests, with very differing
inputs. In addition, multiple other people were employed to verify the veracity of these tests by trying to break it themselves. In the end, no attempt to damage
the program execution was successful.

CASINO RESTRICTIONS AND RULES:

Scoring:
 2x the bet: 21 at any time, or a five-card charlie;
 1.5x the bet: 19 or 20;
 1x the bet: 18 (bet returned);
 0x the bet: less than 18 (bet lost);
 .5x the bet: surrendering before any action taken

Rules/Restrictions:
 A user may split the cards once, and only once, following model casinos that employ flexible splitting, i.e. a 10 and a Jack may be split - the value is the same.
 A user may double down, split or not, as his first move.
 The bet is instantly subtracted from the player's total money, and the scoring rules applied to his bet at the end of the hand create a new amount to give back, if any.


*/