package blackjack;

import java.util.*;

/**
 *
 * @author Michael Hergenrader
 */
public class BlackJackGame extends Game {
    private int bettingLimit;
    private int numberOfDecks; // prompted variables
    
    private ArrayList<BlackJackPlayer> players;
    private BlackJackPlayer currentPlayer;
    private Deck gameDeck; // could rename this to be a "shoe" - all are shuffled together
    
    private BlackJackPlayer dealer; // the dealer (not included in the players list - has its own methods)

    /**
     * BlackJackGame - makes a new game with an established betting limit, a specific number of decks, and the list of game players
     * @param bettingLimit - the input betting limit
     * @param numberOfDecks - the input number of decks
     * @param players - the players input that are placed into the game
     */
    public BlackJackGame(int bettingLimit, int numberOfDecks, ArrayList<BlackJackPlayer> players) {
        
        this.bettingLimit = bettingLimit;
        this.numberOfDecks = numberOfDecks; // initialize the prompted variables in this object
        this.players = new ArrayList<BlackJackPlayer>();        
        //Collections.copy(this.players, players);
        
        for(BlackJackPlayer p : players) {
            this.players.add(new BlackJackPlayer(p)); // does a deep copy of the other list into this class's instance variable
        }
        
        dealer = new BlackJackPlayer("dealer",2000); // 2000 is just an arbitrary number to help create the dealer (not used)    

        gameDeck = new Deck(numberOfDecks); // default type of deck - no jokers in this game
        mainGame();
    }
    
    private BlackJackGame() { // don't let the user construct a default blackjack game - the game relies on user input
        
    }
    
