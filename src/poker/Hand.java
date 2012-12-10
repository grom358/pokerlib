/*
 * Copyright 2012 Cameron Zemek <grom358@gmail.com>.
 */
package poker;

import java.util.Collection;

/**
 * A poker hand.
 *
 * @author Cameron Zemek <grom358@gmail.com>
 */
public class Hand implements Comparable<Hand> {
    static public enum Category {
        HIGH_CARD,
        PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        STRAIGHT,
        FLUSH,
        FULLHOUSE,
        FOUR_OF_A_KIND,
        STRAIGHT_FLUSH
    }

    private Category category;
    private CardList cardList;
    private int handValue;

    private Hand(Category category, CardList cardList, int handValue) {
        this.category = category;
        this.cardList = cardList;
        this.handValue = handValue;
    }

    public Category getCategory() {
        return category;
    }

    public CardList getCards() {
        return cardList;
    }

    public int getValue() {
        return handValue;
    }

    @Override
    public int compareTo(Hand o) {
        return o.handValue - handValue;
    }

    static private final long STRAIGHT_FLUSH_MASK = 0x11111L;
    static private final long ACE_LOW_STRAIGHT_FLUSH_MASK = 0x1111000000001L;
    static private final long SUIT_MASK = 0x1111111111111L;
    static private final long RANK_MASK = 0xFL;

    static private Hand handValue(Category category, CardList cardList) {
        int value = category.ordinal() << 1;
        int i = 0;
        int n = Math.min(5, cardList.size());
        for (; i < n; ++i) {
            Card card = cardList.get(i);
            value = (value << 4) | card.rankValue();
        }
        for (; i < 5; ++i) {
            value <<= 4;
        }
        return new Hand(category, cardList, value);
    }

    static private Hand handValue(Category category, CardSet cardSet) {
        return handValue(category, cardSet.toList());
    }

    static public Hand eval(CardSet cs) {
        long val = cs.longValue();
        long test, mask;

        // Ace low straight flush
        for (int i = 0, n = Card.Suit.size; i < n; ++i) {
            mask = ACE_LOW_STRAIGHT_FLUSH_MASK << i;
            test = val & mask;
            if (test == mask) {
                return handValue(Category.STRAIGHT_FLUSH, new CardSet(test));
            }
        }

        // Straight flush
        for (int i = 0, n = (Card.Rank.size * Card.Suit.size) - (4 * Card.Suit.size);
                i < n; ++i) {
            mask = STRAIGHT_FLUSH_MASK << i;
            test = val & mask;
            if (test == mask) {
                return handValue(Category.STRAIGHT_FLUSH, new CardSet(test));
            }
        }

        CardSet threeOfKind = null;
        CardSet topPair = null;
        CardSet secondPair = null;

        // Search for sets
        for (int i = 0, n = Card.Rank.size; i < n; ++i) {
            mask = RANK_MASK << (i * Card.Suit.size);
            test = val & mask;
            if (test == mask) {
                CardSet kickers = new CardSet(val & ~test);
                CardList hand = (new CardSet(test)).toList();
                hand.add(kickers.toList().get(0));
                return handValue(Category.FOUR_OF_A_KIND, hand);
            }
            if (threeOfKind == null || topPair == null || secondPair == null) {
                int kindCount = Long.bitCount(test);
                if (kindCount >= 3 && threeOfKind == null) {
                    threeOfKind = new CardSet(test);
                } else if (kindCount >= 2 && topPair == null) {
                    topPair = new CardSet(test);
                } else if (kindCount >= 2 && secondPair == null) {
                    secondPair = new CardSet(test);
                }
            }
        }

        if (threeOfKind != null && topPair != null) {
            CardList hand = threeOfKind.toList();
            hand.addAll(topPair.toList());
            return handValue(Category.FULLHOUSE, hand);
        }

        // Search for flush
        for (int i = 0, n = Card.Suit.size; i < n; ++i) {
            mask = SUIT_MASK << i;
            test = val & mask;
            int cardCount = Long.bitCount(test);
            if (cardCount >= 5) {
                return handValue(Category.FLUSH, new CardSet(test));
            }
        }

        // Search for straight
        int straightLength = 0;
        long straight = 0;
        for (int i = 0, n = Card.Rank.size; i < n; ++i) {
            mask = RANK_MASK << (i * Card.Suit.size);
            test = val & mask;
            if (test != 0) {
                straightLength++;
                straight |= Long.lowestOneBit(test);
            } else {
                straightLength = 0;
                straight = 0;
            }
            if (straightLength == 5) {
                return handValue(Category.STRAIGHT, new CardSet(straight));
            }
        }
        // Test for ace low straight
        if (straightLength == 4) {
            test = val & RANK_MASK;
            if (test != 0) {
                straight |= Long.lowestOneBit(test);
                return handValue(Category.STRAIGHT, new CardSet(straight));
            }
        }

        if (threeOfKind != null) {
            CardList hand = threeOfKind.toList();
            CardSet ks = new CardSet(cs);
            ks.subtract(threeOfKind);
            hand.addAll(ks.subList(2));
            return handValue(Category.THREE_OF_A_KIND, hand);
        }

        if (topPair != null && secondPair != null) {
            CardList hand = topPair.toList();
            hand.addAll(secondPair.toList());
            CardSet ks = new CardSet(cs);
            ks.subtract(topPair);
            ks.subtract(secondPair);
            hand.addAll(ks.subList(1));
            return handValue(Category.TWO_PAIR, hand);
        }

        if (topPair != null) {
            CardList hand = topPair.toList();
            CardSet ks = new CardSet(cs);
            ks.subtract(topPair);
            hand.addAll(ks.subList(3));
            return handValue(Category.PAIR, hand);
        }

        // High card
        return handValue(Category.HIGH_CARD, cs);
    }

    static public Hand eval(Collection<Card> hand, Collection<Card> board) {
        CardSet cs = new CardSet();
        cs.addAll(hand);
        cs.addAll(board);
        return eval(cs);
    }
}
