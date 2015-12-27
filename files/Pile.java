import java.util.LinkedList;
import java.util.Set;


/**
 * The Pile class
 * <p>
 * Used to hold the cards which are not currently <br>
 * <pre>
 * 1. in a deck
 * 2. in a hand
 * 3. in a discard pile
 * <p>
 * This will include the trash pile.
 * 
 * @author Paul
 *
 */
public class Pile {
	public final Card c;
	private int numRemaining;
	
	public Pile(int n, Card c) {
		this.c = c;
		numRemaining = n;
		//System.out.println(c.getName() + ": " + n);
	}
	
	public Card takeCard() {
		if(numRemaining == 0) return null;
		numRemaining--;
		return new Card(c);
	}
	
	public boolean isEmpty() {
		return numRemaining == 0;
	}
	
	public int size() {
		// System.out.println(numRemaining);
		return numRemaining;
	}
	
	@Override
	public String toString() {
		return c.toString() + ": " + numRemaining + " left, $" + c.cost();
	}
}