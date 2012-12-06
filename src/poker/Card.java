/*
 * Copyright 2012 Cameron Zemek <grom358@gmail.com>.
 */
package poker;

import java.util.ArrayList;
import java.util.List;

/**
 * A poker card. Aces are high and suit is not important in ranking.
 *
 * @author Cameron Zemek <grom358@gmail.com>
 */
public final class Card implements Comparable<Card> {
    static public enum Suit {
        SPADE,
        HEART,
        DIAMOND,
        CLUB;

        static public final int size = Suit.values().length;

        private final char letter;

        Suit() {
            this.letter = Character.toLowerCase(this.name().charAt(0));
        }

        public char getLetter() {
            return this.letter;
        }

        static public Suit valueOf(char letter) {
            for (Suit s : Suit.values()) {
                if (s.letter == letter) {
                    return s;
                }
            }
            return null;
        }
    }

    static public enum Rank {
        ACE('A', 14),
        KING('K', 13),
        QUEEN('Q', 12),
        JACK('J', 11),
        TEN('T', 10),
        NINE('9', 9),
        EIGHT('8', 8),
        SEVEN('7', 7),
        SIX('6', 6),
        FIVE('5', 5),
        FOUR('4', 4),
        THREE('3', 3),
        DUECE('2', 2);

        static public final int size = Rank.values().length;

        private final char letter;
        private final int value;

        Rank(char letter, int value) {
            this.letter = letter;
            this.value = value;
        }

        public char getLetter() {
            return this.letter;
        }

        public int getValue() {
            return this.value;
        }

        static public Rank valueOf(char letter) {
            for (Rank r : Rank.values()) {
                if (r.letter == letter) {
                    return r;
                }
            }
            return null;
        }

        static public Rank valueOf(int value) {
            for (Rank r : Rank.values()) {
                if (r.value == value) {
                    return r;
                }
            }
            return null;
        }
    }

    private final Suit suit;
    private final Rank rank;

    private Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public int rankValue() {
        return rank.getValue();
    }

    /**
     * Return number between 0 and 51 (inclusive), used as index value
     */
    public int intValue() {
        return rank.ordinal() * Suit.size + suit.ordinal();
    }

    /**
     * Return a number that contains 1 bit, used as bitset value
     */
    public long longValue() {
        return 1l << intValue();
    }

    @Override
    public int compareTo(Card o) {
        return this.intValue() - o.intValue();
    }

    @Override
    public String toString() {
        return "" + rank.getLetter() + suit.getLetter();
    }

    static private final List<Card> protoDeck = new ArrayList<>();

    // Initialize prototype deck
    static {
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                protoDeck.add(new Card(rank, suit));
            }
        }
    }

    static public Card valueOf(int index) {
        if (index < 0 || index > protoDeck.size()) {
            throw new IllegalArgumentException("Invalid card");
        }
        return protoDeck.get(index);
    }

    static public Card valueOf(long num) {
        int index = Long.numberOfLeadingZeros(num);
        return valueOf(index);
    }

    static public Card valueOf(Rank rank, Suit suit) {
        int index = (new Card(rank, suit)).intValue();
        return valueOf(index);
    }

    static public Card valueOf(String card) {
        if (card.length() != 2) {
            throw new IllegalArgumentException("Invalid card format");
        }
        char rank = card.charAt(0);
        char suit = card.charAt(1);
        return valueOf(Rank.valueOf(rank), Suit.valueOf(suit));
    }

    static public List<Card> newDeck() {
        return new ArrayList<>(protoDeck); // Return copy of prototype deck
    }
}
