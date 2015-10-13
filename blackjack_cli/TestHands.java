package blackjack;

/**
 *
 * @author Michael Hergenrader
 */
public class TestHands {

    public static void main() {
        
        Hand h = new BlackJackHand();
        
        System.out.println("The hand total score is " + h.getHandTotal());
        System.out.println("The size of the hand is " + h.size());
        
        Card c = new Card(Card.HEARTS,4,"K");
        Card d = new Card(Card.SPADES,3,"9");
        
        Card e = null;
        
        h.addCard(c);
        h.addCard(d);
        
        System.out.println(h.toString());
        
        System.out.println("Adding a null card, e, will not work, as seen here:");
        h.addCard(e);        
        System.out.println(h.toString());
        
        System.out.println("The hand total score is " + h.getHandTotal());
        System.out.println("The size of the hand is " + h.size());
        
        System.out.println("\nThe last card is a " + h.getLast().toString());
        System.out.println("Now it is removed.");
        
        Card f = h.removeLast();
        System.out.println("The card removed was a " + f.toString());
        
        System.out.println("Now more cards are added.");
        for(int i = 0; i < 3; i++) {
            h.addCard(d);
        }
        System.out.println("The hand total score is " + h.getHandTotal());
        System.out.println("The size of the hand is " + h.size());
        if(h instanceof BlackJackHand) {
            System.out.println("This hand is a five-card charlie: true or false?" + ((BlackJackHand)h).isFiveCardCharlie());
        }
        
        System.out.println("One more card is added: d is added again.");
        h.addCard(d);
        System.out.println(h.toString());
        if(h instanceof BlackJackHand) {
            System.out.println("This hand is a five-card charlie: true or false?" + ((BlackJackHand)h).isFiveCardCharlie());
        }
        
        System.out.println("One more card is added: a king, and a d is removed.");
        h.removeLast();
        h.addCard(new Card(Card.DIAMONDS,10,"K"));
        System.out.println(h.toString());
        if(h instanceof BlackJackHand) {
            System.out.println("This hand is a five-card charlie: true or false?" + ((BlackJackHand)h).isFiveCardCharlie());
        }
        
        System.out.println("The entire hand will now be discarded.");
        h.discardHand();
        System.out.println("The hand total score is " + h.getHandTotal());
        System.out.println("The size of the hand is " + h.size());        
    }
    
    
}
