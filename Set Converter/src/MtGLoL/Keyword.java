package MtGLoL;

public class Keyword {

	public enum Mode {
		core,
		expert,
		pseudo,
		action;
	}

	private String	name;
	private String	match;
	private String	reminder;
	private String	rules;
	private Mode	mode;

	private Keyword() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public String getReminder() {
		return reminder;
	}

	public void setReminder(String reminder) {
		this.reminder = reminder;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public String format() {
		StringBuilder sb = new StringBuilder();

		sb.append("keyword:\r\n");
		sb.append("	name: " + name + "\r\n");
		sb.append("	match: " + match + "\r\n");
		sb.append("	name: " + name + "\r\n");
		sb.append("	match: " + match + "\r\n");

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((reminder == null) ? 0 : reminder.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Keyword other = (Keyword) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (reminder == null) {
			if (other.reminder != null)
				return false;
		} else if (!reminder.equals(other.reminder))
			return false;
		return true;
	}

	public static class Builder {

		private String	name		= "";
		private String	match		= "";
		private String	reminder	= "";
		private String	rules		= "";
		private Mode	mode		= Mode.core;

		public Keyword Build() {
			Keyword keyword = new Keyword();
			keyword.setName(name);
			keyword.setMatch(match);
			keyword.setReminder(reminder);
			keyword.setRules(rules);
			keyword.setMode(mode);

			return keyword;
		}

		public void name(String name) {
			this.name = name;
		}

		public void match(String match) {
			this.match = match;
		}

		public void reminder(String reminder) {
			this.reminder = reminder;
		}

		public void rules(String rules) {
			this.rules = rules;
		}

		public void mode(Mode mode) {
			this.mode = mode;
		}
	}
}
