Michael Hergenrader
CSCI 200 Fall 2009
10/11/09
USCID: 5048471246
HW3: Blackjack GUI program

NOTE: This readme file includes everything about the concrete, non-GUI classes.
For information about the GUI classes of this program, see the GUI_README.

Files included in this program:

.JAVA files
BetInputDialog.java
Blackjack.java *
BlackJackFrame.java 
BlackJackGUICard.java 
BlackJackHand.java *
BlackJackIntroDialog.java
BlackJackPlayer.java *
BlackJackSplashScreenDialog.java 
Card.java *
ClosingListener.java 
Deck.java *
GameCommandsPanel.java
GameTablePanel.java 
Hand.java *
HandResultsDialog.java 
MessagesPanel.java 
PlayerDisplayPanel.java 
PlayerIngameDataPanel.java
PlayerInputPanel.java 
PlayerRegistrationDialog.java

* = included in this README discussion

Images:
yellowchip.jpg
bluechip.jpg
orangechip.jpg
redchip.jpg
whitechip.jpg
blackchip.jpg
michaels_blackjack_table.jpg
blackjack_icon.gif
blackjack.jpg


Program Design:
-----------------------------------------------------------------

The Blackjack.java file
-------------------------------------

This file includes only the main method which invokes a new instance of BlackJackFrame, the true class that oversees everything,
including all drawing, events, and event responses in the game. This class is just used as a driver class for the rest of
the application, instantiating BlackJackFrame.java to do the rest.

+public static void main(String[] args)

does not use the input args, and just instantiates a new instance of BlackJackFrame.


The BlackJackPlayer.java file
-------------------------------------
handles all actions for a player in the game. It encompasses his bets, his hands (both original and split, if necessary), his name,
his total money, and whether he is active and enabled. There are multiple instance variables to speak of:

-private String playerName;

represents the player's name, given to the program through user input in the PlayerInputPanel

-private String playerNickname

represents the player's nickname, given to the program by the PlayerInputPanel

-private int playerMoney;

is another variable whose value is determined by user input at the beginning. It represents how much money he or she has. If it is
zero, the game will check if the player would like to continue playing, by "purchasing" more chips.
    
-private BlackJackHand hand;

represents the current/original hand of a player; that is, the one that exists before and after he or she splits the cards. It uses
the BlackJackHand class to operate all of its actions and check its properties; i.e. whether a player has busted on this hand or 
surrendered it. It is cleared after every round.

-private BlackJackHand splitHand;

represents the hand that is created after the user splits the cards. It receives the second card from the original hand and one
more from the game deck. It is cleared after every round and modified by player commands, if the rules of splitting are allowed
during that time.

-private BlackJackHand currentHand;

is a reference variable to the hand that is currently being operated upon: if the hands are split, it passes between them, allowing
a more flexible way to modify the contents of each hand. After every round, it is set back to the original hand.
    
-private int playerBet;

represents how much a player has bet during a round - it is set during the prompt at the start of a round; if a user chooses to skip
a hand, then this is not set, nor is it used because that player is ignored. It is the value that is the basis if the scoring, the
rules of which are seen in Casino Scoring.

-private int splitBet;     

represents a separate bet that is set equal to the playerBet variable when the hands are split. They are maintained as separate
bets for separate hands because one hand could lose and the other could win, etc. They are composed inside of the player - he or
she modifies these; thus, they are not a part of the Hand or BlackJackHand classes.

-private boolean handsSplit;

is a boolean variable that tells whether the hands are split (if the value is true, they are).

-private boolean active;

tells whether a player will be involved in the current round of betting. If he or she is not active for a hand (they enter s
at the betting prompt), they will not have anything in their scope affected until they return to the actual game.

There are also many instance methods, many of which are simple getter and setter methods: since they are so similar and self-
explanatory, they will be grouped together.

-private boolean disabled;

tells whether a player has left the table for good in this instance of the application. When it is true, the player is skipped
in all actions, including betting and printing data - his "table spot" is left open

