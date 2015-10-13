README - Blackjack
Programmer: Michael Hergenrader
USCID: 5048471246
9/27/09

All files:
README
COMPILE
Blackjack.java
BlackJackGame.java
BlackJackPlayer.java
BlackJackHand.java
Hand.java
Card.java
Deck.java
TestHands.java (seen in the Testing section)


Design:
--------------------------------------------------------

There are a total of seven classes in this source, each with different main responsibilities.

The Blackjack.java file
----------------------------------
handles all initial input, allowing the players to enter how many of them will play this game, how many decks to use, the betting
limit for the table, and the individual stats for the players themselves, which includes each of their names and their values.
There are no instance methods or variables in this file - the main method handles all input and passes execution to the main game
class, BlackJackGame. There are limits to some variables: the betting limit must be a positive integer greater than 0, the number
of players must be between 1 and 5, their money values must be positive integers greater than 1, the number of decks must either
be 2, 4, or 6, and the names must not be blank.

The BlackjackGame.java file
-----------------------------------
handles all ingame events, such as printing the console interface, allowing the user to do different commands such as hitting,
standing, doubling down, splitting, betting, and quitting. This game includes multiple instance variables:

-private int bettingLimit;

is what is given from the inputted variable in the BlackJack.java file. There, a user inputs what the limit is, and this is passed
into the constructor that initializes this variable.

-private int numberOfDecks;

is the number of decks that is inputted by the users of this game. This is passed to the BlackJackGame constructor and then
initialized in this variable. A player can choose between 2, 4, and 6 decks.
    
-private ArrayList<BlackJackPlayer> players;

is the main list of players for the game. An ArrayList was chosen for its efficiency that quickly obtained elements from a list
through random-access, constant time operations. This list is traversed each time for obtaining a bet, printing out an element
(player's information), or asking the user for a command, either one that dictates the action for a hand, or an action for whether
to bet or skip the hand or quit the game entirely (in this case he is removed from the list). When this list is empty, as in every
player has quit the game, the program exits.

-private BlackJackPlayer currentPlayer;

is a reference variable used by multiple methods in this file as a sort of iterator that will handle each player when it points to
a particular individual. It is set back to the beginning of the players ArrayList after each completed hand.

-private Deck gameDeck;

is the "shoe", another term for a "collective deck full of individual decks". It contains every card of either 2, 4, or 6 standard
card decks. Although jokers are supported by the deck class, they are not included in standard Blackjack. Cards are removed from
the deck when a user gets a hand or when the dealer does. After a hand is complete, all cards are set face up and returned to the
pile. In the GUI version to come, they will be set face down, for obvious reasons, but for now, the easiest and most efficient way
to maintain the face properties of cards is to keep them face up and "turn over" the card this is first dealt to the dealer so it
can be secret.
    
-private BlackJackPlayer dealer;

represents the dealer who is not part of the ArrayList, as it has his own methods to worry about, like manageDealer, a method that
relies entirely on this variable. It is kept separate because in order to remove a player from the game via "quitting", an iterator
must be used with the remove() method - if the dealer were to be kept inside the ArrayList, it would be treated as a player, and
would be prompted for a bet and for action, when it should actually be automated by class methods.

This class also includes multiple class methods, including a few constructors:

+public BlackJackGame(int bettingLimit, int numberOfDecks, ArrayList<BlackJackPlayer> players)

is the main constructor, and only usable one, for the class. It requires input from the Blackjack.java file that contains the
variables listed above, with restrictions taken care of by the other class. The construtor copies the betting limit and number
of decks into the above instance variables. Then, it performs a deep copy on the passed array list that is given to the above
class ArrayList - putting each created player inside this data structure. Finally, it initializes the dealer to an arbitrary
name and money amount (as these values aren't used by the dealer; it has the properties of a BlackJackPlayer, but some values,
like these are not used), constructs and builds the gameDeck, and calls the mainGame method to start the game.

-private BlackJackGame()

is a no-arg constructor that is not allowed for use because it is too vague - a game requires players; in this case, they must
come from the command prompt and user input, which is what the game relies on. Thus, because of the focus on correct user input,
this constructor is private so that users must give values to the class.

-private void mainGame()

is the principal method for the entire class. It first prompts each individual player, asking them whether they would like to bet,
skip the upcoming hand, or quit the game entirely. If they choose to bet, it sets their bet and moves on to the next one. If a
player specifies they want to skip this hand, they will be set as inactive for that round, and will be allowed no hands, betting,
or any other action until the hand terminates. If they quit, the method removes them from the list entirely and moves on.

Then, for every player in the list, it gives them cards, sets the default values for each player (such as their hand statuses), and
invokes the inGameMenu method to get user input and commands for each hand. Once this ends, the method will check if the dealer
needs to do anything (see the Casino Rules section below), let it perform actions on the hand if so, and then manage the scoring,
calling manageScoring(). Finally, it cleans up after each player, returning their hands to the deck, and then shuffles the deck.
In this casino, like many developing ones, there is a "continuous card shuffler" that shuffles the entire shoe after each hand
played to prevent counting. The last step of the method is to check whether every player still has money. If any don't, it prompts
each one about whether they would like to "purchase" new betting money, invoking promptToBuyMoreChips.

-private void inGameMenu()

handles the printing of all data, including the cards for all players and the dealer, and processes player commands. First, it
checks whether the dealer or current user (one it is testing for; it iterates through the ArrayList of players) has blackjack - if
so, check if there is a tie, and process accordingly, skipping the rest of the entire method to move on to the payout or loss for
each player (depending on who has the blackjack).

If neither side has one, it moves on to a loop that prompts a user for a character input command telling the game whether he or she
would like to hit, stand, double down, split, or surrender. It checks for invalid input through a switch statement, in which the
default is a bad input. Each one of these tests whether it is allowed. For example, one cannot double down after he has already
hit, and a one cannot surrender after he has hit, doubled down, or split the cards. And, a user cannot split twice.

Finally, the method tests whether another hand should be processed (if the user split his hand) - if the first hand is done, then
the current hand is set to the split one. Else, the method exits. If the current hand is the split one already, and this one is
finished, the method returns so that mainGame can process each player for scoring and the dealer himself.

-private void displayDealerHand()

is a visual method that simply displays what cards are present in the dealer's hand, called not only at the beginning of a hand (for
every player), but also for every step that the dealer performs. It is invoked by mainGame and manageDealer, as specified. When a 
card is turned "face down", a "#" character is drawn to represent the currently unknown card.

