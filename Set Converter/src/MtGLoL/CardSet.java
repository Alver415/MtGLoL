package MtGLoL;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class CardSet {

	private String			name;
	private Set<Card>		cards		= new TreeSet<Card>();
	private Set<Keyword>	keywords	= new TreeSet<Keyword>();

	public CardSet(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Set<Card> getCards() {
		return new HashSet<Card>(cards);
	}

	public void addCard(Card card) {
		this.cards.add(card);
	}

	public Set<Keyword> getKeywords() {
		return new HashSet<Keyword>(keywords);
	}

	public void addKeyword(Keyword keyword) {
		this.keywords.add(keyword);
	}

	public static CardSet consolitate(String name, Set<CardSet> cardSets) {
		CardSet newSet = new CardSet(name);
		for (CardSet set : cardSets) {
			for (Card card : set.getCards()) {
				newSet.addCard(card);
			}
			for (Keyword keyword : set.getKeywords()) {
				newSet.addKeyword(keyword);
			}
		}
		return newSet;
	}
}
