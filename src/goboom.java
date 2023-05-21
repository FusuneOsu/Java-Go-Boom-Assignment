// *************************************************************************************************************************************
// Course: TCP1201 Object Oriented Programming and Data Structures 
// Year: Trimester 2, 2022/23 (T2220) 
// Lab: T13L 
// Names: Tam Yu Heng | Muhammad Imran Syafiq bin Muzarudin | Mohamed Kamal Eldin Bilal | Muhamad Syamil Imran Bin Mohd Mansor
// IDs: 1221304730 | 1201303111 | 1211311523 | 1221303708	
// Emails: 1221304730@student.mmu.edu.my | 1201303111@student.mmu.edu.my | 1211311523@student.mmu.edu.my | 1221303708@student.mmu.edu.my
// *************************************************************************************************************************************

import java.util.*;

// Card class representing a playing card
class Card {
    private String suit;
    private String rank;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    // Getters for suit and rank
    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    // ToString method to display card information
    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

// Player class representing a player in the game
class Player {
    private String name;
    private List<Card> hand;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    // Remove a card from the player's hand by card reference
    public void removeCardFromHand(Card card) {
        hand.remove(card);
    }
    
    // Add card to player's hand
    public void addCardToHand(Card card) {
        hand.add(card);
    }

    // Remove card from player's hand
    public Card playCard(int index) {
        return hand.remove(index);
    }

    // Get player's hand
    public List<Card> getHand() {
        return hand;
    }

    // ToString method to display player's name
    @Override
    public String toString() {
        return name;
    }
}

// GoBoomGame class representing the game itself
class GoBoomGame {
    private List<Card> deck;
    private List<Player> players;
    private Card leadCard;
    private int currentPlayerIndex;

    public GoBoomGame() {
        // Initialize deck, players, and other game variables
        initializeDeck();
        initializePlayers();
        shuffleDeck();
        dealCards();
        determineLeadPlayer();
        currentPlayerIndex = 0;
    }

    // Initialize the deck with 52 cards
    private void initializeDeck() {
        deck = new ArrayList<>();
        String[] suits = {"Spades", "Hearts", "Diamonds", "Clubs"};
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        for (String suit : suits) {
            for (String rank : ranks) {
                deck.add(new Card(suit, rank));
            }
        }
    }

    // Initialize the players
    private void initializePlayers() {
        players = new ArrayList<>();
        players.add(new Player("Player 1"));
        players.add(new Player("Player 2"));
        players.add(new Player("Player 3"));
        players.add(new Player("Player 4"));
    }

    // Shuffle the deck
    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    // Deal 7 cards to each player
    private void dealCards() {
        for (int i = 0; i < 7; i++) {
            for (Player player : players) {
                Card card = deck.remove(0);
                player.addCardToHand(card);
            }
        }
    }

    // Determine the lead player based on the rank of the lead card
    private void determineLeadPlayer() {
        leadCard = deck.remove(0);
        String leadRank = leadCard.getRank();

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            String playerName = player.toString();
            if ((leadRank.equals("A") || leadRank.equals("5") || leadRank.equals("9") || leadRank.equals("K")) && playerName.equals("Player 1")) {
                currentPlayerIndex = i;
                break;
            } else if ((leadRank.equals("2") || leadRank.equals("6") || leadRank.equals("10")) && playerName.equals("Player 2")) {
                currentPlayerIndex = i;
                break;
            } else if ((leadRank.equals("3") || leadRank.equals("7") || leadRank.equals("J")) && playerName.equals("Player 3")) {
                currentPlayerIndex = i;
                break;
            } else if ((leadRank.equals("4") || leadRank.equals("8") || leadRank.equals("Q")) && playerName.equals("Player 4")) {
                currentPlayerIndex = i;
                break;
            }
        }
    }

    // Play a round of the game
    private void playRound() {
        Player currentPlayer = players.get(currentPlayerIndex);
    
        // Display the lead card
        System.out.println("\nLead Card: " + leadCard + "\n");
    
        // Display current player's turn and their hand
        System.out.println(currentPlayer + "'s turn.");
        System.out.println("Hand: " + currentPlayer.getHand());
    
        // Get the card to play from the current player
        int cardIndex = getCardIndexToPlay(currentPlayer);
        Card playedCard = currentPlayer.playCard(cardIndex);
    
        // Display the card played by the current player
        System.out.println(currentPlayer + " played " + playedCard);
    
        // Check if the played card matches the suit or rank of the lead card
        if (!playedCard.getSuit().equals(leadCard.getSuit()) && !playedCard.getRank().equals(leadCard.getRank())) {
            boolean hasMatchingCard = false;
    
            // Check if the current player has a card that matches the suit or rank of the lead card
            for (Card card : currentPlayer.getHand()) {
                if (card.getSuit().equals(leadCard.getSuit()) || card.getRank().equals(leadCard.getRank())) {
                    hasMatchingCard = true;
                    break;
                }
            }
    
            if (hasMatchingCard) {
                System.out.println("Invalid move! The played card must match the suit or rank of the lead card.");
                currentPlayer.addCardToHand(playedCard); // Add the card back to the player's hand
            } else {
                // Update the lead card to the played card
                //leadCard = playedCard;
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size(); // Move to the next player
            }
        } else {
            // Update the lead card to the played card
            //leadCard = playedCard;
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size(); // Move to the next player
        }
    }
    
