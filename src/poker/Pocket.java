/*
 * Copyright 2012 Cameron Zemek <grom358@gmail.com>.
 */
package poker;

/**
 * Pocket cards in Texas Holdem
 *
 * @author Cameron Zemek <grom358@gmail.com>
 */
public class Pocket {
    private Card first;
    private Card second;

    public Pocket(Card first, Card second) {
        // Make first the highest card
        if (first.rankValue() < second.rankValue()) {
            Card tmp = first;
            first = second;
            second = tmp;
        }
        this.first = first;
        this.second = second;
    }

    public Card getFirst() {
        return first;
    }

    public Card getSecond() {
        return second;
    }

    public boolean isPair() {
        return first.getRank() == second.getRank();
    }

    public boolean isSuited() {
        return first.getSuit() == second.getSuit();
    }

    public int getGap() {
        return first.getRank().getValue() - second.getRank().getValue();
    }

    public boolean isConnected() {
        return getGap() == 1;
    }

    public CardList toList() {
        CardList cardList = new CardList();
        cardList.add(first);
        cardList.add(second);
        return cardList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(first.getRank().getLetter());
        sb.append(second.getRank().getLetter());
        if (first.getSuit() == second.getSuit()) {
            sb.append('s');
        }
        return sb.toString();
    }
}
