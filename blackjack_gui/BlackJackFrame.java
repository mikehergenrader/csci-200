package blackjackgui;

// BlackJackFrame.java
// Michael Hergenrader
// CSCI 200 Fall 2009
// USCID: 5048471246
// 10/11/2009

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 *
 * @author Michael Hergenrader
 */
public class BlackJackFrame extends JFrame { // main frame: holds all panels and main game logic
    
    // Panel instance variables
    private GameTablePanel tablePanel;
    private PlayerIngameDataPanel playersPanel;
    private GameCommandsPanel commandsPanel;
    private MessagesPanel messagesPanel;   
    
    // Game instance variables
    private ArrayList<BlackJackPlayer> players;    
    private BlackJackPlayer currentPlayer;
    private BlackJackPlayer dealer;    
    private Deck gameDeck;
    
    private int numberOfPlayers;
    private int numberOfDecks;
    private int bettingLimit;
    private int currentHandNumber;
    
    // Menu instance variables
    private JMenuBar mainMenuBar;
    
    private JMenu gameMenu;
    private JMenu playerActionsMenu;
    //private JMenu helpMenu;
    
    private JMenuItem exit;
    
    private JMenuItem hit;
    private JMenuItem stand;
    private JMenuItem doubleDown;
    private JMenuItem split;
    private JMenuItem surrender;
    private JMenuItem view;
    
    //private JMenuItem help;
    //private JMenuItem about; 
    
    /**
     * constructor - creates the frame, initializes all the panels and gives the necessary information; then, initializes the game fields and calls mainGame() to start the game
     */
    public BlackJackFrame() {
        super("Blackjack by Michael Hergenrader");
        setSize(1200,700);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("C:/Users/Michael/Pictures/images/blackjack_icon.gif").getImage());
        
        currentHandNumber = 1;
        
        BlackJackSplashScreenDialog splash = new BlackJackSplashScreenDialog();
        
        BlackJackIntroDialog s = new BlackJackIntroDialog(this,true);
        setPregameOptions(s.getNumberOfPlayers(),s.getNumberOfDecks(),s.getBettingLimit());
        
        players = new ArrayList<BlackJackPlayer>();  // create players list and initialize them by the dialog
        PlayerRegistrationDialog register = new PlayerRegistrationDialog(this,numberOfPlayers,players);
                
        dealer = new BlackJackPlayer("dealer","",2000);   
        gameDeck = new Deck(numberOfDecks);
        //System.out.println(numberOfDecks);
        
        initializePanels();
        initializeLayout();
        
        setupMainMenuBar();
        setJMenuBar(mainMenuBar); // have to do this after everything else? strange (maybe just needs panels)
        
