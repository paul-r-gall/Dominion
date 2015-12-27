import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * The Card interface. 
 * <p>
 * Any card will implement this. Cards can be played 
 * @author Paul
 *
 */
public class Card {
	private int value;
	private int cost;
	private int victPt;
	public final Function FUNC;
	private final boolean isAction;
	private final String name;
	private final ImageIcon img;
	
	/** 
	 * The most basic constructor. Pass in your data, get out a card.
	 * @param value
	 * @param cost
	 * @param victPt
	 * @param f
	 */
	public Card(int value, int cost, int victPt, Function f, String name,
			String imgFile) {
		this.value = value;
		this.cost = cost;
		this.victPt = victPt;
		this.FUNC = f;
		if(f == null) isAction = false;
		else isAction = true;
		this.name = name;
		img = new ImageIcon(imgFile);
	}
	
	/**
	 * A cloning constructor. Constructs a card which is identical to the 
	 * passed in card.
	 * @param c
	 */
	public Card(Card c) {
		this.value = c.value();
		this.cost = c.cost();
		this.victPt = c.victPt();
		this.FUNC = c.FUNC;
		if (FUNC == null) this.isAction = false;
		else isAction = true;
		this.name = c.getName();
		this.img = c.getImageIcon();
	}
	
	/**
	 * The following static methods return the different types of cards 
	 * available. 
	 * @return
	 */
	public static Card createCopper() {
		return new Card(1, 0, 0, null, "Copper", "DominionDocs/copper.jpg");
	}
	public static Card createSilver() {
		return new Card(2, 3, 0, null, "Silver", "DominionDocs/Silver.jpg");
	}
	public static Card createGold() {
		return new Card(3, 6, 0, null, "Gold", "DominionDocs/Gold.jpg");
	}
	public static Card createEstate() {
		return new Card(0, 2, 1, null, "Estate", "DominionDocs/Estate.jpg");
	}
	public static Card createDuchy() {
		return new Card(0, 5, 3, null, "Duchy", "DominionDocs/Duchy.jpg");
	}
	public static Card createProvince() {
		return new Card(0, 8, 6, null, "Province", "DominionDocs/Province.jpg");
	}
	public static Card createCurse() {
		return new Card(0, 0, -1, null, "Curse", "DominionDocs/Curse.jpg");
	}
	
