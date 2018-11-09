package MtGLoL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TextParser {

	private final static String CURRENT_TIME = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS")
			.format(Calendar.getInstance().getTime());

	private String location_txt = "C:/projects/git/MtGLoL/Misc/Cycles.txt";
	private String location_set = "C:/projects/git/MtGLoL/Misc/Test";

	private static final String TOP = "mse version: 0.3.8\r\n" + "game: magic\r\n" + "stylesheet: new\r\n"
			+ "set info:\r\n" + "	title: League of Legends\r\n" + "	symbol: symbol2.mse-symbol\r\n"
			+ "	automatic reminder text: \r\n" + "styling:\r\n" + "	magic-new:\r\n"
			+ "		text box mana symbols: magic-mana-small.mse-symbol-font\r\n" + "		overlay: \r\n"
			+ "	magic-new-flip:\r\n" + "		text box mana symbols: magic-mana-small.mse-symbol-font\r\n"
			+ "		overlay: \r\n";

	private static final String BOTTOM = "version control:\r\n	type: none\r\napprentice code: ";

	public static void main(String[] args) throws IOException {

		if (args.length != 2) {
			throw new IllegalArgumentException("Input must be location of text file and location of set file");
		}

		String location_txt = args[0];// Example: "C:/projects/git/MtGLoL/Misc/Cycles.txt";
		String location_set = args[1];// Example: "C:/projects/git/MtGLoL/Misc/Test";

		new TextParser(location_txt, location_set);
	}

	public TextParser(String location_txt, String location_set) throws IOException {
		this.location_txt = location_txt;
		this.location_set = location_set;

		String input = fileToString();
		String converted = convertAll(input);
		stringToFile(converted);
	}

	private void stringToFile(String converted) throws FileNotFoundException {
		try (PrintWriter out = new PrintWriter(location_set)) {
			out.print(converted);
		}
	}

	private String fileToString() throws IOException {
		String line = null;
		StringBuilder sb = new StringBuilder();
		String ls = System.getProperty("line.separator");

		try (BufferedReader reader = new BufferedReader(new FileReader(new File(location_txt)))) {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append(ls);
			}
			return sb.toString();
		}
	}

	private String convertAll(String input) {
		StringBuilder sb = new StringBuilder();

		sb.append(TOP);

		String section = "";
		String[] lines = input.split("\r\n");
		for (String line : lines) {
			try {
				if (Character.isDigit(line.trim().charAt(0))) {
					section = line;
				}
				if (location_txt.endsWith("Cycles.txt")) {
					sb.append(convertLineCycles(section, line));
				} else if (location_txt.endsWith("Spells.txt")) {
					sb.append(convertLineSpells(line));
				}
			} catch (Throwable t) {
				System.out.println("Error converting line:\t" + line);
				System.out.println("\tcaused by: " + t.getMessage());
			}
		}

		sb.append(BOTTOM);

		return sb.toString();
	}

	private String convertLineCycles(String section, String line) throws Exception {

		String[] split = line.trim().split("\\s+");

		Faction faction = Faction.parse(split[0]);
		int cmc = Character.getNumericValue(section.charAt(0));
		String cost = (cmc <= 2 ? "" : cmc - 2) + faction.getColors();

		String[] stats = split[1].split("/");
		String power = stats[0];
		String toughness = stats[1];

		String type = "Creature";

		split[0] = "";
		split[1] = "";
		String text = String.join(" ", split).substring(2);

		Card card = new Card.Builder().timeCreated(CURRENT_TIME).timeModified(CURRENT_TIME).castingCost(cost)
				.superType(type).power(power).toughness(toughness).ruleText(text).build();

		return card.format();
	}

	private String convertLineSpells(String line) {

		String[] split = line.trim().split("\\s+");
		String cost = split[0].replace("{", "").replace("}", "");
		String type = split[1];
		split[0] = "";
		split[1] = "";
		String text = String.join(" ", split).substring(2);

		Card card = new Card.Builder().timeCreated(CURRENT_TIME).timeModified(CURRENT_TIME).castingCost(cost)
				.superType(type).ruleText(text).build();

		return card.format();
	}
}