    /**
     * mainGame - starts up and runs the entire program: prompts for bets, prompts for player hand actions, and delegates to other methods
     */
    private void mainGame() {        
        Scanner n = new Scanner(System.in); // ask how much a player would like to bet  
        
        String input = ""; // must initialize for catch block to recognize
        while(players.size() > 0) { // while players in the game still exist
            
            // Obtain the bets for each player - determine if he or she is playing
            Iterator<BlackJackPlayer> iterm = players.iterator();
            
            while(iterm.hasNext()) {                                
                currentPlayer = iterm.next();
                currentPlayer.setActive(true); // each player is automatically assumed to be playing a hand, unless specifies otherwise

                System.out.println("\n" + currentPlayer.getName() + "\'s turn:");
                System.out.println("Your money: $" + currentPlayer.getMoney()); // ask the user for input
                System.out.print("Please enter your bet, or q to exit, or s to skip this hand: ");
                
                // makes sure that the user inputs a valid bet, or exits the game, or skips the hand (bet = n/a)
                while(true) {
                    try {        
                        input = n.nextLine();
                        int bet = Integer.parseInt(input);

                        if(bet < 1) { // make sure the bet is high enough
                            System.out.println("Your bet must be one or higher. Please enter a valid bet: ");
                            continue;
                        }
                        else if(bet > Math.min(bettingLimit,currentPlayer.getMoney())) { // don't let user go over what they have
                            System.out.println("You cannot bet that high. The maximum you can bet is $" + Math.min(bettingLimit,currentPlayer.getMoney()) + ". Please enter a valid bet: ");
                            continue;
                        }
                        currentPlayer.setBet(bet); // create the bet, and subtract it from the money amount
                        currentPlayer.setMoney(currentPlayer.getMoney() - bet);
                        break;
                    }
                    catch(NumberFormatException e) { // make sure bet is valid input in itself, and also allow to quit
                        if(input instanceof String) { // allows user to press "q" or "s" even though it is a number input prompt
                            if(input.equalsIgnoreCase("q")) {                        
                                System.out.println(currentPlayer.getName() + " has chosen to leave the game.");
                                iterm.remove();
                                break;
                            }
                            else if(input.equalsIgnoreCase("s")) { // make some notion to skip this player in the actual game
                                currentPlayer.setActive(false);
                                currentPlayer.setBet(0);
                                break;
                            }
                        } // these shouldn't be reached if the correct input was entered - returns, or continues
                        System.out.println("Error. Invalid input. Please enter a valid bet, s to skip this hand, or q to quit: ");
                        continue;         
                    }
                }   
            }
            
            // give two cards to the dealer and set him up for the hand
            dealer.setupForHand();
            
            dealer.getCurrentHand().addCard(gameDeck.removeTopCard());
            dealer.getCurrentHand().getLast().setFaceUp(false); // can put this on either card (i.e. after the next statement as well)
            dealer.getCurrentHand().addCard(gameDeck.removeTopCard());

            for(int j = 0; j < players.size(); j++) {

                // if player is not active for this hand, then skip and move on
                if(!players.get(j).isActive()) {
                    continue;
                }

                // if player is active, then give him cards
                currentPlayer = (BlackJackPlayer)players.get(j); // cast probably unnecessary
                currentPlayer.setupForHand();

                // add in the first two cards from the top of the deck
                currentPlayer.getCurrentHand().addCard(gameDeck.removeTopCard());
                currentPlayer.getCurrentHand().addCard(gameDeck.removeTopCard());

                inGameMenu(); // ask player what to do with hand and draw the results                
            }
            
            // determines whether a dealer should act - if players have already busted, or surrendered, or all have charlies, or are inactive, the dealer doesn't have to do anything
            // this could just be one variable: who to skip - numPlayersToSkip
            int numPlayersBusted = 0, numPlayersSurrendered = 0, numPlayersInactive = 0, numPlayersWithCharlies = 0;
            for(int i = 0; i < players.size(); i++) {
                if(players.get(i).getInitialHand().isBusted()) {
                    if(players.get(i).hasSplit()) {
                        if(players.get(i).getSplitHand().isBusted() || players.get(i).getSplitHand().isFiveCardCharlie()) { // this will work now - dealer won't go if BOTH hands are busted
                            numPlayersBusted++; // now tests for mixes as well
                        }
                    }
                    else {
                        numPlayersBusted++;
                    }
                }
                if(players.get(i).getInitialHand().isSurrendered()) { // count how many have surrendered
                    numPlayersSurrendered++;
                }
                if(!players.get(i).isActive()) { // count how many are inactive
                    numPlayersInactive++;
                }
                if(players.get(i).getInitialHand().isFiveCardCharlie()) {
                    if(players.get(i).hasSplit()) {
                        if(players.get(i).getSplitHand().isFiveCardCharlie() || players.get(i).getSplitHand().isBusted()) {
                            numPlayersWithCharlies++;  // skip this player if he has both hands as charlies
                        }
                    }
                    else {
                        numPlayersWithCharlies++;
                    }
                }
            }
            // if everyone has busted or surrendered or isn't playing, dealer doesn't need to do anything (this sum also provides the case for a mix of all players not being able to compare to the dealer)
            if(numPlayersBusted+numPlayersSurrendered+numPlayersInactive+numPlayersWithCharlies < players.size()) {
                manageDealer();
            }

            // for the active players, compute what the winnings or losses are for each
            manageScoring();
            
            // the following loops could be combined
            // clean up the players and give their hands back
            Iterator<BlackJackPlayer> iter = players.iterator();
            while(iter.hasNext()) {
                currentPlayer = iter.next();
                currentPlayer.returnHandsToDeck(gameDeck);
            }

            dealer.returnHandsToDeck(gameDeck); // return the dealer's cards as well
            
            gameDeck.shuffleDeck(); // like a lot of casinos today, continously shuffles the cards once they have been returned to prevent counting   

            // Ask a user without any more money if he or she would like to continue, and "buy more chips"
            Iterator<BlackJackPlayer> iter2 = players.iterator(); // this works, but also could use a ListIterator
            while(iter2.hasNext()) {
                BlackJackPlayer c = iter2.next();
                if(c.getMoney() == 0) {
                    System.out.println(c.getName() + " is out of money!");
                    if(!promptToBuyMoreChips(c)) { // player doesn't want to buy back in
                        iter2.remove(); // take him out of the list: essentially, he or she has quit
                    }
                }
            }
        }
    }
    
