package nl.maarten.zuul;

import java.util.ArrayList;

public class Player {

	private String name;
	private ArrayList<Item> items;
	private Room currentRoom;
	
	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public Player(String name) {
		this.name = name;
		items = new ArrayList<>();
	}
	
	public void takeItem(Item item) {
		items.add(item);
	}

	public void dropItem(Item item) {
		items.remove(item);
	}

    public String getItemsString() {
    	String returnString = "Items in " + getName() + "s bag: ";
    	for (Item item : items) {
    		returnString += " " + item.getName() + ",";
    	}
    	return returnString;
    }

	public String getName() {
		return name;
	}

}
