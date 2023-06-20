// *************************************************************************************************************************************
// Course: TCP1201 Object Oriented Programming and Data Structures 
// Year: Trimester 2, 2023 (T2220) 
// Lab: T13L 
// Names: Tam Yu Heng | Muhammad Imran Syafiq bin Muzarudin | Mohamed Kamal Eldin Bilal | Muhamad Syamil Imran Bin Mohd Mansor
// IDs: 1221304730 | 1201303111 | 1211311523 | 1221303708	
// Emails: 1221304730@student.mmu.edu.my | 1201303111@student.mmu.edu.my | 1211311523@student.mmu.edu.my | 1221303708@student.mmu.edu.my
// *************************************************************************************************************************************

import java.util.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;

// Card class representing a playing card
class Card implements Serializable{
    private String suit;
    private String rank;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

// Player class representing a player in the game
class Player implements Serializable {
    private String name;
    private List<Card> hand;
    private Card playedCard;

    public Card getPlayedCard() {
        return playedCard;
    }

    public void setPlayedCard(Card card) {
        playedCard = card;
    }

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public void removeFromHand(int index) {
        hand.remove(index);
    }

    public void removeCardFromHand(Card card) {
        hand.remove(card);
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public Card playCard(int index) {
        return hand.remove(index);
    }

    public List<Card> getHand() {
        return hand;
    }

    @Override
    public String toString() {
        return name;
    }
}

// GoBoomGame class representing the game itself
class GoBoomGame implements Serializable {
    private List<Card> deck;
    private List<Player> players;
    private Card leadCard;
    private int currentPlayerIndex;
    private Map<Player, Integer> scores;
    private boolean gameRunning = true; // Flag to track the game state

    private void calculateScores() {
        for (Player player : players) {
            int score = 0;
            List<Card> hand = player.getHand();
            for (Card card : hand) {
                String rank = card.getRank();
                if (rank.equals("A")) {
                    score += 1;
                } else if (rank.equals("K") || rank.equals("Q") || rank.equals("J")) {
                    score += 10;
                } else {
                    int faceValue = Integer.parseInt(rank);
                    score += faceValue;
                }
            }
            scores.put(player, score);
        }
    }

    public GoBoomGame() {
        initializeDeck();
        initializePlayers();
        shuffleDeck();
        dealCards();
        determineLeadPlayer();
        this.scores = new HashMap<>(); // Initialize the scores variable
        this.gameRunning = true;

        currentPlayerIndex = 0;
        for (Player player : players) {
            this.scores.put(player, 0); // Initialize each player's score to 0
        }

    }

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

    private void initializePlayers() {
        players = new ArrayList<>();
        players.add(new Player("Player 1"));
        players.add(new Player("Player 2"));
        players.add(new Player("Player 3"));
        players.add(new Player("Player 4"));
    }

    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    private void dealCards() {
        for (int i = 0; i < 7; i++) {
            for (Player player : players) {
                Card card = deck.remove(0);
                player.addCardToHand(card);
            }
        }
    }

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

    private void playRound() {
        Player currentPlayer = players.get(currentPlayerIndex);
    
        System.out.println("\nLead Card: " + leadCard + "\n");

        calculateScores();
    
        System.out.println(currentPlayer + "'s turn.");
            int score1 = scores.get(currentPlayer);
            System.out.println("Score: \033[31m" + score1 + "\033[0m"); // Display score in red
        
    
        int cardIndex = getCardIndexToPlay(currentPlayer);
        Card playedCard = currentPlayer.playCard(cardIndex);
    
        System.out.println(currentPlayer + " played " + playedCard);
    
        // Update the score based on the played card's rank
        int score = scores.get(currentPlayer);
        if (playedCard.getRank().equals("A")) {
            score += 1;
        } else if (playedCard.getRank().equals("K") || playedCard.getRank().equals("Q") || playedCard.getRank().equals("J")) {
            score += 10;
        } else {
            score += Integer.parseInt(playedCard.getRank());
        }
        scores.put(currentPlayer, score);
    
        // Store the played card for the player
        currentPlayer.setPlayedCard(playedCard);
    
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
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size(); // Move to the next player
            }
        } else {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size(); // Move to the next player
        }
    
        if (currentPlayerIndex == 0) {
            // Determine the winner of the trick
            Player trickWinner = determineTrickWinner();
            System.out.println("\n--- Trick Winner ---");
            System.out.println(trickWinner + " wins the trick!");
    
            // Select the new lead card
            leadCard = selectNewLeadCard(trickWinner);
    
            currentPlayerIndex = players.indexOf(trickWinner);
        }
    
