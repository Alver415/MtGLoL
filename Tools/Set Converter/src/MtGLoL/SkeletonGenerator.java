package MtGLoL;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.Sets;

import net.lingala.zip4j.ZipFile;

public class SkeletonGenerator {

	private static final String START = "mse version: 0.3.8\r\n" + "game: magic\r\n" + "stylesheet: m15\r\n"
			+ "set info:\r\n" + "	symbol: \r\n" + "	masterpiece symbol: \r\n" + "styling:\r\n" + "	magic-m15:\r\n"
			+ "		text box mana symbols: magic-mana-small.mse-symbol-font\r\n" + "		overlay: ";
	private static final String END = "version control:\r\n" + "	type: none\r\n" + "apprentice code: ";

	private static final double COMMON_RATIO = 0.42;
	private static final double UNCOMMON_RATIO = 0.27;
	private static final double RARE_RATIO = 0.24;
	private static final double MYTHIC_RATIO = 0.07;

	private static final String[] COLORS = new String[] { "", "W", "U", "B", "R", "G", "WU", "UB", "BR", "RG", "GW" };

	public enum Color implements Comparable<Color>{
		COLORLESS("colorless","C"),
		WHITE("white", "W"),
		BLUE("blue", "U"),
		BLACK("black", "B"),
		RED("red", "R"),
		GREEN("green", "G");

		private final String text;
		private final String symbol;

		private Color(String text, String symbol) {
			this.text = text;
			this.symbol = symbol;
		}
	}

	public enum Faction {
		VOID(Color.COLORLESS),
		DEMACIA(Color.WHITE, Color.BLUE),
		FRELJORD(Color.BLUE, Color.BLACK),
		NOXUS(Color.BLACK, Color.RED),
		IXTAL(Color.RED, Color.GREEN),
		IONIA(Color.GREEN, Color.WHITE);

		private final Set<Color> colors;

		private Faction(Color... colors) {
			this.colors = new TreeSet<Color>(Arrays.asList(colors));
		}
	}

	public enum Rarity {
		COMMON("common", 0.42), 
		UNCOMMON("uncommon", 0.27), 
		RARE("rare", 0.24), 
		MYTHIC("mythic", 0.07);

		private final String text;
		private final double ratio;

		private Rarity(String text, double ratio) {
			this.text = text;
			this.ratio = ratio;
		}
	}
	
	public enum SuperType {
		CREATURE("Creature", 0.5), 
		SPELL("Spell", 0.5),;

		private final String text;
		private final double ratio;

		private SuperType(String text, double ratio) {
			this.text = text;
			this.ratio = ratio;
		}
	}

	public enum CardField {
		NAME("\tname: "),
		NOTES("\tnotes: "),
		COST("\tcasting cost: "),
		SUPER_TYPE("\tsuper type: "),
		POWER("\tpower: "),
		TOUGHNESS("\ttoughness: "),
		RARITY("\trarity: ");

		private final String text;

		private CardField(String text) {
			this.text = text;
		}
	}

	public static void main(String... strings) throws IOException {
		Set<Card> cards = generateSkeletonSet();
		writeToMSE(cards);
	}

	private static Set<Card> generateSkeletonSet() {
		Set<Card> cards = new HashSet<>();
		int countTotal = 300;
		int countPerFaction = countTotal / Faction.values().length;
		int count = 0;
		for (Faction faction : Faction.values()) {
			Set<Set<Color>> combinations = new HashSet<>();
			int n = 0;
			while (++n <= faction.colors.size()) {
				combinations.addAll(Sets.combinations(faction.colors, n));
			}
			for (Set<Color> combo : combinations) {
				int countPerCombo = countPerFaction / combinations.size();
				for (Rarity rarity : Rarity.values()) {
					int countPerRarity = (int) Math.max(Math.floor(countPerCombo * rarity.ratio), 1);
					for (int c = 0; c < countPerRarity; c++) {
						SuperType superType = c < countPerRarity / 2 ? SuperType.CREATURE : SuperType.SPELL;
						String setCode = count++ + ": " + faction.name() + " - " +  rarity.name().charAt(0);
						Card card = new Card();
						card.set(CardField.NAME, setCode);
						card.set(CardField.NOTES, setCode);
						card.set(CardField.COST, asCastingCost(combo));
						card.set(CardField.SUPER_TYPE, superType.text);
						card.set(CardField.RARITY, rarity.text);

						cards.add(card);
					}
				}
				
			}

		}
		return cards;
	}

	private static String asCastingCost(Set<Color> combo) {
		StringBuilder sb = new StringBuilder();
		for (Color color : combo) {
			sb.append(color.symbol);
		}
		return sb.toString();
	}


	private static void writeToMSE(Set<Card> cards) throws IOException {
		StringBuilder sb = new StringBuilder();

		sb.append(START);
		for (Card card : cards) {
			writeCard(sb, card);
		}
		sb.append("\n" + END);

		// Write the set file
		FileWriter writer = new FileWriter("Skeleton/set", StandardCharsets.UTF_8);
		writer.write(sb.toString());
		writer.close();

		ZipFile outputZip = new ZipFile(new File("Skeleton.mse-set"));
		// Zip everything from output directory into output.mse-set
		outputZip.addFile("Skeleton/set");
	}

	private static void writeCard(StringBuilder sb, Card card) {
		sb.append("\n").append("card: ");
		for (CardField field : CardField.values()) {
			if (card.fields.containsKey(field)) {
				writeField(sb, field, card.get(field));
			}
		}
	}

	private static void writeField(StringBuilder sb, CardField name, String string) {
		sb.append("\n").append(name.text).append(string);
	}

	private static class Card {
		private final Map<CardField, String> fields = new HashMap<>();

		public void set(CardField field, String value) {
			this.fields.put(field, value);
		}

		public String get(CardField field) {
			return this.fields.get(field);
		}
	}
}
