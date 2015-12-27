/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	public void run() {
		// NOTE : recall that the 'final' keyword notes inmutability
		// even for local variables.

		// Top-level frame in which game components live
		// Be sure to change "TOP LEVEL FRAME" to the name of your game
		final JFrame frame = new JFrame("DOMINION");
		frame.setLocation(10, 10);

		// Status panel
		final JPanel status_panel = new JPanel();
		frame.add(status_panel, BorderLayout.SOUTH);
		final JLabel status = new JLabel("Running...");
		status_panel.add(status);
		
		// Pick the cards you want to use:
		Card[] kingdomCards = new Card[10];
		
		ArrayList<Card> cards = new ArrayList<Card>(18);
		cards.add(Card.createCellar());
		cards.add(Card.createChancellor());
		cards.add(Card.createChapel());
		cards.add(Card.createCouncilRoom());
		cards.add(Card.createFeast());
		cards.add(Card.createFestival());
		cards.add(Card.createGardens());
		cards.add(Card.createLaboratory());
		cards.add(Card.createMarket());
		cards.add(Card.createMoneylender());
		cards.add(Card.createRemodel());
		cards.add(Card.createSmithy());
		cards.add(Card.createSpy());
		cards.add(Card.createThroneRoom());
		cards.add(Card.createVillage());
		cards.add(Card.createWitch());
		cards.add(Card.createWoodcutter());
		cards.add(Card.createWorkshop());
		
		int i = 0;
		while (i < 10) {
			Object[] possibilities = cards.toArray();
			
			Card c = (Card) JOptionPane.showInputDialog(
								frame,
								"Pick the Cards you want to play with",
								"Pick a Card",
								JOptionPane.PLAIN_MESSAGE,
								null,
								possibilities,
								possibilities[0]);
			
			boolean b;
			
			Object[] options = {"Yes", "No"};
			int n = JOptionPane.showOptionDialog(frame,
	        			"Use This Card?",
	        			"A Silly Question",
	        			JOptionPane.YES_NO_OPTION,
	        			JOptionPane.QUESTION_MESSAGE,
	        			c.getImageIcon(),
	        			options,
	        			options[0]);
	        if (n == JOptionPane.YES_OPTION) {
	        		b = true;
	        } else if (n == JOptionPane.NO_OPTION) {
	        		b = false;
	        } else {
	        		b = false;
	        }
	        
	        if (b) {
	        	kingdomCards[i] = c;
	        	cards.remove(c);
	        	i++;
	        }
	        	
		}
		
		ArrayList<String> playerNames = new ArrayList<String>();
		
		i = 1;
		while (true) {
			String s = (String)JOptionPane.showInputDialog(
                    frame,
                    "Enter Player " + i + "\'s name.",
                    "Customized Dialog",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "ham");
			
			playerNames.add(s);
			i++;
			
			Object[] options = {"Yes", "No"};
			int n = JOptionPane.showOptionDialog(frame,
	        			"Add another player?",
	        			"Player add pane",
	        			JOptionPane.YES_NO_OPTION,
	        			JOptionPane.QUESTION_MESSAGE,
	        			null,
	        			options,
	        			options[0]);
	        if (n == JOptionPane.YES_OPTION) {
	        		
	        } else if (n == JOptionPane.NO_OPTION) {
	        		break;
	        } else {
	        		break;
	        }
			
		}
		
		// Main playing area
		final Dominion dom = new Dominion(playerNames, kingdomCards, frame);
		frame.add(dom, BorderLayout.CENTER);

		

		// Note here that when we add an action listener to the reset
		// button, we define it as an anonymous inner class that is
		// an instance of ActionListener with its actionPerformed()
		// method overridden. When the button is pressed,
		// actionPerformed() will be called.
		

		// Put the frame on the screen
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// Start game
		boolean endGame = false;
		while(!endGame) {
			endGame = dom.runTurn();
		}
	}

	/*
	 * Main method run to start and run the game Initializes the GUI elements
	 * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
	 * this in the final submission of your game.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
}