    /**
     * inGameMenu - handles all user input during the game itself and delegates the display to the draw methods of the class
     */
    private void inGameMenu() {
        String userInput;
        Scanner myScanner = new Scanner(System.in);
        
        do {
            System.out.println("\n" + currentPlayer.getName() + "\'s turn:");
            System.out.println("*************************************************");
            displayDealerHand();
            currentPlayer.displayPlayerHand(); // print out the dealer and player hands for comparison (console UI)
            
            // dealer checks for blackjack right away - each player can either match it or not
            if(dealer.hasBlackjack()) {
                if(currentPlayer.hasBlackjack()) { // if both player and dealer have blackjack, push
                    System.out.println(currentPlayer.getName() + " and the dealer push on blackjack.");
                    //currentPlayer.setMoney(currentPlayer.getMoney()+currentPlayer.getBet());
                }
                else { // player loses to a dealer's blackjack
                    System.out.println(currentPlayer.getName() + " loses to the dealer\'s blackjack...");
                }
                break;
            }
            
            if(currentPlayer.hasBlackjack()) { // if just player has blackjack, then don't even do anything - he wins
                System.out.println(currentPlayer.getName() + " has Blackjack!!!");
                break;
            }
            
            // if code makes it here, then neither has blackjack
            
            displayGameMenu();  // draw the menu and check for user input as to the command
            userInput = myScanner.nextLine();
            
            try { // ask user for what to do to his current hand
                char entry = ((userInput.trim()).toUpperCase()).charAt(0); // get the first character of input to test
                switch(entry) {
                    case 'H': // hit
                        addCardToHand(currentPlayer.getCurrentHand());
                        
                        if(currentPlayer.getCurrentHand().getHandTotal() > 21) {
                            currentPlayer.displayPlayerHand();
                            System.out.println("You busted!"); // check to see if player busted
                            currentPlayer.getCurrentHand().setBusted(true);                            
                        }
                        else {
                            if(currentPlayer.getCurrentHand().size() >= 5) {
                                currentPlayer.displayPlayerHand();
                                System.out.println("Five-card Charlie!"); // user got 5 cards w/o a bust!
                            }
                        }                        
                        break;
                        
                    case 'S': // stand - do nothing to this hand
                        System.out.println("Player chooses to stand.");
                        currentPlayer.getCurrentHand().setStood(true);
                        break;
                        
                    case 'D': // double down
                        if(currentPlayer.getCurrentHand().isDoubledDown()) { // don't let it happen twice
                            break;
                        }                        
                        if(currentPlayer.getCurrentHand().size() > 2) { // prevent user from doubling down after he has already been hit
                            System.out.println("You cannot double down after you have already hit.");
                            break;
                        }                        
                        if(currentPlayer.getMoney() < 1) { // not enough money to double down
                            System.out.println("I'm sorry. You don't have enough to double down.");
                            break;
                        }

                        currentPlayer.getCurrentHand().setDoubledDown(true);
                        addCardToHand(currentPlayer.getCurrentHand()); // give player one extra card
                        
                        while(true) { // ask how much user would like to double down with
                            System.out.print("How much would you like to double down with? (up to " + Math.min(currentPlayer.getBet(),currentPlayer.getMoney()) + " dollars) ");
                            Scanner x = new Scanner(System.in);
                            try {
                                int betAddition = x.nextInt();
                                if(betAddition < 1 || betAddition > Math.min(currentPlayer.getBet(),currentPlayer.getMoney())) {
                                    System.out.print("I'm sorry. Please enter a valid input: ");
                                    continue;
                                }

                                // take the bet out of the money
                                currentPlayer.setMoney(currentPlayer.getMoney()-betAddition);
                                
                                // add it to whichever hand applies (is the one with action)
                                if(currentPlayer.getCurrentHand() == currentPlayer.getInitialHand()) {
                                    currentPlayer.setBet(currentPlayer.getBet()+betAddition);
                                }
                                else if(currentPlayer.getCurrentHand() == currentPlayer.getSplitHand()) {
                                    currentPlayer.setSplitBet(currentPlayer.getSplitBet()+betAddition);
                                }                                
                                break;
                            }
                            catch(InputMismatchException e) {
                                System.out.print("I'm sorry. Please enter a valid input: ");
                                //x.next();
                                continue;
                            }
                        }
                        
                        currentPlayer.displayPlayerHand();
                        
                        if(currentPlayer.getCurrentHand().getHandTotal() > 21) { // check if double down caused player to bust
                            System.out.println("You busted!");
                            currentPlayer.getCurrentHand().setBusted(true);
                        }
                                                
                        break;
                        
                    case 'P': // split the cards - allows inexact splitting
                        if(currentPlayer.hasSplit()) {
                            System.out.println("Casino rules: you cannot split more than once.");
                            continue; // this game only allows a player to split once - if they try again, it won't let them
                        }
                        
                        if(currentPlayer.canSplit()) { // check if player can split; if so, switch up cards
                            currentPlayer.setHandsSplit(true);
                            currentPlayer.setSplitBet(currentPlayer.getBet()); // make a separate bet
                            currentPlayer.setMoney(currentPlayer.getMoney()-currentPlayer.getBet());

                            currentPlayer.getSplitHand().addCard(currentPlayer.getInitialHand().removeLast());
                            addCardToHand(currentPlayer.getInitialHand());
                            addCardToHand(currentPlayer.getSplitHand()); // move one card to the split hand                        
                        }
                        else {
                            System.out.println("You cannot split two cards that are not of equal value.");
                            continue; // prevent splitting them if they are not of the same value - try again
                        }

                        break;
                        
                    case 'X': // surrender
                        if(currentPlayer.getCurrentHand().size() > 2 || currentPlayer.hasSplit()) { // player can't do this - can surrender after first two cards dealt only!
                            System.out.println("You cannot surrender after the initial two cards.");
                            continue;
                        }
                        if(currentPlayer.getBet() < 2) { // can't, obviously surrender half of one chip
                            System.out.println("You cannot surrender with only one chip.");
                            continue;
                        }
                        else { // lose half the bet and the hand
                            currentPlayer.getCurrentHand().setSurrendered(true);
                        }
                        break;
                        
                    default: // user did not enter a correct command
                        System.out.println("I'm sorry. That's not a valid input.");
                        continue;
                }
            }
            catch(StringIndexOutOfBoundsException e) {
                System.out.println("You didn't enter anything.");
                continue;
            }
            
            if(currentPlayer.hasSplit()) { // player has split, and first hand is done (make the current hand the split one)
                BlackJackHand h = currentPlayer.getCurrentHand();
                if(h == currentPlayer.getInitialHand()) {
                    if(h.isBusted() || h.isStood() || h.isDoubledDown() || h.isSurrendered() || h.isFiveCardCharlie()) {
                        currentPlayer.setCurrentHandToSplit();
                    }
                }
                else if(h == currentPlayer.getSplitHand()) {
                    if(h.isBusted() || h.isStood() || h.isDoubledDown() || h.isSurrendered() || h.isFiveCardCharlie()) {
                        break; // done with both hands
                    }                    
                }
            }
            else { // hasn't split, but check that he is done with this hand
                if(currentPlayer.getCurrentHand() == currentPlayer.getInitialHand()) { // might not be needed
                    BlackJackHand h = currentPlayer.getCurrentHand();
                    if(h.isBusted() || h.isStood() || h.isDoubledDown() || h.isSurrendered() || h.isFiveCardCharlie()) {
                        break;
                    }
                }
            }
        } while(true);
    }
    
