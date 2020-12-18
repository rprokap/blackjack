import java.util.ArrayList;
import java.util.List;

public class Player {
    private final List<Card> hand;
    private int chips;
    private int initChips;
    private int aceDeduction;
    private boolean bust;

    public Player() {
        this.hand = new ArrayList<>();
        this.chips = 0;
        this.aceDeduction = 0;
        this.bust = false;
    }

    public List<Card> getHand() {
        return hand;
    }
    public int chipBalance() {
        return chips;
    }
    public int aceIsOne() {
        return aceDeduction;
    }
    public boolean isBust() {
        return bust;
    }
    public void dealCard(Card card) {
        hand.add(card);
    }
    public void clearHand() {
        while (hand.size() > 0) {
            hand.remove(0);
        }
    }

    public int addCards() {
        int total = 0;

        for (int i = 0; i < hand.size(); i++) {
            total += Card.getCardValue(hand.get(i));
        }

        return total;
    }

    public void checkIfAce(Card card) {
        if (card.getRank().equals("A")) {
            aceDeduction += 10;
        }
    }

    public void aceDeductionZero() {
        aceDeduction = 0;
    }
    public void addChips(int c) {
        chips += c;
    }
    public void initialChips(int c) {
        initChips = c;
    }
    public void isBust(boolean b) {
        bust = b;
    }
}