-private BlackJackPlayer()

For this game, one cannot construct a default blackjack player, as input dictates the name and the starting money amount that are
given to every BlackJackPlayer instance.

+public BlackJackPlayer(String playerName, int playerMoney)

This constructor takes the input of a player name and the amount of money to start with, sets the appropriate instance variables,
and then calls the helper method, setupPlayer.

+public BlackJackPlayer(BlackJackPlayer other)

This constructor works exactly like the one above, with the exception that it copies a player from another one - this is used
in the BlackJackGame constructor to get the ArrayList from the BlackJack and perform a deep copy through this constructor - getting
every part of this object and placing it in the players ArrayList for the game.

-private void setupPlayer()

This is a helper method that initializes the two hands that a player has, sets the player as active by default, dictates that the
hands are not initially split, and sets the currentHand to hand.

+public void setupForHand()

This is called outside the class to do set some of the defaults mentioned in the helper method above, as well as not reassign the
hands to new memory, but clear their properties, setting them to false, so that a player's busted hand will not pass to the next
round.

+public void returnHandsToDeck(Deck deck) throws NullPointerException

This removes all cards from the specified hand and puts them back into the deck specified as the parameter. If there is no deck, as
in it is equal to null, the exception is thrown. During this time, the method sets all the cards to be face up to prevent any
unknown cards hanging around inside the "shoe".

+public void displayPlayerHand()

Draws out all the contents of a player's hand (and the split one, if the player has split his hands) - it also prints out each
corresponding score.

+public void setCurrentHandToOriginal()
+public void setCurrentHandToSplit()

These two methods modify the currentHand instance variable to point to one hand or the other - these are used for when the current
hand needs to be switched so that the inGameMenu method can modify the appropriate hand and switch between the two seamlessly.

+public void setHandsSplit(boolean value)
+public void setMoney(int money)
+public void setBet(int betValue)
+public void setSplitBet(int betValue)
+public void setActive(boolean value)
+public void setDisabled(boolean value)

These are all setter methods that modify the instance variables mentioned above, each corresponding to the appropriate variable
through the method name.

+public String getName()
+public String getNickname()
+public int getMoney()
+public int getBet()
+public int getSplitBet()
+public boolean hasSplit()
+public boolean isActive()
+public boolean isDisabled()
+public BlackJackHand getCurrentHand(): returns the currentHand reference variable (which hand is currently actively being used)
+public BlackJackHand getInitialHand()
+public BlackJackHand getSplitHand()

These are all getter methods returning the instance variables of the class so their values can be interpreted by other classes
in the program. All match to the instance variables that share the their names.

+public boolean canSplit()

This checks the conditions as to whether a player can split his cards - they must be of the same value, he cannot have already
split the cards, and the size of his ORIGINAL hand (as you cannot split twice) must be two, and unmodified (no other prior action
could have occurred).


The Hand.java file:
-----------------------------------
In a linked list of BlackJackGUICard objects, represents what a true hand would be in a real game. It contains the BlackJackGUICards, provides a way of adding up their values, and includes
methods that will get and remove certain parts of it, abstracting the privatized linked list. Finally, it includes a discardHand() method to clear everything to
prepare for the next hand. In short, this class's purpose is to represent the collection of cards a player receives in a game and what operations are done to them.

There is only one instance variable:
#protected LinkedList<BlackJackGUICard> playerHand;

represents the hand through a linked list because of the efficiency of small linked lists, being able to add elements efficiently
to the end and remove and iterate through them as well. It is protected to make this class subclassable and reusable - for
example, in this program, I subclass it to make a BlackJackHand that has more specific properties and uses, but still has the
same underlying structure.

There are multiple methods, all of which are very easy, as they correspond to the linked list methods, abstracting them to make
this Hand class seem almost a type of linked list:

+public Hand()

constructs the linked list object contained in the class.

+public void addCard(BlackJackGUICard c)

a new card specified in the parameter is added to the end of the linked list, or Hand.

+public String toString()