    /**
     * displayDealerHand - prints out the dealer's hand - the unknown card is known by a "#"
     */    
    private void displayDealerHand() { // print a new line before this entire method (at the call
        System.out.print("Dealer's Hand: ");
        Hand h = dealer.getCurrentHand();
        for(int i = 0; i < h.size(); i++) {
            System.out.print(h.get(i).isFaceUp()?h.get(i).toString()+" ":"# ");            
        }
        System.out.println();        
    }
    
    /**
     * manageDealer - control the dealer's actions - must hit until has at least 17, also checks if he has busted
     */
    private void manageDealer() {
        dealer.getCurrentHand().get(0).setFaceUp(true); // turn the first card over
        displayDealerHand(); // initial, once turned over
        
        while(dealer.getCurrentHand().getHandTotal() < 17) {
            addCardToHand(dealer.getCurrentHand());
            displayDealerHand();
            
            if(dealer.getCurrentHand().getHandTotal() > 21) {
                //displayDealerHand();
                System.out.println("Dealer busts!"); // check to see if player busted
                dealer.getCurrentHand().setBusted(true);
                return;
            }
        }
        dealer.getCurrentHand().setStood(true); // needed? probably not, but safe
        dealer.getCurrentHand().setBusted(false);
        
        if(dealer.hasBlackjack()) { // don't do anything if the dealer has a blackjack - prevent him from printing out more
            System.out.println("Dealer has blackjack!");
            return;
        }
        
        displayDealerHand();
        System.out.println("Dealer stands at " + dealer.getCurrentHand().getHandTotal());        
    }
    