        // Check if the current player has no cards left
        if (currentPlayer.getHand().isEmpty()) {
            System.out.println("\n" + currentPlayer + " has no cards left.");
            System.out.println("\n--- Game Over ---");
            System.out.println(currentPlayer + " wins the round!");
    
            // Set the score of the winning player to 0
            scores.put(currentPlayer, 0);
    
            // Display final scores
            System.out.println("\n--- Final Scores ---");
            for (Player player : players) {
                System.out.println(player + "'s score: " + scores.get(player));
            }
    
            // Determine the winner based on the lowest score
            int lowestScore = Integer.MAX_VALUE;
            Player winner = null;
            for (Player player : players) {
                int playerScore = scores.get(player);
                if (playerScore < lowestScore) {
                    lowestScore = playerScore;
                    winner = player;
                }
            }
            System.out.println("\n--- Winner ---");
            System.out.println(winner + " wins the game!");
        }
    }
    
    
    private Player determineTrickWinner() {
        Player trickWinner = null;
        int highestValue = -1;
    
        for (Player player : players) {
            Card playedCard = player.getPlayedCard();
            if (playedCard != null) {
                int value = getCardValue(playedCard);
                if (value > highestValue) {
                    highestValue = value;
                    trickWinner = player;
                }
            }
        }
    
        return trickWinner;
    }
    
    private int getCardValue(Card card) {
        String rank = card.getRank();
        if (rank.equals("A")) {
            return 14;
        } else if (rank.equals("K")) {
            return 13;
        } else if (rank.equals("Q")) {
            return 12;
        } else if (rank.equals("J")) {
            return 11;
        } else {
            return Integer.parseInt(rank);
        }
    }
    
    private Card selectNewLeadCard(Player trickWinner) {
        List<Card> hand = trickWinner.getHand();
    
        System.out.println(trickWinner + ", select a new lead card from your hand:");
    
        for (int i = 0; i < hand.size(); i++) {
            System.out.println((i + 1) + ". " + hand.get(i));
        }
    
        Scanner scanner = new Scanner(System.in);
        int selection = scanner.nextInt();
    
        if (selection >= 1 && selection <= hand.size()) {
            return hand.remove(selection - 1);
        } else {
            System.out.println("Invalid selection! Choosing the first card by default.");
            return hand.get(0);
        }

    }


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
                System.out.print("Enter card index: ");
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
                    scanner.next();
                }
            }

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
                            System.out.println("Invalid choice! The chosen card must match the suit or rank of the lead card.");
                        }
                    } else {
                        System.out.println("Invalid card index! Please enter a valid index.");
                    }
                } else {
                    System.out.println("Invalid input! Please enter a valid card index.");
                    scanner.next();
                }
            }

            return cardIndex - 1;
        }
    }

    // Save the game state to a file
    private void saveGame() {
        try (FileOutputStream fileOut = new FileOutputStream("game_state.txt");
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(this);
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving the game: " + e.getMessage());
        }
    }

    public void resetGame() {
        initializeDeck();
        initializePlayers();
        shuffleDeck();
        dealCards();
        determineLeadPlayer();
        scores.clear(); // Clear the scores

        currentPlayerIndex = 0;
        for (Player player : players) {
            scores.put(player, 0); // Initialize each player's score
        }

        gameRunning = true;
    }

    public void playGame() {
        int round = 1;
        int playerCount = players.size();
    
        while (gameRunning) {
            System.out.println("\n----- Round " + round + " -----\n");
    
            Player currentPlayer = players.get(currentPlayerIndex); // Get the current player
    
            for (int i = 0; i < playerCount; i++) {
                playRound();
            }

            calculateScores(); // Calculate scores at the end of each round
    
            System.out.println("\n--- Options ---");
            System.out.println("1. Continue to the next round");
            System.out.println("2. Save and quit the game");
            System.out.println("3. Reset game");
    
            Scanner scanner = new Scanner(System.in);
            int option = 0;
            while (option != 1 && option != 2 && option != 3) {
                System.out.print("Choose an option: ");
                if (scanner.hasNextInt()) {
                    option = scanner.nextInt();
                    if (option != 1 && option != 2 && option != 3) {
                        System.out.println("Invalid option! Please choose 1 ,2 or 3");
                    }
                } else {
                    System.out.println("Invalid input! Please enter a valid option number.");
                    scanner.next(); // Consume invalid input
                }
            }
    
            if (option == 2) {
                // Save the game and quit
                saveGame();
                return;

            } else if (option == 3) {
                // Reset the game
                resetGame();
                round = 1; // Start from the first round

            } else {

                round++; // Proceed to the next round

                for (int i = 0; i <= players.size(); i++){

                // if (currentPlayerIndex == players.size() - 1) { //skips to the next player
                //     currentPlayerIndex++;
                // }
                // else {
                //     currentPlayerIndex = 0;
                // }
                }

                
            
            }
        }
    }

}
    public class goboom {
    public static void main(String[] args) {
        GoBoomGame game = new GoBoomGame();
        game.playGame();
    }
}