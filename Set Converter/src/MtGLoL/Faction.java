package MtGLoL;

import java.util.Arrays;
import java.util.List;

public enum Faction {
	DEMACIA("W", "U"),
	FRELJORD("U", "B"),
	NOXUS("B", "R"),
	RUNETERRA_WILD("R", "G"),
	IONIA("G", "W"),
	TARGON("W", "B"),
	SHADOW_ISLES("B", "G"),
	ZAUN("G", "U"),
	PILTOVER("U", "R"),
	SHURIMA("R", "W");

	List<String> colors;

	Faction(String... colors) {
		this.colors = Arrays.asList(colors);
	}

	public String getColors() {
		StringBuilder sb = new StringBuilder();
		for (String color : colors) {
			sb.append(color);
		}
		return sb.toString();
	}

	public static Faction parse(String name) throws Exception {
		for (Faction faction : Faction.values()) {
			if (faction.name().equals(name.toUpperCase())) {
				return faction;
			}
		}
		throw new Exception(name + " is not parseable.");
	}
}