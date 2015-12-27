import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.*;

/**
 * The class for a game of Dominion. It has as private variables the following:
 * 
 * 1. The array of players
 * 2. The current player
 * 3. The Piles of cards which are in play
 * 4. 
 * 
 * I'm thinking I might put all the display methods in here, since inside
 * the card function I'll have access to the status. That's what will have to
 * happen. Probably.
 * @author Paul
 *
 */

@SuppressWarnings("serial")
public class Dominion extends JPanel {
	// the list of players. The first player is the current one. 
	public final LinkedList<Player> PLAYERS;
	
	private JFrame frame;
	
	private Player currentPlayer;
	
	public final Pile[] KINGDOM_CARDS = new Pile[10];
	
	public final Pile GOLD_PILE = new Pile(30, Card.createGold());
	public final Pile SILVER_PILE = new Pile(40, Card.createSilver());
	public final Pile COPPER_PILE = new Pile(60, Card.createCopper());
	public final Pile ESTATE_PILE = new Pile(24, Card.createEstate());
	public final Pile DUCHY_PILE = new Pile(12, Card.createDuchy());
	public final Pile PROVINCE_PILE = new Pile(12, Card.createProvince());
	public final Pile CURSE_PILE = new Pile(30, Card.createCurse());
	
	
	
	public Dominion(ArrayList<String> playerNames, Card[] kingdom_cards, JFrame frame) {
		
		
		PLAYERS = new LinkedList<Player>();
		
		for (String s : playerNames) {
			PLAYERS.add(new Player(this, s));
		}
		
		
		for (int i = 0; i < 10; i++) {
			KINGDOM_CARDS[i] = new Pile(10, kingdom_cards[i]);
		}
		currentPlayer = PLAYERS.poll();
		this.frame = frame;
	}
	
	public boolean isEndGame() {
		int n = 0;
		for (Pile p : KINGDOM_CARDS) {
			if (p.isEmpty()) n++;
		}
		
		if (GOLD_PILE.isEmpty()) n++;
		if (SILVER_PILE.isEmpty()) n++;
		if (COPPER_PILE.isEmpty()) n++;
		if (ESTATE_PILE.isEmpty()) n++;
		if (DUCHY_PILE.isEmpty()) n++;
		if (PROVINCE_PILE.isEmpty()) n++;
		if (CURSE_PILE.isEmpty()) n++;
		
		if (n > 2) return true;
		return false;
	}
	
	public boolean runTurn() {
		Player p = currentPlayer;
		
		p.beginTurn();
		
		// action phase
		while (p.getActions() > 0 && p.getHandSize() > 0) {
			
			
			if (!DominionInterface.boolPrompt(this, "Play a Card?")) break;
			System.out.println("This should not print");
			// pick the card
			Card c = DominionInterface.getCardFromHand(this, p, "Pick a Card to play");
			
			if (c == null) break;
			
			// play the card
			if(DominionInterface.pickThisCard(this, c, "Play this card?")) {
				p.playSingle(c);
			}
			
			repaint();
		}
		
		System.out.println("test");
		System.out.println("Money = " + p.getMoney());
		System.out.println(p.getBuys());
		
		// buy phase
		while (p.getBuys() > 0 && p.getMoney() > 0) {
			System.out.println("will this get printed?");
			if (!DominionInterface.boolPrompt(this, "Buy a Card?")) break;
			
			// pick the card
			Pile buyPile = DominionInterface.pickPile(this, this, "Pick a card to buy");
			
			
			if (buyPile == null) break; 
			
			String message = p.buyCard(buyPile);
			
			if (DominionInterface.pickThisCard(this, buyPile.c, "Buy this Card?")) {
				
				// if the buy goes through, message is null. 
				if(message == null) {
				
					repaint();
					continue;
				}
			
				DominionInterface.showMessage(this, message);
			}
			
			repaint();
			
		}
		
		
		
		//end turn
		p.endTurn();
		
		PLAYERS.add(p);
		currentPlayer = PLAYERS.poll();
		
		if (isEndGame()) {
			endGame();
			return true;
		}
		repaint();
		
		return false;
	}
	
	
	
	private void endGame() {
		Player winner = PLAYERS.peek();
		
		for (Player p : PLAYERS) {
			if (p.computeVictPts() > winner.computeVictPts()) winner = p;
		}
		JOptionPane.showMessageDialog(this, 
				"GAME OVER\n" + 
				"Winner: " + winner + " " + winner.computeVictPts());
	}
	
	/***********************************************************
	 * drawing methods and getting information methods below
	 *
	 *
	 *
	 **************************************************************/
	
	
	
	
	
	@Override
	public void paintComponent(Graphics g) {
		g.translate(0, 10);
		
		super.paintComponent(g);
		
		currentPlayer.paintComponent(g);
		
		// now paint the Piles
		
		g.drawString("Piles Available:", 0, 800);
		
		int j = 1;
		for (Pile p : KINGDOM_CARDS) {
			drawPile(p, 0, 800 + 75 * j, g);
		}
		
		drawPile(COPPER_PILE, 300, 875, g);
		drawPile(SILVER_PILE, 300, 950, g);
		drawPile(GOLD_PILE, 300, 1025, g);
		drawPile(ESTATE_PILE, 300, 1100, g);
		drawPile(DUCHY_PILE, 300, 1175, g);
		drawPile(PROVINCE_PILE, 300, 1250, g);
		drawPile(CURSE_PILE, 300, 1250, g);
		
	}
	
	public void drawPile(Pile p, int x, int y, Graphics g) {
		Card c = p.c;
		g.drawString(c.getName() + ": " + p.size() + " left, $" + c.cost(),
				x, y);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(1500, 3000);
	}
	
	
}