    /**
     * manageScoring - determines how the scoring is done and how winnings and losses are determined for all actions
     * also prints out what happened to every active player in the hand
     */
    private void manageScoring() {        
        System.out.println("\n" + "                 Hand Results:");
        System.out.println("*************************************************");
        
        for(int i = 0; i < players.size(); i++) { // test each player
            BlackJackPlayer p = players.get(i);
            
            if(!p.isActive()) {
                System.out.println(p.getName() + " was inactive for this hand.");
                continue; // skip players that didn't even get a hand
            }

            if(p.hasBlackjack()) {
                if(dealer.hasBlackjack()) { // if both player and dealer have blackjack, push
                    System.out.println(p.getName() + " and the dealer push on blackjack.");
                    p.setMoney(p.getMoney()+p.getBet());
                }
                else {
                    System.out.println(p.getName() + " has Blackjack and wins!"); // gets 1.5x the bet back
                    int winnings = p.getBet()+(p.getBet()*3)/2; // first is the actual bet itself, second is the winnings
                    p.setMoney(p.getMoney()+winnings);
                }
                continue;
            }
            else if(p.getInitialHand().isFiveCardCharlie()) { // blackjack triumphs over all, even the charlies
                System.out.println(p.getName() + " wins by a Five-Card Charlie!"); // gets 1.5x the bet back
                int winnings = p.getBet()+(p.getBet()*3)/2; // first is the actual bet itself, second is the winnings
                p.setMoney(p.getMoney()+winnings);                
            }            
            else {
                if(p.getInitialHand().isSurrendered()) {
                    System.out.println(p.getName() + " has surrendered and will receive half the bet back.");
                    p.setMoney(p.getMoney()+(p.getBet()/2)); // surrender: money+= bet times .5
                }
                else if(p.getInitialHand().isBusted()) {
                    System.out.println(p.getName() + " has busted!");
                    // don't add anything back in
                }
                else if(p.getInitialHand().isDoubledDown()) {
                    if(p.getInitialHand().getHandTotal() > dealer.getCurrentHand().getHandTotal()) {
                        System.out.println(p.getName() + "\'s doubling down was a winning hand!");
                        p.setMoney(p.getMoney()+(p.getBet()*2));
                    }
                    else if(p.getInitialHand().getHandTotal() < dealer.getCurrentHand().getHandTotal()) {
                        if(!dealer.getCurrentHand().isBusted()) { // deal didn't bust, so player loses
                            System.out.println(p.getName() + "\'s doubling down was unsuccessful.");
                        }
                        else { // dealer busted, so doesn't matter what player has - he won
                            System.out.println(p.getName() + "\'s doubling down was a winning hand! b");
                            p.setMoney(p.getMoney()+(p.getBet()*2)); // test
                        }
                    }
                    else { // tied
                        System.out.println(p.getName() + " and the dealer push on the doubled down hand.");
                        p.setMoney(p.getMoney()+p.getBet());
                    }                    
                }
                else { // player stood
                    if(p.getInitialHand().getHandTotal() > dealer.getCurrentHand().getHandTotal()) {
                        System.out.println(p.getName() + " wins!");
                        p.setMoney(p.getMoney()+(p.getBet()*2));
                    }
                    else if(p.getInitialHand().getHandTotal() < dealer.getCurrentHand().getHandTotal()) {
                        if(!dealer.getCurrentHand().isBusted()) { // see a little bit above
                            System.out.println(p.getName() + " loses!");
                        }
                        else {
                            System.out.println(p.getName() + " wins! b"); // player wins, as dealer busted
                            p.setMoney(p.getMoney()+(p.getBet()*2));
                        }
                    }
                    else {
                        System.out.println(p.getName() + " and the dealer push on the hand.");
                        p.setMoney(p.getMoney()+p.getBet());
                    }
                }
                
            }
            
            if(p.hasSplit()) { // check the exact same things for the splitting hand
                if(p.getSplitHand().isBusted()) {
                    System.out.println(p.getName() + " split hand has busted!");
                    // don't add anything back in
                }
                else if(p.getSplitHand().isFiveCardCharlie()) {
                    System.out.println(p.getName() + "\'s split hand wins by a Five-Card Charlie!"); // gets 1.5x the bet back
                    int winnings = p.getSplitBet()+(p.getSplitBet()*3)/2; // first is the actual bet itself, second is the winnings
                    p.setMoney(p.getMoney()+winnings);                
                }
                else if(p.getSplitHand().isDoubledDown()) {
                    if(p.getSplitHand().getHandTotal() > dealer.getCurrentHand().getHandTotal()) {
                        System.out.println(p.getName() + "\'s doubling down on the split hand wins!");
                        p.setMoney(p.getMoney()+(p.getSplitBet()*2));
                    }
                    else if(p.getSplitHand().getHandTotal() < dealer.getCurrentHand().getHandTotal()) {
                        if(!dealer.getCurrentHand().isBusted()) { // deal didn't bust, so player loses
                            System.out.println(p.getName() + "\'s doubling down on the split hand was unsuccessful.");
                        }
                        else {
                            System.out.println(p.getName() + "\'s doubling down on the split hand was a win! b");
                            p.setMoney(p.getMoney()+(p.getSplitBet()*2)); // test
                        }                        
                    }
                    else {
                        System.out.println(p.getName() + " and the dealer push on the doubled down split hand.");
                        p.setMoney(p.getMoney()+p.getSplitBet());
                    }
                    
                }
                else { // player stood (no special actions for this hand)
                    if(p.getSplitHand().getHandTotal() > dealer.getCurrentHand().getHandTotal()) {
                        System.out.println(p.getName() + "\'s split hand wins!");
                        p.setMoney(p.getMoney()+(p.getSplitBet()*2));
                    }
                    else if(p.getSplitHand().getHandTotal() < dealer.getCurrentHand().getHandTotal()) {
                        if(!dealer.getCurrentHand().isBusted()) { // see a little bit above
                            System.out.println(p.getName() + "\'s split hand loses!");
                        }
                        else {
                            System.out.println(p.getName() + "\'s split hand wins! b"); // dealer busted, player wins!
                            p.setMoney(p.getMoney()+(p.getSplitBet()*2));
                        }
                    }
                    else {
                        System.out.println(p.getName() + "\'s split hand and the dealer's hand push.");
                        p.setMoney(p.getMoney()+p.getSplitBet());
                    }
                }
            }
        }     
        
        // scoring - if player hasn't busted (test both hands, if applicable), or surrendered - then compare with the dealer
        // in the above cases, since those happen earlier, then don't even compare, set player to inactive and remove his bet
        // dealer doesn't have any money, btw - just do the "AI" and compare the scores for each applicable player        
    }
    
    
    /** 
     * displayGameMenu() - draws out the options a player has during each hand - not all are available, depending on the situation
     */
    private void displayGameMenu() {
        // draw the player's name and such
        System.out.println("Your money remaining: $" + currentPlayer.getMoney());
        System.out.println("Your bet: $" + currentPlayer.getBet());
        if(currentPlayer.hasSplit()) {
            System.out.println("Your split bet: $" + currentPlayer.getSplitBet());
        }
        System.out.println("What would you like to do?");
        System.out.print("H = hit, S = stand,");

        // only print the commands that are applicable
        if(currentPlayer.canSplit()) {
            System.out.print(" P = split,");
        }
        if(currentPlayer.getCurrentHand().size() <= 2) {
            if(currentPlayer.getMoney() > 0) {
                System.out.print(" D = double down,");
            }
            if(currentPlayer.getBet() > 1 && !currentPlayer.hasSplit()) {
                System.out.print(" X = surrender,");
            }
        }
        System.out.println(" : ");
    }
    
