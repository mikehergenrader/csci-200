package blackjack;

import java.util.*; // this class might not be needed

/**
 *
 * @author Michael Hergenrader
 * Compares first by suit and then by value, need to then distinguish between JACK, QUEEN, and KING (all worth 10)
 */
public class CardComparator implements Comparator<Card> {
    public int compare(Card c1, Card c2) {
        if(c1.getSuit() < c2.getSuit()) {
            return -1;
        }
        else if(c1.getSuit() == c2.getSuit()) {
            if(c1.getValue() < c2.getValue()) {
                return -1;
            }                       
            else if(c1.getValue() > c2.getValue()) {
                return 1;
            }
            else {
                return 0; // used only when same cards appear from combined, separate decks
            }
        }
        else {
            return 1;
        }
    }
}