	public static Card createCellar() {
		return new Card(0, 2, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						// gain an action
						currentPlayer.incActions(1);
						
						// discard n cards, draw n cards.
						int n = 0;
						
						while (true) {
							if (!DominionInterface.boolPrompt(status,
									"Discard a card?")) break;
							
							Card c = DominionInterface.getCardFromHand(status, currentPlayer,
									"Pick a card to discard.");
							currentPlayer.discard(c);
							n++;
							status.repaint();
						}
						
						currentPlayer.drawCard(n);
					}
				}, "Cellar", "DominionDocs/cellar.jpg");
	}
	
	public static Card createChapel() {
		return new Card(0, 2, 0,
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						// trash any number of cards from your hand.
						while (true) {
							if (!DominionInterface.boolPrompt(status,
									"Trash a card?")) break;
							
							Card c = DominionInterface.getCardFromHand(status,
									currentPlayer,
									"Pick a card to trash.");
							currentPlayer.trashSingleCard(c);
							status.repaint();
						}
					}
				}, "Chapel", "DominionDocs/chapel.jpg");
	}
	
	/*
	public static Card createMoat() {
		return new Card(0, 2, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						currentPlayer.drawCard(2);
						// figure out how to block attacks
					}
		}, "Moat", "DominionDocs/moat.jpg");
	}
	*/
	
	public static Card createChancellor() {
		return new Card(0, 3, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						currentPlayer.incMoney(2);
						if (DominionInterface.boolPrompt(status, 
								"Discard entire hand?")) {
							currentPlayer.discard();
						}
					}
		}, "Chancellor", "DominionDocs/chancellor.jpg");
	}
	
	public static Card createVillage() {
		return new Card(0, 3, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						currentPlayer.drawCard();
						currentPlayer.incActions(2);
					}
		}, "Village", "DominionDocs/village.jpg");
	}
	
	public static Card createWoodcutter() {
		return new Card(0, 3, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						currentPlayer.incBuys(1);
						currentPlayer.incMoney(2);
					}
		}, "Woodcutter", "DominionDocs/woodcutter.jpg");
	}
	
	public static Card createWorkshop() {
		return new Card(0, 3, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						// gain a card costing up to $4
						
						Pile p = null;
						
						do {
							p = DominionInterface.pickPile(status, status, 
								"Pick a card costing up to $4");
							if (p.isEmpty()) {
								DominionInterface.showMessage(status, "No more left!");
							} else if (p.c.cost() > 4) {
								DominionInterface.showMessage(status, "Too expensive!");
							}
						} while (p.c.cost() > 4 || p.isEmpty());
						
						currentPlayer.gainCard(p);
					}
		}, "Workshop", "DominionDocs/workshop.jpg");
	}
	
	/*
	public static Card createBureaucrat() {
		return new Card(0, 4, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						// gain a silver card on TOP of deck
						// every player reveals a vict card in hand, puts on deck
						// or reveals hand with no vict cards
					}
		}, "Bureaucrat", "DominionDocs/bureaucrat.jpg");
	}
	*/
	
	public static Card createFeast() {
		final Card c;
		c = new Card(0, 4, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						// Trash this card, gain a card costing up to $5
						// the card is trashed in the Player class after being
						// played. I can't figure out how to reference itself.
						Pile p = null;
						
						do {
							p = DominionInterface.pickPile(status, status, 
								"Pick a card costing up to $5");
							if (p.isEmpty()) {
								DominionInterface.showMessage(status, "No more left!");
							} else if (p.c.cost() > 5) {
								DominionInterface.showMessage(status, "Too expensive!");
							}
						} while (p.c.cost() > 5 || p.isEmpty());
						
						currentPlayer.gainCard(p);
					}
		}, "Feast", "DominionDocs/feast.jpg");
		return c;
	}
	
	public static Card createGardens() {
		return new Card(0, 4, 0, null, "Gardens", "DominionDocs/gardens.jpg");
	}
	
	/*
	public static Card createMilitia() {
		return new Card(0, 4, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						currentPlayer.incMoney(2);
						// other players discard cards of their choice . . .
					}
		}, "Militia", "DominionDocs/militia.jpg");
	}
	*/
	
	public static Card createMoneylender() {
		return new Card(0, 4, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						
						// Option to trash a copper, if done, +$3.
						if (DominionInterface.boolPrompt(status, "Trash a copper?")) {
							Card c = currentPlayer.handContains("Copper");
							if (c == null) {
								DominionInterface.showMessage(status, "No Coppers! Idiot.");
								return;
							}
							currentPlayer.trashSingleCard(c);
							currentPlayer.incMoney(3);
						}
					}
		}, "Moneylender", "DominionDocs/moneylender.jpg");
	}
	
	public static Card createRemodel() {
		return new Card(0, 4, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						// Trash a card, 
						Card c = DominionInterface.getCardFromHand(status, 
								currentPlayer, 
								"Pick a card to trash");
						int theCost = c.cost();
						currentPlayer.trashSingleCard(c);
						
						// gain a card costing up to $2 more.
						Pile p = null;
						do {
							p = DominionInterface.pickPile(status, status, 
									"Pick a card costing up to " + (2 + theCost));
							if (p.isEmpty()) {
								DominionInterface.showMessage(status, 
										"That pile is empty!");
							} else if (p.c.cost() > theCost + 2) {
								DominionInterface.showMessage(status, 
										"Insufficient Funds!");
							} else {
								currentPlayer.gainCard(p);
							}
						} while (p.isEmpty() || p.c.cost() > theCost + 2);
							
					}
		}, "Remodel", "DominionDocs/remodel.jpg");
	}
	
	public static Card createSmithy() {
		return new Card(0, 4, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						currentPlayer.drawCard(3);
					}
		}, "Smithy", "DominionDocs/smithy.jpg");
	}
	
	public static Card createSpy() {
		return new Card(0, 4, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						currentPlayer.drawCard();
						currentPlayer.incActions(1);
						
						// reveal top card, put it back or discard your choice.
						if (DominionInterface.pickThisCard(status, currentPlayer.peekAtDeck(), 
								"Discard This Card?")) {
							currentPlayer.discardFromDeck();
						}
						
					}
		}, "Spy", "DominionDocs/spy.jpg");
	}
	/*
	 
	public static Card createThief() {
		return new Card(0, 4, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						// every player reveals 2 cards. 
						// current player can trash a treasure card
						// or keep it.
						// opponents discard all revealed cards
					}
		}, "Thief", "DominionDocs/thief.jpg");
	}
	*/
	
	public static Card createThroneRoom() {
		return new Card(0, 4, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						// play the next action card twice
						// fuck this card
						currentPlayer.fuckThroneRoom();
					}
		}, "Throne Room", "DominionDocs/ThroneRoom.jpg");
	}
	
	public static Card createCouncilRoom() {
		return new Card(0, 5, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						currentPlayer.drawCard(4);
						currentPlayer.incBuys(1);
						for (Player p : status.PLAYERS) {
							if (p != currentPlayer) p.drawCard();
						}
					}
		}, "Council Room", "DominionDocs/CouncilRoom.jpg");
	}
	
	public static Card createFestival() {
		return new Card(0, 5, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						currentPlayer.incActions(2);
						currentPlayer.incBuys(1);
						currentPlayer.incMoney(2);
					}
		}, "Festival", "DominionDocs/festival.jpg");
	}
	
	public static Card createLaboratory() {
		return new Card(0, 5, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						currentPlayer.drawCard(2);
						currentPlayer.incActions(1);
					}
		}, "Laboratory", "DominionDocs/laboratory.jpg");
	}
	
	/*
	public static Card createLibrary() {
		return new Card(0, 5, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						// draw until 7 cards in hand. 
						// can set aside action cards drawn like this
						// discard set aside cards after done drawing 
					}
		}, "Library", "DominionDocs/library.jpg");
	}
	*/
	
	public static Card createMarket() {
		return new Card(0, 5, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						currentPlayer.incBuys(1);
						currentPlayer.incMoney(1);
						currentPlayer.drawCard(1);
						currentPlayer.incActions(1);
					}
		}, "Market", "DominionDocs/market.jpg");
	}
	
	/*
	public static Card createMine() {
		return new Card(0, 5, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						// trash a treasure card, gain a treasure card costing
						// up to $3 more, put into hand.
					}
		}, "Mine", "DominionDocs/mine.jpg");
	}
	*/
	
	public static Card createWitch() {
		return new Card(0, 5, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						currentPlayer.drawCard(2);
						for (Player p : status.PLAYERS){
							if (p != currentPlayer) 
								p.gainCard(status.CURSE_PILE);
						}
					}
		}, "Witch", "DominionDocs/witch.jpg");
	}
	
	/*
	public static Card createAdventurer() {
		return new Card(0, 6, 0, 
				new Function() {
					public void fun(Player currentPlayer, Dominion status) {
						int n = 0;
						
						// Reveal cards from deck until two treasure cards
						// are revealed. Put those cards into your hand, and 
						// discard the others. 
						while (n < 2) {
							if(currentPlayer.peekAtDeck().value() != 0){
								 currentPlayer.discardFromDeck();
							} else {
								currentPlayer.drawCard();
								n++;
							}
						}
						// there's a problem here - it won't display the cards
						// have to fix this. 
					}
		}, "Adventurer", "DominionDocs/adventurer.jpg");
	}
	*/
	/**
	 * gives the value
	 * @return value of coin or 0 if not a coin card
	 */
	public int value() {
		return value;
	}
	
	/**
	 * 
	 * @return cost of the card
	 */
	public int cost() {
		return cost;
	}
	/**
	 * gives the vict pts this card holds.
	 * @return num of victory points or 0 if not a vict pt card.
	 */
	public int victPt() {
		return victPt;
	}
	
	
	
	
	/**
	 * draw the card
	 * @param g the graphics interface
	 */
	public void draw(Graphics g) {
		
	}

	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @param s the name of a card
	 * @return whether the card has that name. 
	 */
	public boolean isCardType(String s) {
		return (name.equals(s));
	}
	
	public static boolean isTreasureCard(Card c) {
		if (c.value() == 0) return false;
		return true;
	}
	
	public static boolean isVictoryCard(Card c) {
		if (c.victPt() == 0 && !c.getName().equals("Gardens")) return false;
		return true;
	}
	
	public static boolean isActionCard(Card c) {
		if (c.FUNC == null) return false;
		return true;
	}
	
	public ImageIcon getImageIcon() {
		return img;
	}
	
	@Override
	public String toString() {
		return name;
	}
}