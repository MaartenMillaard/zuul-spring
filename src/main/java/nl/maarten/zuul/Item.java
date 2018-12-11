package nl.maarten.zuul;

public class Item {

	private String name;
	private String description;
	private long weight;
	
	public Item(String name, String description, long weight) {
		this.name = name;
		this.description = description;
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public long getWeight() {
		return weight;
	}

}
