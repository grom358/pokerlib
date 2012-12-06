/*
 * Copyright 2012 Cameron Zemek <grom358@gmail.com>.
 */
package poker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A list of poker cards
 *
 * @author Cameron Zemek <grom358@gmail.com>
 */
public class CardList extends ArrayList<Card> {
    public CardList() {
        super();
    }

    public CardList(int initialCapacity) {
        super(initialCapacity);
    }

    public CardList(Collection<Card> cards) {
        super(cards);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Iterator it = this.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
        }
        while (it.hasNext()) {
            sb.append(',');
            sb.append(it.next());
        }
        sb.append(']');
        return sb.toString();
    }

    static final private Pattern listPattern = Pattern.compile("[0-9TJQKA][cdhs]");

    static public CardList valueOf(String str) {
        Matcher matcher = listPattern.matcher(str);
        CardList cardList = new CardList();
        while (matcher.find()) {
            Card card = Card.valueOf(matcher.group());
            cardList.add(card);
        }
        return cardList;
    }
}