returns a string representation of the Hand, printing out each individual BlackJackGUICard object inside (its suit and its value)

+public int getHandTotal()

tallies up the total of the hand, taking into account the 1/11 flexibility of aces - it returns this sum.

+public int size()
+public BlackJackGUICard getLast()
+public BlackJackGUICard removeLast()
+public BlackJackGUICard get(int index)

These methods are equivalent to those of the LinkedList<E> class, except they are defined more specifically for not just Objects,
but BlackJackGUICards, as seen in their return types. Their purpose is to help encapsulate the linked list inside the Hand class, calling the
actual linked list methods themselves.

+public void discardHand()

This calls the clear() method of the linked list class to remove all contents from the hand - that is, all cards. This method
is employed by the BlackJackPlayer class under returnCardsToDeck.


The BlackJackHand.java file:
-----------------------------------
This is a subclass that inherits everything from the Hand class, specifying specific properties of Blackjack hands, in contrast
with those of other games. For that reason, its only true instance variables are boolean variables that apply to Blackjack itself
(other variations of the game). They are included here, and not in the player class because they apply specifically to a Hand
object itself - if they were in the BlackJackPlayer class, then the player would have to keep track of all these properties for
every hand he might have, which, in some versions, might even have multiple splits (this version dictates that only one splitting
is allowed, as it is the most profitable for a casino.

-private boolean stood;

represents whether the player who owns this hand stopped play with it (stood). If this is true, this hand's methods need not be
used any longer in a particular round, as the player has ended any action with this hand.

-private boolean busted;

represents whether or not the total is over 21, as in this hand has been busted.

-private boolean doubledDown;

represents whether or not this hand has been doubled down upon.

-private boolean surrendered;

represents whether or not the player surrendered this hand.

All of the above properties are just used to help determine other events that will occur in BlackJackGame.java.

There are also small instance methods:

+public BlackJackHand()

This constructor, the only one, a no-arg one, simply calls the superclass Hand constructor implicitly to create the LinkedList and
then sets all of the above properties to the default value: false, as these should not be true after a hand is done and continue
into the next one.

+public void resetHand()

This method is the helper method used by the above constructor to set all the values to false - it is public because BlackJackGame
also invokes it to reset these values after each round, to prevent carrying bad values to another round.

+public void setStood(boolean value)
+public void setBusted(boolean value)
+public void setDoubledDown(boolean value)
+public void setSurrendered(boolean value)

These are self-explanatory setter methods for the values above.

-public boolean isStood()
-public boolean isBusted()
-public boolean isDoubledDown()
-public boolean isSurrendered()

These are the getter methods for the boolean values whose names match those of the methods.

-public boolean isFiveCardCharlie()

This method tests the current BlackJackHand object to see if it possesses the traits of a "Five-card charlie" implemented in this
"casino": there must be five cards (checks through the superclass method size() to see if there are 5) and the score must be under
21, which is verified through the superclass method getHandTotal. It is invoked to check if a user obtained this rare feat in
BlackJackGame.java.

-public boolean isComplete() 

is just a combination of the above five getter methods - if any is true, this returns true. It is mainly used to easen the logic
in the main program, as any of these conditions will make a hand complete for a round.


The Card.java file:
------------------------------

This class's responsibility is to represent a card used in a Blackjack game - it has a suit, a value, and a way to represent it. The final term signifies that 10 =
JACK = QUEEN = KING, for example, and all have the same value, but not the same String representation. This final term, thus, gives a way to differentiate them in
this way in the absence of a feasible GUI. The constructors can both instantiate a Card from raw data, and also from another Card
(essentially providing a deep copy). Most of the methods are getter methods in this class, passing back its three data parts. 
However, there are two final interesting methods to take note of that are more specific to a Blackjack game: knockDownAceValue 
and knockUpAceValue: these provide the flexible rule in the game that an ACE may represent 1 or 11. In the game, invoking one of 
these methods changes the value manually to ensure that the hand scoring is working properly. The getHandTotal in the Hand class 
invokes these to manually enforce that the scoring works.

There are few instance variables (and a few constants) in this class:

+public static final int NONE = 0, HEARTS = 1, DIAMONDS = 2, CLUBS = 3, SPADES = 4;

These constants represent the four suits that any card can have. Jokers are the only card that invoke the NONE constant value, 0,
because they have wild values, apart from the fact that standard Blackjack (this casino) does not use them.

-private int value;

represents the value that a card can have - from 1 to 11 in this game (some cards have different representations, as seen below)

-private int suit;

has a value of one of the above four suits - represents the suit of a playing card.

-private String cardRepresentation;

represents the string/"graphical" value of a card; it is used primarily for face cards; it is what a person sees and calls a card:
i.e. Jacks are not referred to as 10's. Here, the representation for a 10 could be "10","J","Q", or "K" and an "A" could be 1 or 11.
    
-private boolean faceUp;

represents whether the suit and value should be displayed, as in, whether a card has its information facing up or not.

There are, like the Hand class, also many simple getter and setter methods, in addition to some constructors.

+public Card(int suit, int value, String cardRepresentation)

is the general constructor that creates a card with a suit, a value, and what it looks like to the user (face cards have special
representations, as explained above). It initializes a card by default to be facing up.

+public Card(Card otherCard)

creates a new Card object from an existing one, almost like a copy constructor. It performs a deep copy of the other card's fields
and whether or not it is face up. It is mostly for utility purposes, such as when a Card object is put into an ArrayList, like the
Deck.

+public int getSuit() 
+public int getValue()
+public String getCardRepresentation()
+public boolean isFaceUp()

These four getter methods match their respective instance variables above and return their values.

+public String toString()
-private String getSuitString()

The toString method returns a String representation of a card with its representation (its value is implicit in the representation)
and its suit. The getSuitString method is a matcher method that matches the constant suit values to strings the represent them:
for example: "H" represents Card.HEARTS. It is a helper method to toString.

+public void knockDownAceValue()
+public void knockUpAceValue()

These methods provide the flexibility of a Blackjack Ace card, which can represent 1 or 11. If the hand value is over 21, the 
knockDownAceValue can be used to make its value 1, while the knockUpAceValue will set an Ace value to 11 to make it go closer to 21.

+public void setFaceUp(boolean value)

This is a simple setter method for the faceUp variable - it will set this variable according to what is specified in the parameter.


The Deck.java file:
---------------------------------
The final class uses an ArrayList to store all of its cards, since efficiency and storage are the main goals. The name is a bit of a misnomer; it should be called
a "shoe", which is a gaming term for more than one deck compiled into a single "deck." Yet the description demanded it be named "Deck" and all the operations are
the same, regardless of the name; therefore, "Deck" it is. There are three constructors. The first makes an actual deck of 52 cards without jokers and shuffles them
up (this is the default/no-arg constructor). The second takes a single parameter, in which it tells the class to create a "shoe" containing a specific number of decks.
Jokers are also not included in this one. Finally, the third asks whether the user would like jokers (this is the only difference when it is compared to the second). If
"true" is placed as the second parameter, the buildDeck method will include jokers as well. And like the others, it shuffles all the cards.

There are a few instance variables:

-private List<BlackJackGUICard> shoe;

This is the representation of multiple decks, called a shoe, hence the name. It can either be an ArrayList or a LinkedList, and its
reference variable is a List<Card> so it can support this flexibility through polymorphism. In this particular instance, the
constructors make it an ArrayList.

-private boolean jokersAdded;

represents whether a deck should add wildcards, aka jokers. If this is set to true, the deck will include jokers and have a total
of 54 cards per deck included in the shoe. Else, each will have a standard 52 cards.

-private int numberOfDecks;

represents how many decks are actually inside the "shoe". There can be any number, despite how the Blackjack.java class only permits
2, 4, or 6 decks.

There are also multiple instance methods:

+public Deck()

The no-arg constructor creates a new Deck object, with only one deck included of 52 cards, setting the jokers as non-existent. It
then calls buildDeck() to create all the Card objects inside the ArrayList and then it shuffles them all together. 

+public Deck(int numberOfDecks)

This constructor is the same as the previous one, except for the fact that it allows an argument of how many decks of 52 cards to
include in the entire "shoe", or ArrayList. Like the first, it does not include jokers, and it calls buildDeck to create and insert
all the Card objects, shuffling them afterwards.

+public Deck(int numberOfDecks, boolean jokersAdded)

This final constructor does the exact same operations as the first two, but it includes jokers, or wildcards, so that each deck
included inside the shoe has 54 cards, and not the standard 52.

-private void buildDeck()

This is a helper method to create all card objects, one for every deck, suit, value, and representation. It creates these Card
objects, then inserts them into the actual deck. Only the constructors invoke this method, as anytime else is unnecessary.

+public void printDeck()

This is a simple testing method that prints out the contents of a Deck, invoking the toString method to get the string
representation of every value and suit of a card. The deck can be ordered or not, it doesn't matter.

+public int size()

This is just a getter method to return how many Card objects exist in the Deck. It just calls the List interface's size()
method to achieve this task.

+public void shuffleDeck()

This method utilizes the Collections.shuffle(List) method to mix up the cards in a random order, avoiding any need to reinvent the
wheel, especially since this method exists for the given Deck representation.

+public Card removeTopCard()

This method just takes off the first card from the deck and returns it for use elsewhere. It is used to give cards to players
during and before each hand commences player actions.

+public void addCard(Card c) throws NullPointerException

This method adds the card specified in the parameter to the List, provided a null element is not passed into it; in this case,
it throws a NullPointerException.


Program Testing:
-----------------------------------------------------------------

These classes were all previously tested in HW1 and HW2, and thus do not really have a defined testing context to speak of for
this homework, #3. All of these eatures have been individually tested as the program progressed inside the BlackJackGame.java of HW2, 
and bugs were eliminated during each step.


Casino Rules and Restrictions:
-----------------------------------------------------------------

Scoring:
-A user who gets a blackjack receives 1.5x their original bet, provided the dealer also does not have blackjack, in which it is a 
 push
-If a user beats the dealer, he obtains an amount equal to what he bet, as in every casino.
-If he surrenders, he forfeits half of his bet, and receives the rest back.
-If he doubles down, he can add up to what his original bet was, and if he wins, receives a value equal to this from the dealer.
-If he splits, he must add another bet that is equal to his original to the table. Each one of these hands are compared to that of
 the dealer individually. (the rules above apply to each one)
-A user who skips an entire hand will not have anything monetary affected.
-If a user obtains five cards without busting, aka a "Five Card Charlie", he will win 1.5x his original bet
-There are no higher-order "charlies".

Rules/Restrictions:
-The dealer must hit until he reaches 17 or higher. If he busts, all eligible hands that didn't bust will be paid according to the
 limits set above.
-A user may split the cards once, and only once, following model casinos that employ flexible splitting, i.e. a 10 and a Jack may be
 split - the value is the same. Each split hand may have any other command applied to it, EXCEPT SURRENDER.
-A user MAY NOT surrender after the first two cards, even if he has split hands. This MUST be his first move; in other words, there
 is only early surrender.
-A user may double down, split or not, as his first move.
-The bet is instantly subtracted from the player's total money, and the scoring rules applied to his bet at the end of the hand 
 create a new amount to give back, if any.
-As mentioned by Professor Crowley, there shall be no late surrender, as having both early and late is useless in a casino - it is
 not profitable. Thus, a user can only surrender early (see above)
-A user may not quit in the middle of a hand, once he has laid down a bet - he may only surrender, and then quit on his next prompt
 for a bet.
-Joker cards have no use in the game; they are implemented, but omitted.
-If all players in a round have either busted, obtained a five card charlie, or have surrendered, or are inactive, the dealer will
 not do anything, as his action is irrelevant and unnecessary in the round.


END OF README