    // Get the index of the card to play from the current player
    private int getCardIndexToPlay(Player currentPlayer) {
        Scanner scanner = new Scanner(System.in);
        List<Card> hand = currentPlayer.getHand();
    
        boolean hasMatchingCard = false;
        for (Card card : hand) {
            if (card.getSuit().equals(leadCard.getSuit()) || card.getRank().equals(leadCard.getRank())) {
                hasMatchingCard = true;
                break;
            }
        }
    
        if (!hasMatchingCard) {
            System.out.println("You don't have any cards that match the lead card. Choose a card from the deck:");
    
            for (int i = 0; i < deck.size(); i++) {
                System.out.println((i + 1) + ": " + deck.get(i));
            }
    
            int cardIndex = -1;
            boolean validInput = false;
    
            while (!validInput) {
                System.out.print("Enter card index: \n");
                if (scanner.hasNextInt()) {
                    cardIndex = scanner.nextInt();
                    if (cardIndex >= 1 && cardIndex <= deck.size()) {
                        Card chosenCard = deck.get(cardIndex - 1);
                        if (chosenCard.getSuit().equals(leadCard.getSuit()) || chosenCard.getRank().equals(leadCard.getRank())) {
                            validInput = true;
                        } else {
                            System.out.println("Invalid choice! The chosen card must match the suit or rank of the lead card.");
                        }
                    } else {
                        System.out.println("Invalid card index! Please enter a valid index.");
                    }
                } else {
                    System.out.println("Invalid input! Please enter a valid card index.");
                    scanner.next(); // Consume invalid input
                }
            }
    
            // Remove the chosen card from the deck
            Card chosenCard = deck.remove(cardIndex - 1);
            currentPlayer.addCardToHand(chosenCard);
    
            return currentPlayer.getHand().indexOf(chosenCard);
        } else {
            System.out.println("Choose a card to play by entering its index from your hand:");
            for (int i = 0; i < hand.size(); i++) {
                System.out.println((i + 1) + ": " + hand.get(i));
            }
    
            int cardIndex = -1;
            boolean validInput = false;
    
            while (!validInput) {
                System.out.print("Enter card index: ");
                if (scanner.hasNextInt()) {
                    cardIndex = scanner.nextInt();
                    if (cardIndex >= 1 && cardIndex <= hand.size()) {
                        Card chosenCard = hand.get(cardIndex - 1);
                        if (chosenCard.getSuit().equals(leadCard.getSuit()) || chosenCard.getRank().equals(leadCard.getRank())) {
                            validInput = true;
                        } else {
                            System.out.println("\nInvalid choice! The chosen card must match the suit or rank of the lead card.");
                        }
                    } else {
                        System.out.println("\nInvalid card index! Please enter a valid index.");
                    }
                } else {
                    System.out.println("\nInvalid input! Please enter a valid card index.");
                    scanner.next(); // Consume invalid input
                }
            }
    
            return cardIndex - 1;
        }
    }
    

    // Check if the game is over
    private boolean isGameOver() {
        // Implement your logic to determine if the game is over
        // For example, if any player's hand is empty
        for (Player player : players) {
            if (player.getHand().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // Play the game
    public void play() {
        System.out.println("Starting a new game of Go Boom!");

        while (!isGameOver()) {
            playRound();
        }

        System.out.println("Game over!");
       // Find the player with an empty hand, indicating the winner
        Player winner = null;
        int winnerIndex = -1;
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (player.getHand().isEmpty()) {
                winner = player;
                winnerIndex = i;
                break;
            }
    }

    if (winner != null) {
        System.out.println("The winner is: " + winner + " (Player " + (winnerIndex + 1) + ")");
    } else {
        System.out.println("No winner!");
    }

    System.out.println();
    }
}

// Main class to start the game
    public class goboom {
    public static void main(String[] args) {
        GoBoomGame game = new GoBoomGame();
        game.play();
    }
}
