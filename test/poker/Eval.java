/*
 * Copyright 2012 Cameron Zemek <grom358@gmail.com>.
 */
package poker;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author grom
 */
public class Eval {

    public Eval() {
    }

    private void assertCategory(Hand.Category category, String cards) {
        CardList cardList = CardList.valueOf(cards);
        CardSet cs = new CardSet(cardList);
        Hand hand = Hand.eval(cs);
        assertEquals(cards, category, hand.getCategory());
    }

    @Test
    public void straightFlush() {
        // Royal flush
        assertCategory(Hand.Category.STRAIGHT_FLUSH, "[Ah,Kh,Qh,Jh,Th]");
        // Ace low straight flush
        assertCategory(Hand.Category.STRAIGHT_FLUSH, "[Ah,2h,3h,4h,5h]");
        // Straight flush
        assertCategory(Hand.Category.STRAIGHT_FLUSH, "[2h,3h,4h,5h,6h]");
        assertCategory(Hand.Category.STRAIGHT_FLUSH, "[4h,5h,6h,7h,8h]");
        assertCategory(Hand.Category.STRAIGHT_FLUSH, "[8h,9h,Th,Jh,Qh]");
        assertCategory(Hand.Category.STRAIGHT_FLUSH, "[8d,9d,Td,Jd,Qd]");
        assertCategory(Hand.Category.STRAIGHT_FLUSH, "[8c,9c,Tc,Jc,Qc]");
        assertCategory(Hand.Category.STRAIGHT_FLUSH, "[8s,9s,Ts,Js,Qs]");
    }

    @Test
    public void fourOfAKind() {
        assertCategory(Hand.Category.FOUR_OF_A_KIND, "[Ac,Ad,Ah,As,3c]");
    }

    @Test
    public void fullHouse() {
        assertCategory(Hand.Category.FULLHOUSE, "[Ac,Ad,Ah,Ks,Kd]");
    }

    @Test
    public void flush() {
        assertCategory(Hand.Category.FLUSH, "[Ac,Jc,Tc,8c,2c]");
    }

    @Test
    public void straight() {
        // Ace high
        assertCategory(Hand.Category.STRAIGHT, "[Ah,Kd,Qh,Js,Th]");
        // Ace low straight
        assertCategory(Hand.Category.STRAIGHT, "[Ah,2h,3d,4h,5s]");
        // Straight
        assertCategory(Hand.Category.STRAIGHT, "[2h,3d,4h,5s,6h]");
        assertCategory(Hand.Category.STRAIGHT, "[4d,5h,6s,7h,8c]");
        assertCategory(Hand.Category.STRAIGHT, "[8h,9c,Th,Js,Qh]");
    }

    @Test
    public void threeOfAKind() {
        assertCategory(Hand.Category.THREE_OF_A_KIND, "[3c,3d,3h,9c,5h]");
    }

    @Test
    public void twoPair() {
        assertCategory(Hand.Category.TWO_PAIR, "[2c,2d,6d,6s,4h]");
    }

    @Test
    public void pair() {
        assertCategory(Hand.Category.PAIR, "[9h,9s,8d,2c,4s]");
    }

    @Test
    public void highCard() {
        assertCategory(Hand.Category.HIGH_CARD, "[Ac,Jd,8c,7c,3s]");
    }
}
