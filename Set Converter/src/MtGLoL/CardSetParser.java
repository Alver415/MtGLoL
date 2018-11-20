package MtGLoL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CardSetParser {

	public static CardSet fileToSet(String location) throws IOException {
		return parseSet(fileToString(location));
	}

	private static String fileToString(String location) throws IOException {
		String line = null;
		StringBuilder sb = new StringBuilder();
		String ls = System.getProperty("line.separator");

		try (BufferedReader reader = new BufferedReader(new FileReader(new File(location)))) {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append(ls);
			}
			return sb.toString();
		}
	}

	private static CardSet parseSet(String string) {
		String[] cardString = string.split("card:");
		CardSet set = new CardSet("League of Legends");

		for (int i = 0; i < cardString.length; i++) {
			if ("card:".equals(cardString[i])) {

				Card.Builder builder = new Card.Builder();
				builder.hasStyleing(cardString[++i]);
				builder.notes(cardString[++i]);
				builder.timeCreated(cardString[++i]);
				builder.timeModified(cardString[++i]);
				builder.name(cardString[++i]);
				builder.castingCost(cardString[++i]);
				builder.image(cardString[++i]);
				builder.superType(cardString[++i]);
				builder.subType1(cardString[++i]);
				builder.subType2(cardString[++i]);
				builder.ruleText(cardString[++i]);
				builder.flavorText(cardString[++i]);
				builder.power(cardString[++i]);
				builder.toughness(cardString[++i]);
				builder.copyright(cardString[++i]);
				builder.image2(cardString[++i]);
				builder.copyright(cardString[++i]);

				Card card = builder.build();
				set.addCard(card);
			}

			if ("keyword:".equals(cardString[i])) {
				Keyword.Builder builder = new Keyword.Builder();
				builder.name(cardString[++i]);
				builder.match(cardString[++i]);
				builder.reminder(cardString[++i]);
				builder.rules(cardString[++i]);
				builder.match(cardString[++i]);

				Keyword keyword = builder.Build();
				set.addKeyword(keyword);
			}
		}

		return set;

	}
}
