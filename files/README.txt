Description of the implementation of the Game DOMINION

Paul Gallagher, for CIS120 final project

An overview of the classes:

Function:
An interface containing a single method "func", which takes in a Dominion status, and Player p. This will become an anonymous inner class in the Card class.


Card:
The basic card class. It has private variables for cost, value, victory points, name, ImageIcon, and public final Function, that is, what happens when the card is played. 

There are two constructors for this class: one is a standard constructor where you pass in the cost, value, victorypts, name, image file, and Function object, and it creates a card. The other constructor is a cloning constructor, where I pass in a card, and it returns a card which is structurally identical. 

I chose not to override the .equals() method, because I want different pointers to the same card type to be distinct. Instead, I have a .isCardType(String cardName) method to detect when two cards are the same type. 

To construct the different varieties of cards, I have static methods inside the card class, each of which creates a single card of the given type. For example, the method createCellar() will return a single card which has the functionality of the Cellar type. I made these methods so that I didn't have to create 27 different classes - one for each card. 


Pile: 
The basic class for Piles of cards. It has one private int for the number of cards left in the pile, and one public final Card for the type of the pile. I overrode the toString() method to give information about number remaining, and cost of the pile. 


Player:
The basic Player class. It has private linked list for the deck, and arrayLists for the hand, discardPile, and playedCards. It also has private ints for victory points, actions, buys, and money. Finally, there's a private Dominion for the current status of the game. 

I made a bunch of public methods here, because the Cards can do all sorts of things to the Players, and I wanted the cards to just call the appropriate method from the player when it did something. 

I've also overridden paintComponent(). 


Dominion:
The class for the state of the game. Stores an array of Kingdom Card piles, and final Piles for the treasure, victory, and curse cards. Also stores a LinkedList for the players. 

There's not that many methods here. One of them detects the end of the game (when three piles are exhausted). One of them runs the current player's turn. I've also overridden the paintComponent method, though that's not perfect.


DominionInterface:
Here are a bunch of static methods for throwing up the dialog boxes which run the user interaction with the game. Each static method makes a different kind of dialog box for different situations which might arise during the game. The dialog boxes all have the Dominion status of the game as the parent JPanel. 

******************************************

It's a pity that the implementation isn't quite working yet. Oh well.