-private void manageDealer()

provides the implementation for the dealer actions - as long as it has under 17 as a score, it must continue to hit. It also tests
for blackjack and for when the dealer may bust (if it has over 21). It prints out each step, according to displayDealerHand.

-private void manageScoring()

provides the implementation for standard Blackjack scoring, specified below in the Casino Scoring section. It compares any active
hands for each player (ones that haven't busted or been surrendered) against that of the dealer and either takes away the player's
bet if he lost, or provides payout to that player if he won.

-private void displayGameMenu()

is the visual printout method for all the available actions a player can perform. If the conditions do not allow a command, it will
not be part of the list (if the user picks this option, inGameMenu will catch it and not execute that command). This is the visual
"UI" for the player and comes up after every action of every hand for every player, presenting information like the contents of the
hand, the current score, the dealer's hand contents, the bet, the current amount of money, etc.

-private void addCardToHand(Hand myHand)

is the method that adds a particular card to a hand. It is used for the beginning setting up of a hand, and for hitting, doubling
down, and splitting. The Hand parameter specifies which hand to give this card to - it is added to the end of it.

-private boolean promptToBuyMoreChips(BlackJackPlayer p)

is a method that checks if a user without any money ($0) would like to continue the game. It will first ask this question - if the
player, p, declines, then it will return false, and this player will be removed from the list entirely in mainGame. Otherwise,
it will ask him for how much he actually wants, give it to his player account, and keep him in the game, returning back to the
normal execution flow.


The BlackJackPlayer.java file
-------------------------------------
handles all actions for a player in the game. It encompasses his bets, his hands (both original and split, if necessary), his name,
his total money, and whether he is active. There are multiple instance variables to speak of:

-private String playerName;

represents the player's name, given to the program through user input in BlackJack.java.

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

These are all setter methods that modify the instance variables mentioned above, each corresponding to the appropriate variable
through the method name.

+public String getName()
+public int getMoney()
+public int getBet()
+public int getSplitBet()
+public boolean hasSplit()
+public boolean isActive()
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
In a linked list of Card objects, represents what a true hand would be in a real game. It contains the Cards, provides a way of adding up their values, and includes
methods that will get and remove certain parts of it, abstracting the privatized linked list. Finally, it includes a discardHand() method to clear everything to
prepare for the next hand. In short, this class's purpose is to represent the collection of cards a player receives in a game and what operations are done to them.

There is only one instance variable:
#protected LinkedList<Card> playerHand;

represents the hand through a linked list because of the efficiency of small linked lists, being able to add elements efficiently
to the end and remove and iterate through them as well. It is protected to make this class subclassable and reusable - for
example, in this program, I subclass it to make a BlackJackHand that has more specific properties and uses, but still has the
same underlying structure.

There are multiple methods, all of which are very easy, as they correspond to the linked list methods, abstracting them to make
this Hand class seem almost a type of linked list:

+public Hand()

constructs the linked list object contained in the class.

+public void addCard(Card c)

a new card specified in the parameter is added to the end of the linked list, or Hand.

+public String toString()

returns a string representation of the Hand, printing out each individual Card object inside (its suit and its value)

+public int getHandTotal()

tallies up the total of the hand, taking into account the 1/11 flexibility of aces - it returns this sum.

+public int size()
+public Card getLast()
+public Card removeLast()
+public Card get(int index)

These methods are equivalent to those of the LinkedList<E> class, except they are defined more specifically for not just Objects,
but Cards, as seen in their return types. Their purpose is to help encapsulate the linked list inside the Hand class, calling the
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

-private List<Card> shoe;

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

+public void addCard(Card c)

This method adds the card specified in the parameter to the List, provided a null element is not passed into it.


Testing:
-----------------------------------------------------

The TestHands class tests both the validity of the Hand.java file and the BlackJackHand.java file because it invokes all their
methods and shows that the cards in each of these are always accounted for and reflect a working hand. It goes even further to
prove that empty hands work in terms of determining their size and contents. Finally, it proves that the methods that parallel
those of the List work exactly the way they do. If there is an empty list, for example, an Exception won't be thrown like the List
does because they it will return null in case of an empty list. The user must check this, as it is specified as a post condition.
Only removeLast is included in the actual test because it exactly parallels the other two methods, representing the three
separate methods: removeLast, getLast, and get(index) as the same test.

The rest of the classes were tested as a whole, as the Card and Deck classes were tested before this second assignment, and the
other classes were already mostly tested, except for the new features, like the dealer, that were accounted for. All of these
features have been individually tested as the program progressed inside the BlackJackGame.java, and bugs were eliminated during
each step.


Casino Scoring and Rules:
-----------------------------------------------------
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


COMPILE
Michael Hergenrader
USCID: 5048471246
9/27/09
CSCI 200 - Fall 2009

To compile the program, type in:
javac Blackjack.java BlackJackGame.java BlackJackPlayer.java Hand.java BlackJackHand.java Card.java Deck.java

and press enter.

To run the program, type in:
java Blackjack
and press enter as well.