        setVisible(true);
        mainGame();
    }
    
    /**
     * initializePanels - helper method for the constructor to create all four panels and pass the frame as the owning reference
     */
    private void initializePanels() {
        tablePanel = new GameTablePanel(this);
        playersPanel = new PlayerIngameDataPanel(this);
        commandsPanel = new GameCommandsPanel(this);
        messagesPanel = new MessagesPanel(this);
    }
    
    /**
     * initializeLayout - sets up the constraints for a GridBagLayout in order to arrange all four of the frame's JPanel objects correctly
     */
    private void initializeLayout() {
        /*setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints(); // dictate how GridBagLayout will work
        
        c.gridwidth = 2;
        c.gridheight = 2;
        
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;        
        add(tablePanel,c); // the drawing panel
        
        c.gridx = 980;
        c.gridy = 0;
        add(playersPanel,c); // the player stats panel to the upper right
        
        c.gridx = 0;
        c.gridy = 570;        
        add(commandsPanel,c); // the commands panel in the bottom left
        
        c.gridx = 980;
        c.gridy = 570;
        add(messagesPanel,c); // the messages panel in the bottom right*/
        
        
        SpringLayout sl = new SpringLayout();
        setLayout(sl);
        
        add(tablePanel);
        add(playersPanel);
        add(commandsPanel);
        add(messagesPanel);
        
        //Adjust constraints for the label so it's at (5,5).
        sl.putConstraint(SpringLayout.WEST,tablePanel,0,SpringLayout.WEST,getContentPane());
        sl.putConstraint(SpringLayout.NORTH,tablePanel,0,SpringLayout.NORTH,getContentPane());
        
        sl.putConstraint(SpringLayout.WEST,playersPanel,0,SpringLayout.EAST,tablePanel);
        sl.putConstraint(SpringLayout.NORTH,playersPanel,0,SpringLayout.NORTH,getContentPane());

        sl.putConstraint(SpringLayout.WEST,commandsPanel,0,SpringLayout.WEST,getContentPane());
        sl.putConstraint(SpringLayout.NORTH,commandsPanel,0,SpringLayout.SOUTH,tablePanel);
        
        sl.putConstraint(SpringLayout.WEST,messagesPanel,0,SpringLayout.EAST,commandsPanel);
        sl.putConstraint(SpringLayout.NORTH,messagesPanel,0,SpringLayout.SOUTH,playersPanel);


        
    }
    
    /**
     * setPregameOptions - a helper method to grab the data from the BlackJackIntroDialog objects - sets the game options specified
     * @param numberOfPlayers - how many players are in this game
     * @param numberOfDecks - the number of decks they want to use
     * @param bettingLimit - the betting limit they would like to set
     */
    private void setPregameOptions(int numberOfPlayers, int numberOfDecks, int bettingLimit) {
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfDecks = numberOfDecks;
        this.bettingLimit = bettingLimit;        
    }
    
    /**
     * setupMainMenuBar - creates the top menu bar, adding all the items and giving all of them listeners
     */
    private void setupMainMenuBar() {
        mainMenuBar = new JMenuBar();
        
        gameMenu = new JMenu("Game");
        playerActionsMenu = new JMenu("Actions");
        //helpMenu = new JMenu("Help");
        
        exit = new JMenuItem("Exit");
        
        hit = new JMenuItem("Hit");
        stand = new JMenuItem("Stand");
        doubleDown = new JMenuItem("Double Down");
        split = new JMenuItem("Split Cards");
        surrender = new JMenuItem("Surrender");
        view = new JMenuItem("View Other Hand");
        
        //help = new JMenuItem("Help");
        //about = new JMenuItem("About");
        
        gameMenu.setMnemonic(KeyEvent.VK_G);
        playerActionsMenu.setMnemonic(KeyEvent.VK_A);
        //helpMenu.setMnemonic(KeyEvent.VK_H);
        
        // add keyboard shortcuts
        hit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,ActionEvent.CTRL_MASK));
        stand.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
        doubleDown.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,ActionEvent.CTRL_MASK));
        split.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,ActionEvent.CTRL_MASK));
        surrender.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,ActionEvent.CTRL_MASK));
        view.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,ActionEvent.CTRL_MASK));
        
        MenuItemListener ml = new MenuItemListener();
        exit.addActionListener(ml);
        hit.addActionListener(ml);
        stand.addActionListener(ml);
        doubleDown.addActionListener(ml);
        split.addActionListener(ml);
        surrender.addActionListener(ml);
        view.addActionListener(ml);
        //help.addActionListener(ml);
        //about.addActionListener(ml);    
        
        gameMenu.add(exit);
        
        playerActionsMenu.add(hit);
        playerActionsMenu.add(stand);
        playerActionsMenu.add(doubleDown);
        playerActionsMenu.add(split);
        playerActionsMenu.add(surrender);
        playerActionsMenu.add(view);
        
        //helpMenu.add(help);
        //helpMenu.add(about);
        
        mainMenuBar.add(gameMenu);
        mainMenuBar.add(playerActionsMenu);
        //mainMenuBar.add(helpMenu);
    }
    
    /**
     * getPlayers - returns a reference to all current players in this game
     * @return - a reference to the ArrayList of player objects
     */
    public ArrayList<BlackJackPlayer> getPlayers() { // might remove this method
        return players;
    }
    
    /**
     * addPlayer - adds a new player to this list - provides abstraction, so other panels don't have to worry about the actual ArrayList itself
     * @param name - the player's name
     * @param nickname - the player's nickname
     * @param moneyAmount - the player's starting money amount
     */
    public void addPlayer(String name, String nickname, int moneyAmount) {
        players.add(new BlackJackPlayer(name,nickname,moneyAmount));
    }
    
    /**
     * getNumberOfPlayers - getter method for number of players in the game
     * @return - the number of players
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    
    /**
     * getBettingLimit - getter method for betting limit
     * @return - the established betting limit
     */
    public int getBettingLimit() {
        return bettingLimit;
    }
    
    /**
     * getCurrentHandNumber - getter method for the current hand number (how many have been played)
     * @return - the hand number (ID)
     */
    public int getCurrentHandNumber() {
        return currentHandNumber;
    }
    
    /**
     * getCurrentPlayer - returns the current player (getter method)
     * @return - a reference to the current player in the ArrayList
     */
    public BlackJackPlayer getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * getDealer - returns the dealer object
     * @return - a reference to the dealer
     */
    public BlackJackPlayer getDealer() {
        return dealer;
    }
    
    /**
     * mainGame - starts up and runs the entire program: prompts for bets, prompts for player hand actions, and delegates to other methods
     */
    public void mainGame() {

        while(true) {            
            playersPanel.setInformation("\nBetting Limit: $" + bettingLimit + "\nHand Number: " + currentHandNumber);
            
            // Collect bets from everyone for the hand
            collectBets();

            dealer.setupForHand();
            dealer.getCurrentHand().addCard(gameDeck.removeTopCard());
            dealer.getCurrentHand().getLast().setFaceUp(false); // can put this on either card (i.e. after the next statement as well)
            dealer.getCurrentHand().addCard(gameDeck.removeTopCard());

            for(int j = 0; j < players.size(); j++) {

                // if player is not active for this hand, then skip and move on
                if(!players.get(j).isActive() || players.get(j).isDisabled()) {
                    continue;
                }
                
                // if player is active, then give him cards
                currentPlayer = (BlackJackPlayer)players.get(j); // cast probably unnecessary
                currentPlayer.setupForHand();

                // add in the first two cards from the top of the deck
                BlackJackGUICard c = gameDeck.removeTopCard();
                c.setFaceUp(false);
                currentPlayer.getCurrentHand().addCard(c);
                currentPlayer.getCurrentHand().addCard(gameDeck.removeTopCard());

                updateFields();
            }        

            for(int k = 0; k < players.size(); k++) { // manage each player's actions
                if(!players.get(k).isActive() || players.get(k).isDisabled()) {
                    continue;
                }

                currentPlayer = players.get(k);
                currentPlayer.getCurrentHand().get(0).setFaceUp(true);
                
                resetAllButtons();                
                setTurnColors(k);
                
                manageGameActions(); // handle the events for each player - decide when he or she is done
            }

            disableAllButtons(); // don't let player do anything while dealer is at work
            manageDealer();
            manageScoring(); // for the active players, compute what the winnings or losses are for each - print results

            // clean up the players and give their hands back           
            for(int i = 0; i < players.size(); i++) {
                currentPlayer = players.get(i);
                if(currentPlayer.isDisabled() || !currentPlayer.isActive()) { // note to self: check this second condition
                    continue;
                }
                currentPlayer.returnHandsToDeck(gameDeck);
                currentPlayer.setBet(0); //TTTTTTTTTTTTTTT - this is needed to remove the chips from the table
                currentPlayer.setSplitBet(0);                
            }            
            
            dealer.returnHandsToDeck(gameDeck); // return the dealer's cards as well
            gameDeck.shuffleDeck(); // like a lot of casinos today, continously shuffles the cards once they have been returned to prevent counting   

            updateFields();
            currentHandNumber++;
            
            // Ask a user without any more money if he or she would like to continue, and "buy more chips"
            Iterator<BlackJackPlayer> iter2 = players.iterator(); // this works, but also could use a ListIterator
            while(iter2.hasNext()) {
                BlackJackPlayer c = iter2.next();
                if(c.getMoney() == 0) {
                    if(!c.isDisabled() && !promptToBuyMoreChips(c)) { // player doesn't want to buy back in
                        c.setDisabled(true);
                        updateFields();
                    }
                }
            }

            // make sure that there are still players in the game before  the next hand starts
            int numPlayersDisabled = 0;
            for(int i = 0; i < players.size(); i++) { // all players are bankrupt, and no one chooses to buy back in
                if(players.get(i).isDisabled()) {
                    numPlayersDisabled++;
                }
            }
            if(numPlayersDisabled == players.size()) { // if all are disabled, game over
                JOptionPane.showMessageDialog(null,"Thanks for playing!\nGoodbye!");
                System.exit(0);
            }            
        }
    }
    
    /**
     * setTurnColors - helper method to set the colors of the buttons in all the game frames, to denote a specific player's turn
     * @param playerIndex - the current player - will dictate which color is drawn
     */
    private void setTurnColors(int playerIndex) {
        commandsPanel.setButtonColors(playerIndex);
        messagesPanel.setTextColor(playerIndex);
    }
    
    /**
     * collectBets - cycles through each player, prompting for a bet, or seeing if they want to skip the hand - invokes BetInputDialog
     */
    private void collectBets() {
        int i = 0;
        
        for(int j = 0; j < players.size(); j++) {
            currentPlayer = players.get(j);
            if(currentPlayer.isDisabled()) {
                i++;
                continue;
            }
            else {
                currentPlayer.setActive(true); // each player is automatically assumed to be playing a hand, unless specifies otherwise
                        
                BetInputDialog betInput = new BetInputDialog(this,true,i);
                i++;
                updateFields();
            }
        }
    }
    
    /**
     * manageGameActions - handles all of the actions of each player's turn, and dictates what buttons are allowed and when each turn is over
     */
    private void manageGameActions() {        
        messagesPanel.setMessage(currentPlayer.getName() + "\'s turn");

        // check if anyone has blackjack
        if(dealer.hasBlackjack()) {
            if(currentPlayer.hasBlackjack()) { // if both player and dealer have blackjack, push
                JOptionPane.showMessageDialog(null,currentPlayer.getName() + " and the dealer push on blackjack.");
                currentPlayer.setMoney(currentPlayer.getMoney()+currentPlayer.getBet()); // check if this is right
                return;
            }
            else { // player loses to a dealer's blackjack
                JOptionPane.showMessageDialog(null,currentPlayer.getName() + " loses to the dealer\'s blackjack...");
                return;
            }
        }

        if(currentPlayer.hasBlackjack()) { // if just player has blackjack, then don't even do anything - he wins
            JOptionPane.showMessageDialog(null,currentPlayer.getName() + " has Blackjack!!!");
            return;
        }

        // disable buttons at start of turn if conditions are met
        if(currentPlayer.getBet() < 2) {
           disableSurrendering();
        }

        if(!currentPlayer.canSplit()) {
            disableSplitting();
        }

        if(currentPlayer.getMoney() == 0) {
            disableDoublingDown();
            disableSplitting();
            //System.out.println("a");
        }

        // main game action loop for players
        do {                
            if(currentPlayer.hasSplit()) { // player has split, and first hand is done (make the current hand the split one)
                BlackJackHand h = currentPlayer.getCurrentHand();

                if(h == currentPlayer.getInitialHand()) {
                    if(h.isComplete()) { // one hand done, check the other
                        if(currentPlayer.getInitialHand().isFiveCardCharlie()) {
                            JOptionPane.showMessageDialog(null,currentPlayer.getName() + "\'s first hand is a Five-Card Charlie!");
                        }
                        
                        if(currentPlayer.getSplitHand().isComplete()) {
                            if(currentPlayer.getSplitHand().isFiveCardCharlie()) {
                                JOptionPane.showMessageDialog(null,currentPlayer.getName() + "\'s split hand is a Five-Card Charlie!");
                            }
                            
                            return; // both are done, so return
                        }
                        else {
                            disableViewToggle();
                            currentPlayer.setCurrentHandToSplit();
                            
                            if(currentPlayer.getCurrentHand().size() == 2 && currentPlayer.getMoney() > 0) {
                                enableDoublingDown();
                            }
                            else {
                                disableDoublingDown();
                                //System.out.println("b"); // problem is here
                            }
                        }                         
                    }
                }
                else if(h == currentPlayer.getSplitHand()) {
                    if(h.isComplete()) {
                        if(h.isFiveCardCharlie()) {
                            JOptionPane.showMessageDialog(null,currentPlayer.getName() + "\'s split hand is a Five-Card Charlie!");
                        }
                        
                        if(currentPlayer.getInitialHand().isComplete()) {
                            if(currentPlayer.getInitialHand().isFiveCardCharlie()) {
                                JOptionPane.showMessageDialog(null,currentPlayer.getName() + "\'s first hand is a Five-Card Charlie!");
                            }
                            
                            return; // both are done, so return
                        }
                        else {
                            disableViewToggle();
                            currentPlayer.setCurrentHandToOriginal();
                            if(currentPlayer.getCurrentHand().size() == 2 && currentPlayer.getMoney() > 0) {
                                enableDoublingDown();
                            }
                            else {
                                disableDoublingDown();
                                //System.out.println("c");
                            }
                        }
                    }                    
                }
            }

            else { // hasn't split, but check that he is done with this hand
                if(currentPlayer.getInitialHand().isComplete()) {
                    if(currentPlayer.getInitialHand().isFiveCardCharlie()) {
                        JOptionPane.showMessageDialog(null,currentPlayer.getName() + "\'s hand is a Five-Card Charlie!");
                    }
                    return;
                }
            }
        } while(true);
    }
    
    /**
     * addCardToHand - adds a card to the specified hand
     * @param myHand - a reference to the hand that the card is added to
     */
    public void addCardToHand(Hand myHand) { // this should be used - this is duplicated in Hand.java?
        BlackJackGUICard c = (BlackJackGUICard)gameDeck.removeTopCard();
        myHand.addCard(c);
    }
    
    /**
     * setMessage - calls the setMessage method of the messages panel - used just for less typing here
     * @param s - the string to set in the messages panel
     */
    private void setMessage(String s) {
        messagesPanel.setMessage(s);
    }
    
    /**
     * updateFields - a helper method like the one above that calls the updateFields method of the players panel
     */
    private void updateFields() {
        playersPanel.updateFields(players);
    }
    
    /**
     * playerHits - the actions to take when a player chooses to hit - invoked by either the button or the menu option
     */
    public void playerHits() {
        setMessage(currentPlayer.getName() + " hits."); // pass the message to the messages panel
        addCardToHand(currentPlayer.getCurrentHand());
        updateFields();

        if(currentPlayer.getCurrentHand().getHandTotal() > 21) { // check if player busted
            JOptionPane.showMessageDialog(null,"You busted!");
            currentPlayer.getCurrentHand().setBusted(true);                           
        }
    }
    
    /**
     * playerStands - dictates the action of when a player chooses to stand
     */
    public void playerStands() {
        setMessage(currentPlayer.getName() + " chooses to stand.");
        currentPlayer.getCurrentHand().setStood(true);
        updateFields();
    }

    /**
     * playerDoublesDown - handles the actions that must occur when a player decides to double down - prompts for his addition
     */
    public void playerDoublesDown() {
        String input = (String)JOptionPane.showInputDialog(null,"How much would you like to double down with?\n(up to " + Math.min((currentPlayer.getCurrentHand()==currentPlayer.getSplitHand())?currentPlayer.getSplitBet():currentPlayer.getBet(),currentPlayer.getMoney()) + " dollars)");//,JOptionPane.QUESTION_MESSAGE);
        
        if(input == null) { // user hit cancel
            return;
        }
        
        int betAddition = 0;
        try {
            betAddition = Integer.parseInt(input);
            
            // outside the correct range
            if(betAddition < 1 || betAddition > Math.min((currentPlayer.getCurrentHand()==currentPlayer.getSplitHand())?currentPlayer.getSplitBet():currentPlayer.getBet(),currentPlayer.getMoney())) {
                JOptionPane.showMessageDialog(null,"Your amount is not in the range of $1 - $" + Math.min((currentPlayer.getCurrentHand()==currentPlayer.getSplitHand())?currentPlayer.getSplitBet():currentPlayer.getBet(),currentPlayer.getMoney()) + "\nPlease enter an amount in this range.","Invalid amount",JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        catch(NumberFormatException nme) {
            JOptionPane.showMessageDialog(null,"Your entry is not a valid amount. Please enter a valid amount to double down with.","Invalid amount",JOptionPane.ERROR_MESSAGE);
            return;
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

        addCardToHand(currentPlayer.getCurrentHand()); // give player one extra card
        
        setMessage(currentPlayer.getName() + " doubles down.");
        updateFields();

        if(currentPlayer.getCurrentHand().getHandTotal() > 21) { // check if double down caused player to bust            
            JOptionPane.showMessageDialog(null,"You busted!");
            currentPlayer.getCurrentHand().setBusted(true);            
        }
        else {
            currentPlayer.getCurrentHand().setDoubledDown(true); // CANNOT BE BOTH - THE FACT THAT THAT DOUBLED DOWN COMES FIRST STILL CAUSES THE DEALER TO GO - NEED TO SET LATER (EITHER WAY, THE PLAYER'S HAND IS OVER)
        }
    }
    
    /**
     * playerSplits - splits the cards and gives the player two new ones, also enables the view button so user can freely move in between hands
     */
    public void playerSplits() {
        if(currentPlayer.canSplit()) { // check if player can split; if so, switch up cards
            
            // checks if there are two aces; if so, knock up their values so when they are in separate hands, they work normally
            BlackJackGUICard firstCard = currentPlayer.getCurrentHand().get(0);
            BlackJackGUICard secondCard = currentPlayer.getCurrentHand().get(1);
            
            if(firstCard.getCardRepresentation().equals("A") && secondCard.getCardRepresentation().equals("A")) {
                if(firstCard.getValue() == 1) {
                    firstCard.knockUpAceValue();
                }
                if(secondCard.getValue() == 1) {
                    secondCard.knockUpAceValue();
                }
            }
            
            currentPlayer.setHandsSplit(true);
            currentPlayer.setSplitBet(currentPlayer.getBet()); // make a separate bet
            currentPlayer.setMoney(currentPlayer.getMoney()-currentPlayer.getBet());

            currentPlayer.getSplitHand().addCard(currentPlayer.getInitialHand().removeLast());
            addCardToHand(currentPlayer.getInitialHand());
            addCardToHand(currentPlayer.getSplitHand()); // move one card to the split hand
            
            updateFields(); // update the player's action
            setMessage(currentPlayer.getName() + " splits the cards.");
        }
        
        disableSplitting();
        disableSurrendering();
        if(currentPlayer.getMoney() == 0) {
            disableDoublingDown();
            //System.out.println("f");
        }
        
        enableViewToggle(); // let the user flip back and forth between cards (since can't show both hands at once)
    }
    
    /**
     * playerSurrenders - dictates that the player has decided to surrender; his hand is done
     */
    public void playerSurrenders() {
        // lose half the bet and the hand
        currentPlayer.getCurrentHand().setSurrendered(true);
        setMessage(currentPlayer.getName() + " surrenders.");
    }
    
    /**
     * toggleHandView - switches which hand is viewed/current, the original or the split, and changes the button text accordingly
     */
    public void toggleHandView() {
        if(currentPlayer.getCurrentHand() == currentPlayer.getInitialHand()) {
            view.setText("Manage first hand");
            commandsPanel.setViewButtonText("Manage first hand");
            currentPlayer.setCurrentHandToSplit(); // flip the text and the current view to the split hand
        }
        else {
            view.setText("Manage second hand");
            commandsPanel.setViewButtonText("Manage second hand");
            currentPlayer.setCurrentHandToOriginal(); // same, but flip to the original hand
        }
    }

    /**
     * manageDealer - control the dealer's actions - must hit until has at least 17, also checks if he has busted
     */
    private void manageDealer() {
        // determines whether a dealer should act - if players have already busted, or surrendered, or all have charlies, or are inactive, the dealer doesn't have to do anything
        // this could just be one variable: who to skip - numPlayersToSkip
        int numPlayersBusted = 0, numPlayersSurrendered = 0, numPlayersInactive = 0, numPlayersWithCharlies = 0, numPlayersWithBlackjack = 0;
        for(int m = 0; m < players.size(); m++) {
            if(players.get(m).getInitialHand().isBusted()) {
                if(players.get(m).hasSplit()) {
                    if(players.get(m).getSplitHand().isBusted() || players.get(m).getSplitHand().isFiveCardCharlie()) { // this will work now - dealer won't go if BOTH hands are busted
                        numPlayersBusted++; // now tests for mixes as well
                    }
                }
                else {
                    numPlayersBusted++;
                }
            }
            if(players.get(m).getInitialHand().isSurrendered()) { // count how many have surrendered
                numPlayersSurrendered++;
            }
            if(!players.get(m).isActive()) { // count how many are inactive
                numPlayersInactive++;
            }
            if(players.get(m).hasBlackjack()) {
                numPlayersWithBlackjack++;
            }
            if(players.get(m).getInitialHand().isFiveCardCharlie()) {
                if(players.get(m).hasSplit()) {
                    if(players.get(m).getSplitHand().isFiveCardCharlie() || players.get(m).getSplitHand().isBusted()) {
                        numPlayersWithCharlies++;  // skip this player if he has both hands as charlies
                    }
                }
                else {
                    numPlayersWithCharlies++;
                }
            }
        }

        int totalToSkip = numPlayersBusted+numPlayersSurrendered+numPlayersInactive+numPlayersWithCharlies+numPlayersWithBlackjack;

        // if everyone has busted or surrendered or isn't playing, dealer doesn't need to do anything (this sum also provides the case for a mix of all players not being able to compare to the dealer)
        if(totalToSkip >= players.size()) {
            return;
        }
        
        dealer.getCurrentHand().get(0).setFaceUp(true); // turn the first card over
        redrawDealerCards();
        
        while(dealer.getCurrentHand().getHandTotal() < 17) { // must stand on 17 or over
            addCardToHand(dealer.getCurrentHand());
            redrawDealerCards();
            
            if(dealer.getCurrentHand().getHandTotal() > 21) { // dealer has busted
                JOptionPane.showMessageDialog(null,"Dealer busts!");
                
                dealer.getCurrentHand().setBusted(true);
                return;
            }
        }
        dealer.getCurrentHand().setStood(true); // needed? probably not, but safe
        dealer.getCurrentHand().setBusted(false);
        
        if(dealer.hasBlackjack()) { // don't do anything if the dealer has a blackjack - prevent him from printing out more
            return;
        }

        JOptionPane.showMessageDialog(null,"Dealer stands at " + dealer.getCurrentHand().getHandTotal());
    }
    
    /**
     * redrawDealerCards - helper method that refreshes the table panel, so that the players can see the step by step actions of the dealer
     */
    private void redrawDealerCards() {
        tablePanel.drawDealerCards(tablePanel.getGraphics());
        try {
            Thread.sleep(800); // creates the delay between actions
        }
        catch(InterruptedException e) {
            System.out.println("Interruption occurred in drawing dealer cards.");
        }
    }
    
    /**
     * manageScoring - determines how the scoring is done and how winnings and losses are determined for all actions
     * also prints out what happened to every active player in the hand
     */
    private void manageScoring() {        
    
        // scoring - if player hasn't busted (test both hands, if applicable), or surrendered - then compare with the dealer
        // in the above cases, since those happen earlier, then don't even compare, set player to inactive and remove his bet
        // dealer doesn't have any money, btw - just do the "AI" and compare the scores for each applicable player     
        HandResultsDialog results = new HandResultsDialog(this,true);
        
        //results.clearText(); // unnecessary
        results.addText("---------------- RESULTS ---------------\n");   
        
        results.addText("Dealer\'s Score: " + dealer.getCurrentHand().getHandTotal() + "\n");
        
        for(int j = 0; j < players.size(); j++) { // draw general scores
            BlackJackPlayer p = players.get(j);
            if(p.isDisabled()) {
                continue;
            }
            
            results.addText(p.getName() + "\'s hand score: " + p.getInitialHand().getHandTotal());
            
            if(p.hasSplit()) {
                results.addText(", " + p.getName() + "\'s split hand score: " + p.getSplitHand().getHandTotal());
            }
            results.addText("\n");
        }
        
        results.addText("\n");
        
        for(int i = 0; i < players.size(); i++) { // test each player
            BlackJackPlayer p = players.get(i);
            
            if(p.isDisabled()) {
                continue; // don't display anything for disabled players
            }
            
            if(!p.isActive()) {
                results.addText(p.getName() + " was inactive for this hand.\n");
                playersPanel.updatePlayerStatus(i,null);
                continue; // skip players that didn't even get a hand
            }

            if(p.hasBlackjack()) {
                if(dealer.hasBlackjack()) { // if both player and dealer have blackjack, push
                    results.addText(p.getName() + " and the dealer push on blackjack.\n");
                    playersPanel.updatePlayerStatus(i,"was a Blackjack and pushed with the dealer's.");
                    p.setMoney(p.getMoney()+p.getBet());
                }
                else {
                    results.addText(p.getName() + " has a Blackjack and wins!\n");
                    playersPanel.updatePlayerStatus(i,"was a Blackjack!");
                    int winnings = p.getBet()+(p.getBet()*3)/2; // first is the actual bet itself, second is the winnings
                    p.setMoney(p.getMoney()+winnings);
                }
                continue;
            }
            else if(p.getInitialHand().isFiveCardCharlie()) { // blackjack triumphs over all, even the charlies
                results.addText(p.getName() + " wins by a Five-Card Charlie!\n");
                playersPanel.updatePlayerStatus(i,"won by a Five-Card Charlie!");
                int winnings = p.getBet()+(p.getBet()*3)/2; // first is the actual bet itself, second is the winnings
                p.setMoney(p.getMoney()+winnings);                
            }            
            else {
                if(p.getInitialHand().isSurrendered()) {
                    results.addText(p.getName() + " surrendered, losing half the initial bet.\n");
                    playersPanel.updatePlayerStatus(i,"was surrendered, with half the bet returned.");
                    p.setMoney(p.getMoney()+(p.getBet()/2)); // surrender: money+= bet times .5
                }
                else if(p.getInitialHand().isBusted()) {
                    results.addText(p.getName() + "\'s hand busted!\n");
                    playersPanel.updatePlayerStatus(i,"busted!");
                    // don't add anything back in
                }
                else if(p.getInitialHand().isDoubledDown()) {
                    if(p.getInitialHand().getHandTotal() > dealer.getCurrentHand().getHandTotal()) {
                        results.addText(p.getName() + "\'s doubled down hand wins!\n");
                        playersPanel.updatePlayerStatus(i,"won with a doubling down!");
                        p.setMoney(p.getMoney()+(p.getBet()*2));
                    }
                    else if(p.getInitialHand().getHandTotal() < dealer.getCurrentHand().getHandTotal()) {
                        if(!dealer.getCurrentHand().isBusted()) { // deal didn't bust, so player loses
                            results.addText(p.getName() + "\'s doubled down hand loses.\n");
                            playersPanel.updatePlayerStatus(i,"lost with a double down.");
                        }
                        else { // dealer busted, so doesn't matter what player has - he won
                            results.addText(p.getName() + "\'s doubled down hand wins!\n");
                            playersPanel.updatePlayerStatus(i,"won with a doubling down!");
                            p.setMoney(p.getMoney()+(p.getBet()*2)); // test
                        }
                    }
                    else { // tied
                        results.addText(p.getName() + "\'s hand pushed with the dealer.\n");
                        playersPanel.updatePlayerStatus(i,"was doubled down and pushed with the dealer's hand.");
                        p.setMoney(p.getMoney()+p.getBet());
                    }                    
                }
                else { // player stood
                    if(p.getInitialHand().getHandTotal() > dealer.getCurrentHand().getHandTotal()) {
                        results.addText(p.getName() + "\'s hand wins with a score of " + p.getInitialHand().getHandTotal() + ".\n");
                        playersPanel.updatePlayerStatus(i,"won!");
                        p.setMoney(p.getMoney()+(p.getBet()*2));
                    }
                    else if(p.getInitialHand().getHandTotal() < dealer.getCurrentHand().getHandTotal()) {
                        if(!dealer.getCurrentHand().isBusted()) { // see a little bit above
                            results.addText(p.getName() + "\'s hand loses.\n");
                            playersPanel.updatePlayerStatus(i,"lost.");
                        }
                        else {
                            results.addText(p.getName() + "\'s hand wins with a score of " + p.getInitialHand().getHandTotal() + ".\n");
                            playersPanel.updatePlayerStatus(i,"won!");
                            p.setMoney(p.getMoney()+(p.getBet()*2));
                        }
                    }
                    else {
                        results.addText(p.getName() + "\'s hand pushed with the dealer's.\n");
                        playersPanel.updatePlayerStatus(i,"pushed with the dealer's.");
                        p.setMoney(p.getMoney()+p.getBet());
                    }
                }                
            }
            
            // if the player split hands, draw everything for that hand too
            if(p.hasSplit()) { // check the exact same things for the splitting hand
                if(p.getSplitHand().isBusted()) {
                    results.addText(p.getName() + "\'s split hand has busted!\n");
                    playersPanel.updatePlayerSplitStatus(i,"busted!");
                    // don't add anything back in
                }
                else if(p.getSplitHand().isFiveCardCharlie()) {
                    // gets 1.5x the bet back
                    results.addText(p.getName() + "\'s split hand wins by a Five-Card Charlie!\n");
                    playersPanel.updatePlayerSplitStatus(i,"won by a Five-Card Charlie!");
                    int winnings = p.getSplitBet()+(p.getSplitBet()*3)/2; // first is the actual bet itself, second is the winnings
                    p.setMoney(p.getMoney()+winnings);                
                }
                else if(p.getSplitHand().isDoubledDown()) {
                    if(p.getSplitHand().getHandTotal() > dealer.getCurrentHand().getHandTotal()) {
                        results.addText(p.getName() + "\'s split hand wins on a double down.\n");
                        playersPanel.updatePlayerSplitStatus(i,"won with a doubling down!");
                        p.setMoney(p.getMoney()+(p.getSplitBet()*2));
                    }
                    else if(p.getSplitHand().getHandTotal() < dealer.getCurrentHand().getHandTotal()) {
                        if(!dealer.getCurrentHand().isBusted()) { // deal didn't bust, so player loses
                            results.addText(p.getName() + "\'s doubling down on the split hand was unsuccessful.\n");
                            playersPanel.updatePlayerSplitStatus(i,"lost with a doubling down.");
                        }
                        else {
                            results.addText(p.getName() + "\'s split hand wins on a double down.\n");
                            playersPanel.updatePlayerSplitStatus(i,"won with a doubling down!");
                            p.setMoney(p.getMoney()+(p.getSplitBet()*2)); // test
                        }                        
                    }
                    else {
                        results.addText(p.getName() + " and the dealer push on the doubled down split hand.\n");
                        playersPanel.updatePlayerSplitStatus(i,"with a doubling down pushed with the dealer's hand.");
                        p.setMoney(p.getMoney()+p.getSplitBet());
                    }
                    
                }
                else { // player stood (no special actions for this hand)
                    if(p.getSplitHand().getHandTotal() > dealer.getCurrentHand().getHandTotal()) {
                        results.addText(p.getName() + "\'s split hand wins with a score of " + p.getSplitHand().getHandTotal() + ".\n");
                        playersPanel.updatePlayerSplitStatus(i,"won!");
                        p.setMoney(p.getMoney()+(p.getSplitBet()*2));
                    }
                    else if(p.getSplitHand().getHandTotal() < dealer.getCurrentHand().getHandTotal()) {
                        if(!dealer.getCurrentHand().isBusted()) { // see a little bit above
                            results.addText(p.getName() + "\'s split hand loses!\n");
                            playersPanel.updatePlayerSplitStatus(i,"lost.");
                        }
                        else {
                            // dealer busted, player wins!
                            results.addText(p.getName() + "\'s split hand wins with a score of " + p.getSplitHand().getHandTotal() + ".\n");
                            playersPanel.updatePlayerSplitStatus(i,"won!");
                            p.setMoney(p.getMoney()+(p.getSplitBet()*2));
                        }
                    }
                    else {
                        results.addText(p.getName() + "pushed with the dealer's hand.\n");
                        playersPanel.updatePlayerSplitStatus(i,"pushed with the dealer's hand.");
                        p.setMoney(p.getMoney()+p.getSplitBet());
                    }
                }
            }
        }     
        
        // create the dialog box
        results.setSize(400,400);
        results.setResizable(false);
        results.setLocationRelativeTo(null);
        results.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        results.setTitle("Hand Results");
        results.setVisible(true);   
    }
    
    /**
     * promptToBuyMoreChips - if a user doesn't have any more money, allow them to continue by "purchasing" more chips
     * @param p - the player to ask for purchasing
     * @return - whether the player wanted more money, if so, return true (this method handles how much is given to him)
     */
    private boolean promptToBuyMoreChips(BlackJackPlayer p) {
        int answer = JOptionPane.showConfirmDialog(null,p.getName() + " is out of money!\nWould you like to buy more chips?","Out of money!",JOptionPane.YES_NO_OPTION);
        
        if(answer == JOptionPane.NO_OPTION) { // player doesn't want to buy back in
            return false;
        }
        
        while(true) {
            String amount = (String)JOptionPane.showInputDialog(null,"How many chips would you like to purchase?");
            if(amount == null) {
                return false;
            }
            
            try {
                int chips = Integer.parseInt(amount);
                if(chips < 1) { // entered a zero or negative number
                    JOptionPane.showMessageDialog(null,"You must buy at least $1 worth of chips to stay in.");
                    continue;
                }
                p.setMoney(chips);
                break;
            }
            catch(NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null,"Your input is invalid.\nPlease enter a valid amount of chips.");
                continue;
            }
        }
        
        updateFields(); // if got here, then player has successfully bought back in
        return true;
    }
    
    /**
     * disableAllButtons - turn off all button functionality while the dealer is at work - prevent any game miscues
     */
    public void disableAllButtons() {
        hit.setEnabled(false);
        commandsPanel.setHitButtonEnabled(false);
        
        stand.setEnabled(false);
        commandsPanel.setStandButtonEnabled(false);
        
        doubleDown.setEnabled(false);
        commandsPanel.setDoubleDownButtonEnabled(false);
        
        split.setEnabled(false);
        commandsPanel.setSplitButtonEnabled(false);
        
        surrender.setEnabled(false);
        commandsPanel.setSurrenderButtonEnabled(false);
        
        view.setEnabled(false);
        commandsPanel.setViewButtonEnabled(false);
    }
    
    /**
     * resetAllButtons - turns all the buttons to their appropriate enabled states between turns
     */
    public void resetAllButtons() {
        hit.setEnabled(true);
        commandsPanel.setHitButtonEnabled(true);
        
        stand.setEnabled(true);
        commandsPanel.setStandButtonEnabled(true);
        
        doubleDown.setEnabled(true);
        commandsPanel.setDoubleDownButtonEnabled(true);
        
        surrender.setEnabled(true);
        commandsPanel.setSurrenderButtonEnabled(true);
        
        split.setEnabled(true);
        commandsPanel.setSplitButtonEnabled(true);
        
        view.setEnabled(false);
        commandsPanel.setViewButtonEnabled(false);
    }
    
    /**
     * disableDoublingDown - disable both the menu option and button option, so player cannot double down
     */
    public void disableDoublingDown() {
        doubleDown.setEnabled(false);
        commandsPanel.setDoubleDownButtonEnabled(false);
    }
    
    /**
     * enableDoublingDown - enable both the menu option and button option, so player can double down
     */
    public void enableDoublingDown() { // to turn the buttons back on when player switches hands in a split
        doubleDown.setEnabled(true);
        commandsPanel.setDoubleDownButtonEnabled(true);
    }
    
    /**
     * disableSplitting - disable both the menu option and button option, so player cannot split the cards
     */
    public void disableSplitting() {
        split.setEnabled(false);
        commandsPanel.setSplitButtonEnabled(false);
    }
    
    /**
     * disableSurrendering - disable both the menu option and button option, so player cannot surrender
     */
    public void disableSurrendering() {
        surrender.setEnabled(false);
        commandsPanel.setSurrenderButtonEnabled(false);
    }
    
    /**
     * disableViewToggle - disable both the menu option and button option, so player cannot switch between hands, as he only has the first one
     */
    public void disableViewToggle() {
        view.setEnabled(false);
        commandsPanel.setViewButtonEnabled(false);
    }
    
    /**
     * enableViewToggle - enable both the menu option and button option, so player can freely look at his split hands
     */
    public void enableViewToggle() {
        view.setEnabled(true);
        commandsPanel.setViewButtonEnabled(true);
    }
    
    /**
     * inner class: MenuItemListener implements ActionListener for all the menu buttons - pretty self-explanatory
     */
    class MenuItemListener implements ActionListener {
        
        public void actionPerformed(ActionEvent ae) {
            if(ae.getSource() == exit) {
                System.exit(0);
            }
            else if(ae.getSource() == hit) {
                playerHits();
            }
            else if(ae.getSource() == stand) {
                playerStands();
            }
            else if(ae.getSource() == doubleDown) {
                playerDoublesDown();
            }
            else if(ae.getSource() == split) {
                playerSplits();
            }
            else if(ae.getSource() == surrender) {
                playerSurrenders();
            }
            else if(ae.getSource() == view) {
                toggleHandView();
            }
            /*else if(ae.getSource() == help) {
                
            }
            else if(ae.getSource() == about) {
                
            }*/
        }        
    }   
}
