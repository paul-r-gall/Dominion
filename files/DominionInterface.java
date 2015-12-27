import java.util.ArrayList;
import java.util.Set;

import javax.swing.*;


/**
 * A DominionInterface is the UI for the Dominion game. 
 * <p>
 * It contains the methods to get any kind of user input after the game has 
 * actually begun.
 * @author Paul
 *
 */

public class DominionInterface {
	
	public static boolean pickThisCard(JPanel panel, Card c, String message) {
		Object[] options = {"Yes", "No"};
        int n = JOptionPane.showOptionDialog(panel,
                        message,
                        "A Silly Question",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        c.getImageIcon(),
                        options,
                        options[0]);
        if (n == JOptionPane.YES_OPTION) {
            return true;
        } else if (n == JOptionPane.NO_OPTION) {
            return false;
        } else {
            return false;
        }
	}

	public static Pile pickPile(JPanel panel, Dominion status, String message) {
		ArrayList<Pile> piles = new ArrayList<Pile>(17);
		for (int i = 0; i < 10; i++) {
			piles.add(status.KINGDOM_CARDS[i]);
		}
		piles.add(status.COPPER_PILE);
		piles.add(status.SILVER_PILE);
		piles.add(status.GOLD_PILE);
		
		piles.add(status.ESTATE_PILE);
		piles.add(status.DUCHY_PILE);
		piles.add(status.PROVINCE_PILE);
		
		piles.add(status.CURSE_PILE);
		
		Object[] possibilities = piles.toArray();
		
		Pile p = (Pile) JOptionPane.showInputDialog(
							panel,
							message,
							"Pick a Pile",
							JOptionPane.PLAIN_MESSAGE,
							null,
							possibilities,
							possibilities[0]);
		
		return p;
	}
	
	public static Card getCardFromHand(JPanel panel, Player p, String message) {
		ArrayList<Card> cards = p.getHand();
		Object[] possibilities = cards.toArray();
		
		
        Card c = (Card) JOptionPane.showInputDialog(
                            panel,
                            message,
                            "Pick a Card",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            possibilities,
                            possibilities[0]);
        
		return c;
	}
	
	
	public static void showMessage(JPanel panel, String message) {
		JOptionPane.showMessageDialog(panel, message);
	}
	
	public static boolean boolPrompt(JPanel panel, String message) {
		
		Object[] options = {"Yes", "No"};
        int n = JOptionPane.showOptionDialog(panel,
                        message,
                        "A Silly Question",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);
        if (n == JOptionPane.YES_OPTION) {
            return true;
        } else if (n == JOptionPane.NO_OPTION) {
            return false;
        } else {
            return false;
        }
		
	}
	
	public static void showError(JPanel panel, String message) {
		
	}
	
	
}