    /**
     * addCardToHand - appends a card from the top of the deck to the end of the specified hand
     * @param myHand - a reference to a Hand object to which a Card will be appended
     */
    private void addCardToHand(Hand myHand) { // this should be used - this is duplicated in Hand.java?
        Card c = (Card)gameDeck.removeTopCard();
        myHand.addCard(c);
    }
    
    /**
     * promptToBuyMoreChips - if a user doesn't have any more money, allow them to continue by "purchasing" more chips
     * @param p - the player to ask for purchasing
     * @return - whether the player wanted more money, if so, return true (this method handles how much is given to him)
     */
    private boolean promptToBuyMoreChips(BlackJackPlayer p) {
        System.out.println("Would you like to buy more chips? (y/n): ");
        Scanner s = new Scanner(System.in);
        
        String answer = "";
        while(true) { // get his initial answer            
            answer = s.nextLine();
            char entry = ((answer.trim()).toLowerCase()).charAt(0);
            if(entry == 'y') {
                break;
            }
            else if(entry == 'n') { // player doesn't want anything else - remove him from the list
                return false;
            }          
        }
        
        System.out.println("How many chips would you like? $"); // player wanted more chips, so ask for how many, and return true
        while(true) { // prompt for more chips            
            try {
                int chips = s.nextInt();
                if(chips < 1) {
                    System.out.println("I'm sorry. You must buy at least $1 worth of chips. How many would you like? $");
                    continue;
                }
                p.setMoney(chips);
                break;
            }
            catch(InputMismatchException e) { // catches invalid answers
                System.out.println("I'm sorry. That input is invalid. How many chips would you like? $");
                continue;
            }            
        }
        return true;
    }    
}
