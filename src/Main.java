import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final String[] RANKS = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K" };

    private final Player player;
    private final Player dealer;
    private List<Card> deck;
    private final Scanner in;

    public Main() {
        this.player = new Player();
        this.dealer = new Player();
        this.in = new Scanner(System.in);
    }

    public void play() {
        shuffle();

        int playerChipPurchase = 0;
        do {
            System.out.println("\nyour chip total is: " + player.chipBalance() + " chip(s). enter a number between (inclusive) 1 and 32767 to obtain chips.");
            try {
                playerChipPurchase = in.nextShort();
            }
            catch (Exception e) {
                playerChipPurchase = 0;
                in.next();
            }
        } while (playerChipPurchase <= 0);
        in.nextLine();
        player.initialChips(player.chipBalance() + playerChipPurchase);
        player.addChips(playerChipPurchase);

        while (deck.size() >= 4 && player.chipBalance() > 0) {
            for (int i = 0; i < 2; i++) {
                player.dealCard(deck.get(0));
                deck.remove(0);
                dealer.dealCard(deck.get(0));
                deck.remove(0);
            }

            int wager = 0;
            do {
                System.out.println("\nyour chip total is:  " + player.chipBalance() + " chip(s). enter a number between (inclusive) 1 and 25 to place a wager.");
                try {
                    wager = in.nextInt();
                }
                catch (Exception e) {
                    wager = 0;
                    in.next();
                }
                if (wager > player.chipBalance()) {
                    System.out.println("you can't wager more than your balance.");
                }
            } while (wager <= 0 || wager > player.chipBalance() || wager > 25);
            in.nextLine();

            System.out.print("\nplayer hand: [");
            for (int i = 0; i < player.getHand().size() - 1; i++) {
                System.out.print(player.getHand().get(i).getRank() + ", ");
            }
            System.out.println(player.getHand().get(player.getHand().size() - 1).getRank() + "]");

            System.out.print("dealer hand: [?, ");
            for (int i = 1; i < dealer.getHand().size() - 1; i++) {
                System.out.print(dealer.getHand().get(i).getRank() + ", ");
            }
            System.out.println(dealer.getHand().get(dealer.getHand().size() - 1).getRank() + "]");

            if (player.addCards() == 21) {
                System.out.println("\nblackjack! your wager was " + wager + " chip(s), so you win " + wager * 1.5 + " chip(s)!");
                player.addChips((int) (1.5 * wager));
            }
            else {
                takeTurn(false);

                if (!player.isBust()) {
                    System.out.print("\ndealer hand: [");
                    for (int i = 0; i < dealer.getHand().size() - 1; i++) {
                        System.out.print(dealer.getHand().get(i).getRank() + ", ");
                    }
                    System.out.println(dealer.getHand().get(dealer.getHand().size() - 1).getRank() + "]");

                    takeTurn(true);
                }

                if (!player.isBust() && !dealer.isBust()) {
                    System.out.println("\nplayer total: " + (player.addCards() - player.aceIsOne()));
                    System.out.println("versus");
                    System.out.println("dealer total: " + (dealer.addCards() - dealer.aceIsOne()));
                }

                if (player.isBust()) {
                    System.out.println("\nbust! you lose your wager of " + wager + " chip(s).");
                    player.addChips(-1 * wager);
                } else if (dealer.isBust()) {
                    System.out.println("\ndealer bust! you win " + wager + " chip(s).");
                    player.addChips(wager);
                } else if (player.addCards() - player.aceIsOne() > dealer.addCards() - dealer.aceIsOne()) {
                    System.out.println("\nplayer win! you win " + wager + " chip(s).");
                    player.addChips(wager);
                } else if (player.addCards() - player.aceIsOne() < dealer.addCards() - dealer.aceIsOne()) {
                    System.out.println("\nyou lose your wager of " + wager + " chip(s).");
                    player.addChips(-1 * wager);
                } else if (player.addCards() - player.aceIsOne() == dealer.addCards() - dealer.aceIsOne()) {
                    System.out.println("\ntie!");
                }
            }

            player.isBust(false);
            dealer.isBust(false);
            player.aceDeductionZero();
            dealer.aceDeductionZero();
            player.clearHand();
            dealer.clearHand();
            System.out.println("\nnext hand: ");
        }

        endGame();
    }

    public void takeTurn(boolean cpu) {
        if (!cpu) {
            player.aceDeductionZero();
            if (player.addCards() > 21) {
                for (int i = 0; i < player.getHand().size(); i++) {
                    player.checkIfAce(player.getHand().get(i));
                    if (player.addCards() - player.aceIsOne() < 21) {
                        break;
                    }
                }
            }
            System.out.println("player total: " + (player.addCards() - player.aceIsOne()));

            if (player.addCards() - player.aceIsOne() == 21) {
                System.out.println("\nplayer total is equal to 21. (stand)"); 
                return;
            } else if (player.addCards() - player.aceIsOne() > 21) {
                player.isBust(true);
                System.out.println("\nplayer bust."); 
                return;
            } else if (deck.size() == 0) {
                System.out.println("\ndeck is empty. (stand)");
                return;
            }

            String playerMove = "";
            do {
                System.out.println("\nplayer move: ");
                playerMove = in.nextLine().toLowerCase();
            } while (!playerMove.equals("hit") && !playerMove.equals("stand"));

            if (playerMove.equals("hit")) {
                player.dealCard(deck.get(0));
                deck.remove(0);

                System.out.print("\nplayer hand: [");
                for (int i = 0; i < player.getHand().size() - 1; i++) {
                    System.out.print(player.getHand().get(i).getRank() + ", ");
                }
                System.out.println(player.getHand().get(player.getHand().size() - 1).getRank() + "]");

                takeTurn(false);
            }
        }
        else {
            dealer.aceDeductionZero();
            if (dealer.addCards() > 21) {
                for (int i = 0; i < dealer.getHand().size(); i++) {
                    dealer.checkIfAce(dealer.getHand().get(i));
                    if (dealer.addCards() - dealer.aceIsOne() < 21) {
                        break;
                    }
                }
            }
            System.out.println("dealer total: " + (dealer.addCards() - dealer.aceIsOne()));

            if (dealer.addCards() - dealer.aceIsOne() >= 17 && dealer.addCards() - dealer.aceIsOne() <= 21) {
                System.out.println("\ndealer stands.");
            } else if (dealer.addCards() - dealer.aceIsOne() > 21) {
                dealer.isBust(true);
                System.out.println("\ndealer bust."); 
            } else if (deck.size() == 0) {
                System.out.println("\nthe deck is empty. dealer stands."); 
            } else if (dealer.addCards() - dealer.aceIsOne() <= 16) {
                System.out.println("\ndealer hits.");
                dealer.dealCard(deck.get(0));
                deck.remove(0);

                System.out.print("\ndealer hand: [");
                for (int i = 0; i < dealer.getHand().size() - 1; i++) {
                    System.out.print(dealer.getHand().get(i).getRank() + ", ");
                }

                System.out.println(dealer.getHand().get(dealer.getHand().size() - 1).getRank() + "]");

                takeTurn(true);
            }
        }
    }

    public void shuffle() {
        deck = new ArrayList<>(52);
        for (String rank : RANKS) {
            for (int i = 0; i < 4; i++) {
                deck.add(new Card(rank));
            }
        }

        Collections.shuffle(deck);
    }

    public void endGame() {
        String endMessage = (player.chipBalance() == 0) ? "\nchip balance: 0.\ngame over!" : "\nthe deck is empty.\ngame over!";
        System.out.println(endMessage);

        player.clearHand();
        dealer.clearHand();

        String newRound = "";
        do {
            System.out.println("\nplay again? (yes or no?)");
            newRound = in.nextLine().toLowerCase();
        } while (!newRound.equals("yes") && !newRound.equals("no"));
        if (newRound.equals("yes")) {
            play();
        }
        else {
            in.close();
        }
    }

    public static void main(String[] args) {
        System.out.println("welcome 2 blackjack\nwhen prompted to take your turn, choose to either 'hit' or 'stay'.");
        new Main().play();
    }
}
