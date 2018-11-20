package MtGLoL;

import java.util.Set;

public class Formatter {

	private static final String TOP = "mse version: 0.3.8\r\n" + "game: magic\r\n" + "stylesheet: new\r\n"
			+ "set info:\r\n" + "	title: League of Legends\r\n" + "	symbol: symbol2.mse-symbol\r\n"
			+ "	automatic reminder text: \r\n" + "styling:\r\n" + "	magic-new:\r\n"
			+ "		text box mana symbols: magic-mana-small.mse-symbol-font\r\n" + "		overlay: \r\n"
			+ "	magic-new-flip:\r\n" + "		text box mana symbols: magic-mana-small.mse-symbol-font\r\n"
			+ "		overlay: \r\n";

	private static final String BOTTOM = "version control:\r\n	type: none\r\napprentice code: ";

	public static String format(Card card) {
		StringBuilder sb = new StringBuilder();

		sb.append("card:\r\n");
		sb.append("	has styling: " + card.getHasStyleing() + "\r\n");
		sb.append("	notes: " + card.getNotes() + "\r\n");
		sb.append("	time created: " + card.getTimeCreated() + "\r\n");
		sb.append("	time modified: " + card.getTimeModified() + "\r\n");
		sb.append("	name: " + card.getName() + "\r\n");
		sb.append("	casting cost: " + card.getCastingCost() + "\r\n");
		sb.append("	image: " + card.getImage() + "\r\n");
		sb.append("	super type: <word-list-type>" + card.getSuperType() + "</word-list-type>\r\n");
		sb.append("	sub type: ");
		sb.append("<word-list-race>" + card.getSubType1() + "</word-list-race>");
		sb.append("<soft> </soft>");
		sb.append("<word-list-class>" + card.getSubType2() + "</word-list-class>\r\n");
		sb.append("	rule text: " + card.getRuleText() + "\r\n");
		sb.append("	flavor text: <i-flavor>" + card.getFlavorText() + "</i-flavor>\r\n");
		sb.append("	power: " + card.getPower() + "\r\n");
		sb.append("	toughness: " + card.getToughness() + "\r\n");
		sb.append("	copyright: " + card.getCopyright() + "\r\n");
		sb.append("	image 2: " + card.getImage2() + "\r\n");
		sb.append("	copyright 2: " + card.getCopyright2() + "\r\n");

		return sb.toString();
	}

	public static String format(Keyword keyword) {
		StringBuilder sb = new StringBuilder();

		sb.append("keyword:\r\n");
		sb.append("	name: " + keyword.getName() + "\r\n");
		sb.append("	name: " + keyword.getMatch() + "\r\n");
		sb.append("	name: " + keyword.getReminder() + "\r\n");
		sb.append("	name: " + keyword.getRules() + "\r\n");
		sb.append("	name: " + keyword.getMode().toString() + "\r\n");

		return sb.toString();
	}

	public static String format(CardSet set) {
		StringBuilder sb = new StringBuilder();
		sb.append(TOP);

		Set<Card> sortedCards = set.getCards();
		for (Card card : sortedCards) {
			sb.append(format(card));
		}
		final Set<Keyword> keywords = set.getKeywords();
		for (Keyword keyword : keywords) {
			sb.append(format(keyword));
		}
		sb.append(BOTTOM);

		return sb.toString();
	}
}
