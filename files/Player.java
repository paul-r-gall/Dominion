import java.awt.*;
import java.awt.event.*;
import java.util.Collections;

import javax.swing.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;


@SuppressWarnings("serial")
public class Player extends JPanel {
	/**
	 * Here's all the logic that's going to be contained in the Player class
	 * 
	 * The information needed to draw the JFrame comes later.
	 */
	private LinkedList<Card> deck;
	private ArrayList<Card> hand;
	private ArrayList<Card> discardPile;
	private ArrayList<Card> playedCards;
	
	private int actions;
	private int buys;
	private int victPts;
	private int money;
	
	private static int playerCounter = 0;
	
	private final int playerNumber;
	private final String name;
	
	private Dominion status;
	
	private int throneRoomHandler = 0;
	
	
	public Player(Dominion status, String name) {
		/** 
		 * Here's the logical information for the player.
		 */
		playerCounter++;
		playerNumber = playerCounter;
		
		this.status = status;
		this.name = name;
		
		deck = new LinkedList<Card>();
		
		discardPile = new ArrayList<Card>();
		
		hand = new ArrayList<Card>();
		
		playedCards = new ArrayList<Card>();
		
		for (int i = 0; i < 7; i++) {
			deck.add(status.COPPER_PILE.takeCard());
		}
		
		
		
		for (int i = 0; i < 3; i++) {
			deck.add(status.ESTATE_PILE.takeCard());
		}
		
		
		
		Collections.shuffle(deck);
		
		for (int i = 0; i < 5; i++) drawCard();
		actions = 1;
		buys = 1;
		
		
		/**
		 * Here's the information for drawing the Player.
		 */
		
		
	}
	
	public void drawCard() {
		
		if(deck.isEmpty()) {
			deck.addAll(discardPile);
			discardPile = new ArrayList<Card>();
			Collections.shuffle(deck);
		}
		
		
		if(deck.isEmpty()) return;
		hand.add(deck.remove(0));
		status.repaint();
	}
	
	public void drawCard(int n) {
		for (int i = 0; i < n; i++) drawCard();
	}
	
	public void endTurn() {
		discardPile.addAll(playedCards);

		discard();
		
		playedCards = new ArrayList<Card>();
		
		for (int i = 0; i < 5; i++) drawCard();
		status.repaint();
	}
	
	public void beginTurn() {
		buys = 1;
		actions = 1;
		money = computeMoney();
		status.repaint();
	}
	
	/**
	 * discards entire hand
	 */
	public void discard() {
		discardPile.addAll(hand);
		hand = new ArrayList<Card>();
		status.repaint();
	}
	
	/**
	 * discards the given card
	 * @param c the card to discard
	 */
	public void discard(Card c) {
		discardPile.add(c);
		hand.remove(c);
		status.repaint();
	}
	
	public void trashSingleCard(Card c) {
		hand.remove(c);
		discardPile.remove(c);
		deck.remove(c);
		playedCards.remove(c);
		status.repaint();
	}
	
	public void discardFromDeck() {
		if (deck.isEmpty()) {
			deck.addAll(discardPile);
			discardPile = new ArrayList<Card>();
			Collections.shuffle(deck);
		}
		discardPile.add(deck.remove(0));
		status.repaint();
	}
	
	public Card peekAtDeck() {
		return deck.peek();
	}
	
	public void playSingle(Card c) {
		if (c.FUNC == null) return;
		
		playedCards.add(c);
		hand.remove(c);
		c.FUNC.fun(this, status);
		
		if (throneRoomHandler > 0) {
			c.FUNC.fun(this, status);
			throneRoomHandler--;
		}
		
		// trash it if it's a feast card.
		if (c.isCardType("Feast")) trashSingleCard(c);
		
		actions--;
		status.repaint();
	}
	
	public String buyCard(Pile p) {
		if (p.c.cost() > money) {
			return "Insufficient Funds";
		}
		Card c = p.takeCard();
		if (c == null) {
			return "No more cards in this pile";
		}
		money -= p.c.cost();
		discardPile.add(c);
		buys--;
		status.repaint();
		return null;
	}
	
	public void gainCard(Pile p) {
		Card c = p.takeCard();
		if (c == null) {
			return;
		}
		discardPile.add(c);
		status.repaint();
	}
	
	public void gainCard(Card c) {
		discardPile.add(c);
		status.repaint();
	}
	
	public ArrayList<Card> getHand() {
		return new ArrayList<Card>(hand);
	}
	
	public void fuckThroneRoom() {
		throneRoomHandler++;
	}
	
	private int computeMoney() {
		money = 0;
		for(Card c : hand) {
			money += c.value();
		}
		System.out.println(money);
		return money;
	}
	
	public int computeVictPts() {
		victPts = 0;
		for (Card c : hand) {
			victPts += c.victPt();
			if (c.isCardType("Gardens")) {
				victPts += (deck.size() + hand.size() + discardPile.size())/10;
			}
		}
		
		for (Card c : deck) {
			victPts += c.victPt();
			if (c.isCardType("Gardens")) {
				victPts += (deck.size() + hand.size() + discardPile.size())/10;
			}
		}
		
		for (Card c : discardPile) {
			victPts += c.victPt();
			if (c.isCardType("Gardens")) {
				victPts += (deck.size() + hand.size() + discardPile.size())/10;
			}
		}
		
		return victPts;
	}
	/**
	 * 
	 * @param s the name of the card you're looking for.
	 * @return c the first card with that name or null if there is no such
	 * card
	 *
	 */
	public Card handContains(String s) {
		for (Card c : hand) {
			if (c.isCardType(s)) return c;
		}
		return null;
	}
	
	public void incBuys(int n) {
		buys += n;
		status.repaint();
	}
	
	public void incMoney(int n) {
		money += n;
		status.repaint();
	}
	
	public void incActions(int n) {
		actions += n;
		status.repaint();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public int getMoney() {
		return money;
	}
	
	public int getActions() {
		return actions;
	}
	
	public int getBuys() {
		return buys;
	}
	
	public int getHandSize() {
		return hand.size();
	}
	
	public int getDeckSize() {
		return deck.size();
	}
	
	/**
	 * Here's the code for actually drawing the JPanel. 
	 */
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString("Deck", 0, 0);
		g.drawString("Discard Pile", 0, 400);
		drawHand(g);
		g.drawString("Actions: " + actions, 600, 0);
		g.drawString("Buys: " + buys, 600, 150);
		g.drawString("Money: " + money, 600, 300);
		
		drawPlayedCards(g);
	}
	
	private void drawHand(Graphics g) {
		g.drawString("Hand:", 300, 0);
		int j = 1;
		for (Card c : hand) {
			g.drawString(c.getName(), 300, j * 75);
			j++;
		}
	}
	
	private void drawPlayedCards(Graphics g) {
		g.drawString("Played Cards", 900, 0);
		int j = 1;
		for (Card c : playedCards) {
			g.drawString(c.getName(), 900, j * 75);
			j++;
		}
	}
}