public class Card {
    private final String rank;

    public Card(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }

    public static int getCardValue(Card card) {
        String rank = card.getRank();
        try {
            return Integer.parseInt(rank);
        } catch (NumberFormatException e) {
            switch (rank) {
                case "T": case "J": case "Q": case "K": return 10;
                case "A": return 11;
            }
        }

        return -1;
    }
}
