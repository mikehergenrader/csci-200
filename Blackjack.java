package blackjack;

import java.util.*;

/**
 *
 * @author Michael Hergenrader
 */
public class Blackjack {

    public static void main(String[] args) {
                
        int numberOfPlayers = 0; // how many players to include in this game (1-5)
        int numberOfDecks = 2; // prompt the number of decks to include
        int limit = 0; // the table max limit for betting
        int money = 0; // the starting money value for each player
        
        ArrayList<BlackJackPlayer> players = new ArrayList(); // the players to add into the game
        
        Scanner userInput = new Scanner(System.in); // used to get the input
        
        System.out.println("Welcome to Blackjack!");
        System.out.print("This table allows between one and five players. How many will be joining today? ");
        
        // Prompt for the total number of players
        do {            
            try {
                numberOfPlayers = userInput.nextInt();
                if(numberOfPlayers < 1) { // make sure there is at least one player
                    System.out.println("You must have at least one player. How many will be joining? ");
                    continue;
                }
                else if(numberOfPlayers > 5) { // can only have up to 5 players for this game
                    System.out.println("I\'m sorry. There can only be up to five players. How many will be joining? ");
                    continue;
                }
                break;
            }
            catch(InputMismatchException e) {
                System.out.print("I'm sorry. That amount is invalid. How many will be joining? ");
                userInput.next(); // avoids being stuck in the scanner if no integer is entered
                continue;        
            }
        } while(true);
        
        
        // Prompt for how many decks to use
        System.out.println("How many decks would you like to use? 2,4, or 6? ");        
        do {
            try {
                numberOfDecks = userInput.nextInt();
                if(numberOfDecks == 2 || numberOfDecks == 4 || numberOfDecks == 6) { // only allow 2,4, or 6 as the description states
                    break;
                }
                else {
                    System.out.print("That value is not allowed. Please enter a valid amount: ");
                    continue; // didn't enter a correct amount of decks
                }
            }
            catch(InputMismatchException e) {
                System.out.print("I'm sorry. That amount is invalid. Please enter a valid number: ");
                userInput.next(); // avoids being stuck in the scanner if no integer is entered
                continue;
            }
        } while(true);
        
        
        // Prompts users for the betting limit for the table
        System.out.println("And what betting limit should this table have? ");        
        do {
            try {
                limit = userInput.nextInt();
                if(limit < 1) { // make sure the limit is at least 1
                    System.out.print("Your limit is too low. Please enter a larger limit: ");
                    continue;
                }
                break;
            }
            catch(InputMismatchException e) {
                System.out.print("I'm sorry. That amount is invalid. Please enter a valid limit: ");
                userInput.next(); // avoids being stuck in the scanner if no integer is entered (or if something is too large)
                continue;
            }
        } while(true);
        
        
        // Create the players individually, one by one, getting their name and initial money value
        String userName = "";        
        for(int i = 0; i < numberOfPlayers; i++) {
            System.out.print("Player " + (i+1) + ", please enter your name: ");

            do {
                try {
                    userName = userInput.nextLine();
                }
                catch(NoSuchElementException e) {
                    if(userName.equals("")) {
                        System.out.print("I'm sorry. An empty name is not valid. Please enter your name: ");
                        continue;
                    }
                }
            } while(userName.equals("")); // keep going until the user actually puts a name in
            userName = userName.trim(); // get rid of any extra whitespace a user may include to screw this up
            
            System.out.println();
            System.out.print("And how much money would you like to start with, " + userName + "? ");

            
            // Prompt for money that a user will have
            do {            
                try {
                    money = userInput.nextInt();
                    if(money < 1) { // make sure user has enough money to play
                        System.out.println("Your input is too small. How much would you like? ");
                        continue;
                    }                
                    break;
                }
                catch(InputMismatchException e) {
                    System.out.print("I'm sorry. That amount is invalid. How much would you like? ");
                    userInput.next(); // avoids being stuck in the scanner if no integer is entered
                    continue;        
                }
            } while(true);
            
            players.add(new BlackJackPlayer(userName,money)); // add all of the players to the list for the game
        }
        
        BlackJackGame g = new BlackJackGame(limit,numberOfDecks,players); // construct and start the game!
    }
}
