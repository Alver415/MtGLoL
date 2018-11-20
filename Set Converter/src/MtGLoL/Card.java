package MtGLoL;

public class Card {
	private final String	hasStyleing;
	private final String	notes;
	private final String	timeCreated;
	private final String	timeModified;
	private final String	name;
	private final String	castingCost;
	private final String	image;
	private final String	superType;
	private final String	subType1;
	private final String	subType2;
	private final String	ruleText;
	private final String	flavorText;
	private final String	power;
	private final String	toughness;
	private final String	copyright;
	private final String	image2;
	private final String	copyright2;

	public Card(String hasStyleing, String notes, String timeCreated, String timeModified, String name,
			String castingCost, String image, String superType, String subType1, String subType2, String ruleText,
			String flavorText, String power, String toughness, String copyright, String image2, String copyright2) {
		this.hasStyleing = hasStyleing;
		this.notes = notes;
		this.timeCreated = timeCreated;
		this.timeModified = timeModified;
		this.name = name;
		this.castingCost = castingCost;
		this.image = image;
		this.superType = superType;
		this.subType1 = subType1;
		this.subType2 = subType2;
		this.ruleText = ruleText;
		this.flavorText = flavorText;
		this.power = power;
		this.toughness = toughness;
		this.copyright = copyright;
		this.image2 = image2;
		this.copyright2 = copyright2;
	}

	public String getHasStyleing() {
		return hasStyleing;
	}

	public String getNotes() {
		return notes;
	}

	public String getTimeCreated() {
		return timeCreated;
	}

	public String getTimeModified() {
		return timeModified;
	}

	public String getName() {
		return name;
	}

	public String getCastingCost() {
		return castingCost;
	}

	public String getImage() {
		return image;
	}

	public String getSuperType() {
		return superType;
	}

	public String getSubType1() {
		return subType1;
	}

	public String getSubType2() {
		return subType2;
	}

	public String getRuleText() {
		return ruleText;
	}

	public String getFlavorText() {
		return flavorText;
	}

	public String getPower() {
		return power;
	}

	public String getToughness() {
		return toughness;
	}

	public String getCopyright() {
		return copyright;
	}

	public String getImage2() {
		return image2;
	}

	public String getCopyright2() {
		return copyright2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((castingCost == null) ? 0 : castingCost.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((power == null) ? 0 : power.hashCode());
		result = prime * result + ((ruleText == null) ? 0 : ruleText.hashCode());
		result = prime * result + ((subType1 == null) ? 0 : subType1.hashCode());
		result = prime * result + ((subType2 == null) ? 0 : subType2.hashCode());
		result = prime * result + ((superType == null) ? 0 : superType.hashCode());
		result = prime * result + ((toughness == null) ? 0 : toughness.hashCode());
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
		Card other = (Card) obj;
		if (castingCost == null) {
			if (other.castingCost != null)
				return false;
		} else if (!castingCost.equals(other.castingCost))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (power == null) {
			if (other.power != null)
				return false;
		} else if (!power.equals(other.power))
			return false;
		if (ruleText == null) {
			if (other.ruleText != null)
				return false;
		} else if (!ruleText.equals(other.ruleText))
			return false;
		if (subType1 == null) {
			if (other.subType1 != null)
				return false;
		} else if (!subType1.equals(other.subType1))
			return false;
		if (subType2 == null) {
			if (other.subType2 != null)
				return false;
		} else if (!subType2.equals(other.subType2))
			return false;
		if (superType == null) {
			if (other.superType != null)
				return false;
		} else if (!superType.equals(other.superType))
			return false;
		if (toughness == null) {
			if (other.toughness != null)
				return false;
		} else if (!toughness.equals(other.toughness))
			return false;
		return true;
	}

	public static class Builder {
		private String	hasStyleing		= "false";
		private String	notes			= "generated";
		private String	timeCreated		= "";
		private String	timeModified	= "";
		private String	name			= "";
		private String	castingCost		= "";
		private String	image			= "";
		private String	superType		= "";
		private String	subType1		= "";
		private String	subType2		= "";
		private String	ruleText		= "";
		private String	flavorText		= "";
		private String	power			= "";
		private String	toughness		= "";
		private String	copyright		= "";
		private String	image2			= "";
		private String	copyright2		= "";

		public Card build() {
			return new Card(hasStyleing, notes, timeCreated, timeModified, name, castingCost, image, superType,
					subType1, subType2, ruleText, flavorText, power, toughness, copyright, image2, copyright2);
		}

		public Builder hasStyleing(String hasStyleing) {
			this.hasStyleing = hasStyleing;
			return this;
		}

		public Builder notes(String notes) {
			this.notes = notes;
			return this;
		}

		public Builder timeCreated(String timeCreated) {
			this.timeCreated = timeCreated;
			return this;
		}

		public Builder timeModified(String timeModified) {
			this.timeModified = timeModified;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder castingCost(String castingCost) {
			this.castingCost = castingCost;
			return this;
		}

		public Builder image(String image) {
			this.image = image;
			return this;
		}

		public Builder superType(String superType) {
			this.superType = superType;
			return this;
		}

		public Builder subType1(String subType1) {
			this.subType1 = subType1;
			return this;
		}

		public Builder subType2(String subType2) {
			this.subType2 = subType2;
			return this;
		}

		public Builder ruleText(String ruleText) {
			this.ruleText = ruleText;
			return this;
		}

		public Builder flavorText(String flavorText) {
			this.flavorText = flavorText;
			return this;
		}

		public Builder power(String power) {
			this.power = power;
			return this;
		}

		public Builder toughness(String toughness) {
			this.toughness = toughness;
			return this;
		}

		public Builder copyright(String copyright) {
			this.copyright = copyright;
			return this;
		}

		public Builder image2(String image2) {
			this.image2 = image2;
			return this;
		}

		public Builder copyright2(String copyright2) {
			this.copyright2 = copyright2;
			return this;
		}

	}
}